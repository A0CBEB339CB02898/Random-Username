package io.github.A0CBEB339CB02898.randomusername.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * JSON词库配置模型
 * 对应 wordbank-*.json 文件的结构
 */
@Data
public class WordBankConfig {

    /** 语言标识 */
    private String language;

    /** 版本号 */
    private String version;

    /** 描述信息 */
    private String description;

    /** 基础词库 */
    private BasicWords basic;

    /** 风格模板 */
    private Map<String, StyleConfig> styles;

    /** 时间模板 */
    private Map<String, TimeConfig> times;

    /**
     * 基础词库
     */
    @Data
    public static class BasicWords {
        /** 形容词列表 */
        private List<String> adjectives;

        /** 名词列表 */
        private List<String> nouns;

        /** 前缀列表 */
        private List<String> prefixes;
    }

    /**
     * 风格配置
     */
    @Data
    public static class StyleConfig {
        /** 风格名称 */
        private String name;

        /** 风格描述 */
        private String description;

        /** 风格表情符号 */
        private String emoji;

        /** 模板列表 */
        private List<String> templates;
    }

    /**
     * 时间配置
     */
    @Data
    public static class TimeConfig {
        /** 时间段名称 */
        private String name;

        /** 时间范围（如 "06:00-10:00"） */
        private String timeRange;

        /** 时间段描述 */
        private String description;

        /** 模板列表 */
        private List<String> templates;
    }
}

