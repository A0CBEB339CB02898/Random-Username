package github.A0CBEB339CB02898.randomusername.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.ArrayList;

/**
 * 词库元数据类
 * 用于描述词库文件的详细信息，支持验证和版本管理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordBankMetadata {
    /** 词库名称 */
    private String name;

    /** 词库描述 */
    private String description;

    /** 词库版本 */
    @Builder.Default
    private String version = "1.0";

    /** 词库语言（zh/en） */
    private String language;

    /** 词库类别（basic/style/time） */
    private String category;

    /** 词库关键词 */
    @Builder.Default
    private List<String> keywords = new ArrayList<>();

    /** 最小词条数量（用于验证） */
    @Builder.Default
    private int minEntries = 0;

    /** 最大词条数量（可选，用于验证） */
    private Integer maxEntries;

    /** 作者 */
    private String author;

    /** 创建日期 */
    private String createdDate;

    /** 最后更新日期 */
    private String lastUpdated;

    /** 词库文件路径（相对路径） */
    private String filePath;

    /** 依赖的其他词库（可选） */
    @Builder.Default
    private List<String> dependencies = new ArrayList<>();

    /** 是否为必需词库 */
    @Builder.Default
    private boolean required = true;

    /** 自定义属性（扩展用） */
    @Builder.Default
    private java.util.Map<String, Object> customProperties = new java.util.HashMap<>();

    /**
     * 验证元数据是否有效
     * @return 验证是否通过
     */
    public boolean isValid() {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (language == null || language.trim().isEmpty()) {
            return false;
        }
        if (category == null || category.trim().isEmpty()) {
            return false;
        }
        if (minEntries < 0) {
            return false;
        }
        if (maxEntries != null && maxEntries < minEntries) {
            return false;
        }
        return true;
    }

    /**
     * 获取元数据摘要信息
     * @return 摘要字符串
     */
    public String getSummary() {
        return String.format("%s (v%s) - %s [%s/%s]",
            name, version, description != null ? description : "No description",
            language, category);
    }
}

