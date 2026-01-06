package github.A0CBEB339CB02898.randomusername.loader;

import com.google.gson.Gson;
import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.model.WordBank;
import github.A0CBEB339CB02898.randomusername.model.WordBankConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * JSON词库加载器
 * 从JSON格式文件加载词库配置
 */
public class JsonWordLoader extends AbstractWordLoader {

    private final Gson gson = new Gson();

    @Override
    public WordBank load(String path, Language language) throws Exception {
        WordBankConfig config = loadConfig(path, language);
        return convertToWordBank(config);
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

    /**
     * 加载JSON配置
     */
    private WordBankConfig loadConfig(String path, Language language) throws Exception {
        String jsonPath = resolveJsonPath(path, language);

        if (jsonPath.startsWith(WordBankConstants.HTTP_PREFIX)) {
            return loadFromHttp(jsonPath);
        } else {
            return loadFromPath(jsonPath);
        }
    }

    /**
     * 解析JSON文件路径
     */
    private String resolveJsonPath(String path, Language language) {
        // 如果path已经是完整的JSON文件路径，直接返回
        if (path.endsWith(".json")) {
            return path;
        }

        // 如果是目录路径，构建JSON文件名
        String normalizedPath = path.endsWith("/") ? path : path + "/";
        return normalizedPath + "wordbank-" + language.name().toLowerCase() + ".json";
    }

    /**
     * 从HTTP加载
     */
    private WordBankConfig loadFromHttp(String urlString) throws Exception {
        HttpURLConnection connection = createHttpConnection(urlString, "GET", WordBankConstants.HTTP_LOAD_TIMEOUT);
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream is = connection.getInputStream();
                     InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    return gson.fromJson(reader, WordBankConfig.class);
                }
            } else {
                throw new UsernameGeneratorException("Failed to load from URL: " + urlString + " (HTTP " + responseCode + ")");
            }
        } finally {
            connection.disconnect();
        }
    }

    /**
     * 从文件系统或classpath加载
     */
    private WordBankConfig loadFromPath(String path) throws IOException {
        try (InputStream is = getInputStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("JSON file not found: " + path);
            }
            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                return gson.fromJson(reader, WordBankConfig.class);
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
        return new FileInputStream(path);
    }

    /**
     * 将JSON配置转换为WordBank
     */
    private WordBank convertToWordBank(WordBankConfig config) {
        WordBank wordBank = new WordBank();

        // 加载基础词库
        if (config.getBasic() != null) {
            WordBankConfig.BasicWords basic = config.getBasic();

            if (basic.getAdjectives() != null) {
                basic.getAdjectives().forEach(wordBank::addAdjective);
            }

            if (basic.getNouns() != null) {
                basic.getNouns().forEach(wordBank::addNoun);
            }

            if (basic.getPrefixes() != null) {
                basic.getPrefixes().forEach(wordBank::addPrefix);
            }
        }

        // 加载风格模板
        if (config.getStyles() != null) {
            for (Map.Entry<String, WordBankConfig.StyleConfig> entry : config.getStyles().entrySet()) {
                String styleKey = entry.getKey();
                List<String> templates = entry.getValue().getTemplates();
                if (templates != null) {
                    templates.forEach(template -> wordBank.addStyleTemplate(styleKey, template));
                }
            }
        }

        // 加载时间模板
        if (config.getTimes() != null) {
            for (Map.Entry<String, WordBankConfig.TimeConfig> entry : config.getTimes().entrySet()) {
                String timeKey = entry.getKey();
                List<String> templates = entry.getValue().getTemplates();
                if (templates != null) {
                    templates.forEach(template -> wordBank.addTimeTemplate(timeKey, template));
                }
            }
        }

        return wordBank;
    }

    /**
     * 获取HTTP文件时间戳
     */
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

    /**
     * 获取文件时间戳
     */
    private long getFileTimestamp(String path) {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            return -1; // classpath资源不支持时间戳
        }
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return file.lastModified();
    }

    /**
     * 创建HTTP连接
     */
    private HttpURLConnection createHttpConnection(String urlString, String method, int timeout) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        return connection;
    }
}

