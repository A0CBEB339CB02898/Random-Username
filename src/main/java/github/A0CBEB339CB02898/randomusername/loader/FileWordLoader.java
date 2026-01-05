package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.config.Style;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 基于文件的词库加载器
 * 支持从本地文件系统或 classpath 加载
 */
public class FileWordLoader extends AbstractWordLoader {

    /**
     * 从指定路径加载词库文件或目录
     * @param path 词库路径，支持 "classpath:" 前缀
     * @param language 语言
     * @return 加载后的 WordBank 实例
     * @throws Exception 如果文件读取失败
     */
    @Override
    public WordBank load(String path, Language language) throws Exception {
        WordBank wordBank = new WordBank();
        if (isDirectory(path)) {
            loadFromDirectory(wordBank, path);
        } else {
            loadSingleFileContent(wordBank, path);
        }
        return wordBank;
    }

    private boolean isDirectory(String path) {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            // 在 classpath 下，约定以 / 结尾为目录
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

        // 加载风格文件
        for (Style style : Style.values()) {
            loadSingleFile(wordBank, normalizedPath + WordBankConstants.STYLE_PREFIX + style.getKey() + WordBankConstants.FILE_EXTENSION);
        }

        // 加载时间文件
        String[] timeKeys = {"morning", "noon", "night", "weekend", "normal"};
        for (String timeKey : timeKeys) {
            loadSingleFile(wordBank, normalizedPath + WordBankConstants.TIME_PREFIX + timeKey + WordBankConstants.FILE_EXTENSION);
        }
    }

    private void loadSingleFile(WordBank wordBank, String filePath) throws IOException {
        try (InputStream is = getInputStream(filePath)) {
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    merge(wordBank, reader);
                }
            }
        } catch (FileNotFoundException ignored) {
            // 如果文件不存在，忽略即可
        }
    }

    private void loadSingleFileContent(WordBank wordBank, String filePath) throws IOException {
        try (InputStream is = getInputStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("词库文件未找到: " + filePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                merge(wordBank, reader);
            }
        }
    }

    private InputStream getInputStream(String path) throws FileNotFoundException {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            String resourcePath = path.substring(WordBankConstants.CLASSPATH_PREFIX.length());
            if (resourcePath.startsWith("/")) {
                resourcePath = resourcePath.substring(1);
            }
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                // 如果直接找不到，尝试当前线程加载器
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
            }
            return is;
        }
        return new FileInputStream(path);
    }

    @Override
    public long getTimestamp(String path) {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            return -1;
        }
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return file.lastModified();
    }
}
