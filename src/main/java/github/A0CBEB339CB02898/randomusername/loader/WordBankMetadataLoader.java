package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.Style;
import github.A0CBEB339CB02898.randomusername.config.TimeType;
import github.A0CBEB339CB02898.randomusername.model.WordBankMetadata;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 词库元数据加载器
 * 负责加载和解析词库的元数据文件（.meta.yml）
 */
public class WordBankMetadataLoader {

    private final Yaml yaml;

    public WordBankMetadataLoader() {
        this.yaml = new Yaml();
    }

    /**
     * 加载词库元数据
     * @param metadataPath 元数据文件路径
     * @return WordBankMetadata 实例，如果文件不存在返回 null
     */
    public WordBankMetadata loadMetadata(String metadataPath) {
        if (metadataPath == null || metadataPath.isEmpty()) {
            return null;
        }

        try {
            if (metadataPath.startsWith(WordBankConstants.HTTP_PREFIX)) {
                return loadMetadataFromHttp(metadataPath);
            } else {
                return loadMetadataFromPath(metadataPath);
            }
        } catch (FileNotFoundException e) {
            // 元数据文件不存在是正常情况，返回 null
            return null;
        } catch (Exception e) {
            throw new UsernameGeneratorException("Failed to load metadata from: " + metadataPath, e);
        }
    }

    /**
     * 从 HTTP URL 加载元数据
     */
    private WordBankMetadata loadMetadataFromHttp(String urlString) throws Exception {
        HttpURLConnection connection = createHttpConnection(urlString);
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream is = connection.getInputStream();
                     InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    return yaml.loadAs(reader, WordBankMetadata.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return null;
            } else {
                throw new UsernameGeneratorException("Failed to load metadata from URL: " + urlString + " (HTTP " + responseCode + ")");
            }
        } finally {
            connection.disconnect();
        }
    }

    /**
     * 从文件系统或 classpath 加载元数据
     */
    private WordBankMetadata loadMetadataFromPath(String path) throws IOException {
        try (InputStream is = getInputStream(path)) {
            if (is == null) {
                return null;
            }
            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                return yaml.loadAs(reader, WordBankMetadata.class);
            }
        }
    }

    /**
     * 获取输入流
     */
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
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return new FileInputStream(file);
    }

    /**
     * 创建 HTTP 连接
     */
    private HttpURLConnection createHttpConnection(String urlString) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(WordBankConstants.HTTP_LOAD_TIMEOUT);
        connection.setReadTimeout(WordBankConstants.HTTP_LOAD_TIMEOUT);
        return connection;
    }

    /**
     * 批量加载目录下所有元数据文件
     * @param directoryPath 目录路径
     * @return 文件名 -> 元数据的映射
     */
    public Map<String, WordBankMetadata> loadAllMetadataInDirectory(String directoryPath) {
        Map<String, WordBankMetadata> metadataMap = new HashMap<>();

        // 基础词库文件
        String[] basicFiles = {
            WordBankConstants.ADJECTIVES,
            WordBankConstants.NOUNS,
            WordBankConstants.PREFIXES
        };

        for (String fileName : basicFiles) {
            String metadataPath = getMetadataPath(directoryPath, fileName);
            WordBankMetadata metadata = loadMetadata(metadataPath);
            if (metadata != null) {
                metadataMap.put(fileName, metadata);
            }
        }

        // 风格词库文件 - 从 Style 枚举自动获取
        for (Style style : Style.values()) {
            String fileName = WordBankConstants.STYLE_PREFIX + style.getKey() + WordBankConstants.FILE_EXTENSION;
            String metadataPath = getMetadataPath(directoryPath, fileName);
            WordBankMetadata metadata = loadMetadata(metadataPath);
            if (metadata != null) {
                metadataMap.put(fileName, metadata);
            }
        }

        // 时间词库文件 - 从 TimeType 枚举自动获取
        for (TimeType timeType : TimeType.values()) {
            String fileName = WordBankConstants.TIME_PREFIX + timeType.getKey() + WordBankConstants.FILE_EXTENSION;
            String metadataPath = getMetadataPath(directoryPath, fileName);
            WordBankMetadata metadata = loadMetadata(metadataPath);
            if (metadata != null) {
                metadataMap.put(fileName, metadata);
            }
        }

        return metadataMap;
    }

    /**
     * 根据词库文件名获取对应的元数据文件路径
     * @param directoryPath 目录路径
     * @param fileName 词库文件名（例如：adjectives.txt）
     * @return 元数据文件路径（例如：adjectives.meta.yml）
     */
    private String getMetadataPath(String directoryPath, String fileName) {
        String normalizedPath = directoryPath.endsWith("/") ? directoryPath : directoryPath + "/";
        String baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
        return normalizedPath + baseFileName + ".meta.yml";
    }
}

