package github.A0CBEB339CB02898.randomusername.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 词库模型类
 * 存储形容词、名词、风格模板和时间模板
 */
@Data
public class WordBank {
    /** 形容词列表 */
    private List<String> adjectives = new ArrayList<>();
    /** 名词列表 */
    private List<String> nouns = new ArrayList<>();
    /** 风格模板映射，Key 为风格名称（如 explorer），Value 为该风格下的模板列表 */
    private Map<String, List<String>> styles = new HashMap<>();
    /** 时间模板映射，Key 为时间段（如 morning, night），Value 为对应的模板列表 */
    private Map<String, List<String>> timeTemplates = new HashMap<>();
    /** 预定义前缀列表 */
    private List<String> prefixes = new ArrayList<>();

    /**
     * 添加一个形容词
     * @param adj 形容词
     */
    public void addAdjective(String adj) {
        adjectives.add(adj);
    }

    /**
     * 添加一个名词
     * @param noun 名词
     */
    public void addNoun(String noun) {
        nouns.add(noun);
    }

    /**
     * 添加一个风格模板
     * @param style 风格标识
     * @param template 模板字符串
     */
    public void addStyleTemplate(String style, String template) {
        styles.computeIfAbsent(style, k -> new ArrayList<>()).add(template);
    }

    /**
     * 添加一个时间模板
     * @param timeType 时间段标识
     * @param template 模板字符串
     */
    public void addTimeTemplate(String timeType, String template) {
        timeTemplates.computeIfAbsent(timeType, k -> new ArrayList<>()).add(template);
    }

    /**
     * 添加一个前缀
     * @param prefix 前缀
     */
    public void addPrefix(String prefix) {
        prefixes.add(prefix);
    }
}

