package io.github.A0CBEB339CB02898.randomusername.config;

/**
 * 用户名生成模式
 */
public enum GenerationMode {
    /**
     * 1. 基于固定前缀 + 随机字符串（数字+字母）
     */
    PREFIX_RANDOM,

    /**
     * 2. 随机形容词 + 名词 + 随机字符串
     */
    ADJ_NOUN_RANDOM,

    /**
     * 3. 名词 + 随机字符串
     */
    NOUN_RANDOM,

    /**
     * 4. 根据注册时间，给定类似基于“时间+状态”的动态组合
     */
    TIME_BASED,

    /**
     * 5. 可选风格（如：探索者、态度、江湖、浪漫）
     */
    STYLE_BASED
}
