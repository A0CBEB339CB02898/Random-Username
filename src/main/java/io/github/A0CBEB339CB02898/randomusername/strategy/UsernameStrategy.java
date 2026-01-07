package io.github.A0CBEB339CB02898.randomusername.strategy;

import io.github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import io.github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 用户名生成策略基类
 */
public abstract class UsernameStrategy {

    /**
     * 根据词库和配置生成用户名
     * @param wordBank 词库
     * @param config 配置
     * @return 生成的用户名
     */
    public abstract String generate(WordBank wordBank, GeneratorConfig config);

    /**
     * 生成指定长度的随机字符串
     * @param length 长度
     * @param useNumbers 是否包含数字
     * @param useLetters 是否包含字母
     * @return 随机字符串
     */
    protected String generateRandomString(int length, boolean useNumbers, boolean useLetters) {
        if (length <= 0) return "";
        
        String numbers = "0123456789";
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        StringBuilder chars = new StringBuilder();
        if (useNumbers) chars.append(numbers);
        if (useLetters) chars.append(letters);
        
        if (chars.length() == 0) return "";
        
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
