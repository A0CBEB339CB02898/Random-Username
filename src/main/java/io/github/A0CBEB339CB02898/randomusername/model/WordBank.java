package io.github.A0CBEB339CB02898.randomusername.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 词库模型类
 * 存储基础形容词/名词，以及各风格的专属词库和时段形容词
 */
@Data
public class WordBank {
    /** 基础形容词列表 */
    private List<String> adjectives = new ArrayList<>();
    /** 基础名词列表 */
    private List<String> nouns = new ArrayList<>();

    /**
     * 风格词库映射
     * Key: 风格标识（如 default, explorer）
     * Value: StyleWordBank 包含该风格的形容词、名词、时段形容词
     */
    private Map<String, StyleWordBank> styles = new HashMap<>();

    /**
     * 添加基础形容词
     */
    public void addAdjective(String adj) {
        adjectives.add(adj);
    }

    /**
     * 添加基础名词
     */
    public void addNoun(String noun) {
        nouns.add(noun);
    }

    /**
     * 添加风格词库
     */
    public void addStyle(String styleKey, StyleWordBank styleBank) {
        styles.put(styleKey, styleBank);
    }

    /**
     * 获取风格词库
     */
    public StyleWordBank getStyle(String styleKey) {
        return styles.get(styleKey);
    }

    /**
     * 风格词库内部类
     */
    @Data
    public static class StyleWordBank {
        /** 风格名称 */
        private String name;
        /** 该风格的形容词列表 */
        private List<String> adjectives = new ArrayList<>();
        /** 该风格的名词列表 */
        private List<String> nouns = new ArrayList<>();
        /** 该风格的时段形容词映射 */
        private Map<String, List<String>> timeAdjectives = new HashMap<>();

        public StyleWordBank() {
        }

        public StyleWordBank(String name) {
            this.name = name;
        }

        /**
         * 添加形容词
         */
        public void addAdjective(String adj) {
            adjectives.add(adj);
        }

        /**
         * 添加名词
         */
        public void addNoun(String noun) {
            nouns.add(noun);
        }

        /**
         * 添加时段形容词
         */
        public void addTimeAdjective(String timeType, String adj) {
            timeAdjectives.computeIfAbsent(timeType, k -> new ArrayList<>()).add(adj);
        }

        /**
         * 获取指定时段的形容词列表
         */
        public List<String> getTimeAdjectives(String timeType) {
            return timeAdjectives.getOrDefault(timeType, new ArrayList<>());
        }
    }
}
