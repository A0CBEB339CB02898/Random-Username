package github.A0CBEB339CB02898.randomusername.config;

import lombok.Getter;

/**
 * 用户名风格定义
 */
@Getter
public enum Style {
    /** 🌍 探索者维度 */
    EXPLORER("explorer"),
    /** 🔥 情感与态度维度 */
    ATTITUDE("attitude"),
    /** 🗡️ 江湖/武侠维度 */
    JIANGHU("jianghu"),
    /** 🎨 意象与浪漫维度 */
    ROMANTIC("romantic");

    private final String key;

    Style(String key) {
        this.key = key;
    }

}
