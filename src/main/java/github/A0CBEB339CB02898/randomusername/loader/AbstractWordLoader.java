package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 抽象词库加载器，提供公共的解析逻辑
 * 
 * 词库文件格式规范 (INI 风格):
 * [ADJECTIVES] - 每行一个形容词
 * [NOUNS] - 每行一个名词
 * [PREFIXES] - 每行一个前缀（用于 PrefixRandomStrategy）
 * [STYLE_STYLEKEY] - 基于风格的模板列表，STYLEKEY 对应 Style enum 的 key (如 [STYLE_EXPLORER])
 * [TIME_TIMEKEY] - 基于时间的模板列表，TIMEKEY 对应 morning, noon, night, weekend, normal
 * 
 * 模板占位符:
 * {N} - 随机 4 位数字
 * {random} 或 xxxx - 随机字符串 (长度和组成由 GeneratorConfig 决定)
 * 
 * # 开头的行为注释
 */
public abstract class AbstractWordLoader implements WordLoader {

    /**
     * 解析词库内容
     * @param reader 读取器
     * @return WordBank 实例
     * @throws IOException IO 异常
     */
    protected WordBank parse(BufferedReader reader) throws IOException {
        WordBank wordBank = new WordBank();
        merge(wordBank, reader);
        return wordBank;
    }

    /**
     * 将读取的词库内容合并到已有的 WordBank 中
     * @param wordBank 目标 WordBank
     * @param reader 读取器
     * @throws IOException IO 异常
     */
    protected void merge(WordBank wordBank, BufferedReader reader) throws IOException {
        String line;
        String currentSection = "";
        boolean isFirstLine = true;
        while ((line = reader.readLine()) != null) {
            // 移除 BOM 字符（仅在第一行）
            if (isFirstLine) {
                if (line.length() > 0 && line.charAt(0) == '\uFEFF') {
                    line = line.substring(1);
                }
                isFirstLine = false;
            }

            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1).toUpperCase();
                continue;
            }

            switch (currentSection) {
                case WordBankConstants.SECTION_ADJECTIVES:
                    wordBank.addAdjective(line);
                    break;
                case WordBankConstants.SECTION_NOUNS:
                    wordBank.addNoun(line);
                    break;
                case WordBankConstants.SECTION_PREFIXES:
                    wordBank.addPrefix(line);
                    break;
                default:
                    if (currentSection.startsWith(WordBankConstants.SECTION_STYLE_PREFIX)) {
                        wordBank.addStyleTemplate(currentSection.substring(WordBankConstants.SECTION_STYLE_PREFIX.length()).toLowerCase(), line);
                    } else if (currentSection.startsWith(WordBankConstants.SECTION_TIME_PREFIX)) {
                        wordBank.addTimeTemplate(currentSection.substring(WordBankConstants.SECTION_TIME_PREFIX.length()).toLowerCase(), line);
                    }
                    break;
            }
        }
    }
}
