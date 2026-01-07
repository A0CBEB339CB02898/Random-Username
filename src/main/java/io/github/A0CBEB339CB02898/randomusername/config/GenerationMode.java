package io.github.A0CBEB339CB02898.randomusername.config;

/**
 * 用户名生成模式 - 仅支持两种格式
 */
public enum GenerationMode {
    /**
     * 形容词 + 名词 + 随机后缀（_xxxx）
     * 例如：勇敢的冒险者_aBc2
     */
    ADJ_NOUN_RANDOM,

    /**
     * 名词 + 随机后缀（_xxxx）
     * 例如：冒险家_xY9k
     */
    NOUN_RANDOM
}
