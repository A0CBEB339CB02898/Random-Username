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
    /** 词库元数据映射，Key 为文件名，Value 为元数据对象 */
    private Map<String, WordBankMetadata> metadata = new HashMap<>();

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

    /**
     * 添加词库元数据
     * @param fileName 文件名
     * @param meta 元数据对象
     */
    public void addMetadata(String fileName, WordBankMetadata meta) {
        if (meta != null) {
            metadata.put(fileName, meta);
        }
    }

    /**
     * 获取词库元数据
     * @param fileName 文件名
     * @return 元数据对象，不存在则返回 null
     */
    public WordBankMetadata getMetadata(String fileName) {
        return metadata.get(fileName);
    }

    /**
     * 获取词库统计信息（包含元数据）
     * @return 统计信息字符串
     */
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("WordBank Statistics:\n");
        sb.append("  Adjectives: ").append(adjectives.size()).append("\n");
        sb.append("  Nouns: ").append(nouns.size()).append("\n");
        sb.append("  Prefixes: ").append(prefixes.size()).append("\n");
        sb.append("  Styles: ").append(styles.size()).append(" types\n");
        sb.append("  Time Templates: ").append(timeTemplates.size()).append(" types\n");
        sb.append("  Metadata entries: ").append(metadata.size()).append("\n");

        if (!metadata.isEmpty()) {
            sb.append("\nMetadata Details:\n");
            metadata.forEach((fileName, meta) -> {
                sb.append("  - ").append(fileName).append(": ").append(meta.getSummary()).append("\n");
            });
        }

        return sb.toString();
    }
}
