package io.github.A0CBEB339CB02898.randomusername.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用户名生成配置类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorConfig {
    /** 生成模式，默认为形容词+名词 */
    @Builder.Default
    private GenerationMode mode = GenerationMode.ADJ_NOUN_RANDOM;
    
    /** 语言风格，默认为中文 */
    @Builder.Default
    private Language language = Language.ZH;
    
    /** 特定风格模式下的风格选择，默认为 DEFAULT */
    @Builder.Default
    private Style style = Style.DEFAULT;

    /** 随机后缀长度，默认为 6 */
    @Builder.Default
    private int suffixLength = 6;

    /** 注册时间，用于时段形容词识别，默认为当前时间 */
    @Builder.Default
    private LocalDateTime registrationTime = LocalDateTime.now();
    
    /** 外部词库路径（文件路径或 HTTP URL） */
    private String wordBankPath;

    /** 随机后缀是否包含数字，默认为 true */
    @Builder.Default
    private boolean useNumbers = true;
    
    /** 随机后缀是否包含字母，默认为 true */
    @Builder.Default
    private boolean useLetters = true;

    /** 是否启用时段形容词识别，默认为 false */
    @Builder.Default
    private boolean enableTimeBasedAdjective = false;

    /** 是否使用词库缓存，默认为 true。关闭后每次生成都会重新加载词库 */
    @Builder.Default
    private boolean useCache = true;
}
