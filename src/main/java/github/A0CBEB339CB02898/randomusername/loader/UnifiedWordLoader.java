package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.config.Style;
import github.A0CBEB339CB02898.randomusername.config.TimeType;
import github.A0CBEB339CB02898.randomusername.model.WordBank;
import github.A0CBEB339CB02898.randomusername.model.WordBankMetadata;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一的词库加载器，同时支持文件和 HTTP 源
 */
public class UnifiedWordLoader extends AbstractWordLoader {

    private final WordBankMetadataLoader metadataLoader = new WordBankMetadataLoader();

    @Override
    public WordBank load(String path, Language language) throws Exception {
        WordBank wordBank = new WordBank();

        if (path.startsWith(WordBankConstants.HTTP_PREFIX)) {
            loadFromHttp(wordBank, path);
        } else {
            loadFromPath(wordBank, path);
        }

        return wordBank;
    }

    @Override
    public long getTimestamp(String path) {
        if (path == null || path.isEmpty()) {
            return 0;
        }

        if (path.startsWith(WordBankConstants.HTTP_PREFIX)) {
            return getHttpTimestamp(path);
        } else {
            return getFileTimestamp(path);
        }
    }

    private void loadFromHttp(WordBank wordBank, String urlString) throws Exception {
        HttpURLConnection connection = createHttpConnection(urlString, "GET", WordBankConstants.HTTP_LOAD_TIMEOUT);
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    merge(wordBank, reader);
                }
            } else {
                throw new UsernameGeneratorException("从以下URL加载词库失败: " + urlString + " (HTTP " + responseCode + ")");
            }
        } finally {
            connection.disconnect();
        }
    }

    private void loadFromPath(WordBank wordBank, String path) throws IOException {
        if (isDirectory(path)) {
            loadFromDirectory(wordBank, path);
        } else {
            loadSingleFile(wordBank, path);
        }
    }

    private boolean isDirectory(String path) {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            return path.endsWith("/");
        }
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    private void loadFromDirectory(WordBank wordBank, String path) throws IOException {
        String normalizedPath = path.endsWith("/") ? path : path + "/";

        // 加载基础文件
        loadSingleFile(wordBank, normalizedPath + WordBankConstants.ADJECTIVES);
        loadSingleFile(wordBank, normalizedPath + WordBankConstants.NOUNS);
        loadSingleFile(wordBank, normalizedPath + WordBankConstants.PREFIXES);

        // 加载风格和时间文件
        loadStyleAndTimeFiles(wordBank, normalizedPath);
    }

    private void loadStyleAndTimeFiles(WordBank wordBank, String basePath) throws IOException {
        // 加载风格文件 - 从 Style 枚举自动获取
        for (Style style : Style.values()) {
            loadSingleFile(wordBank, basePath + WordBankConstants.STYLE_PREFIX + style.getKey() + WordBankConstants.FILE_EXTENSION);
        }

        // 加载时间文件 - 从 TimeType 枚举自动获取
        for (TimeType timeType : TimeType.values()) {
            loadSingleFile(wordBank, basePath + WordBankConstants.TIME_PREFIX + timeType.getKey() + WordBankConstants.FILE_EXTENSION);
        }
    }

    private void loadSingleFile(WordBank wordBank, String filePath) throws IOException {
        try (InputStream is = getInputStream(filePath)) {
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    merge(wordBank, reader);
                }

                // 尝试加载对应的元数据文件
                String metadataPath = getMetadataPath(filePath);
                WordBankMetadata metadata = metadataLoader.loadMetadata(metadataPath);
                if (metadata != null) {
                    String fileName = extractFileName(filePath);
                    wordBank.addMetadata(fileName, metadata);
                }
            }
        } catch (FileNotFoundException ignored) {
            // 文件不存在，忽略
        }
    }

    /**
     * 根据词库文件路径获取元数据文件路径
     */
    private String getMetadataPath(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filePath.substring(0, lastDotIndex) + ".meta.yml";
        }
        return filePath + ".meta.yml";
    }

    /**
     * 从完整路径中提取文件名
     */
    private String extractFileName(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf('/');
        if (lastSlashIndex >= 0) {
            return filePath.substring(lastSlashIndex + 1);
        }
        int lastBackslashIndex = filePath.lastIndexOf('\\');
        if (lastBackslashIndex >= 0) {
            return filePath.substring(lastBackslashIndex + 1);
        }
        return filePath;
    }

    private InputStream getInputStream(String path) throws FileNotFoundException {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            String resourcePath = path.substring(WordBankConstants.CLASSPATH_PREFIX.length());
            if (resourcePath.startsWith("/")) {
                resourcePath = resourcePath.substring(1);
            }
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
            }
            return is;
        }
        return new FileInputStream(path);
    }

    private long getHttpTimestamp(String urlString) {
        try {
            HttpURLConnection connection = createHttpConnection(urlString, "HEAD", WordBankConstants.HTTP_TIMESTAMP_TIMEOUT);
            try {
                return connection.getLastModified();
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private long getFileTimestamp(String path) {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            return -1;
        }
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return file.lastModified();
    }

    private HttpURLConnection createHttpConnection(String urlString, String method, int timeout) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        return connection;
    }
}

