package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 基于 HTTP 的远程词库加载器
 * 支持从指定的 URL 下载并解析词库
 */
public class HttpWordLoader extends AbstractWordLoader {

    /**
     * 从指定的 URL 加载词库
     * @param urlString 远程词库的 URL 地址
     * @param language 语言
     * @return 加载后的 WordBank 实例
     * @throws Exception 如果网络连接失败或响应码不为 200
     */
    @Override
    public WordBank load(String urlString, Language language) throws Exception {
        HttpURLConnection connection = createHttpConnection(urlString, "GET", WordBankConstants.HTTP_LOAD_TIMEOUT);
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return parse(reader);
                }
            } else {
                throw new UsernameGeneratorException("从以下URL加载词库失败: " + urlString + " (HTTP " + responseCode + ")");
            }
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public long getTimestamp(String urlString) {
        if (urlString == null || !urlString.startsWith(WordBankConstants.HTTP_PREFIX)) {
            return 0;
        }
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
     * 创建 HTTP 连接并配置超时
     */
    private HttpURLConnection createHttpConnection(String urlString, String method, int timeout) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        return connection;
    }
}
