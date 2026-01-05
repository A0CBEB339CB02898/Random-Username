package github.A0CBEB339CB02898.randomusername.loader;

/**
 * 词库常量定义
 */
public final class WordBankConstants {
    private WordBankConstants() {}

    public static final String ADJECTIVES = "adjectives.txt";
    public static final String NOUNS = "nouns.txt";
    public static final String PREFIXES = "prefixes.txt";
    
    public static final String STYLE_PREFIX = "style_";
    public static final String TIME_PREFIX = "time_";
    public static final String FILE_EXTENSION = ".txt";

    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String HTTP_PREFIX = "http";

    public static final String SECTION_ADJECTIVES = "ADJECTIVES";
    public static final String SECTION_NOUNS = "NOUNS";
    public static final String SECTION_PREFIXES = "PREFIXES";
    public static final String SECTION_STYLE_PREFIX = "STYLE_";
    public static final String SECTION_TIME_PREFIX = "TIME_";

    // HTTP 连接配置
    public static final int HTTP_LOAD_TIMEOUT = 10000;
    public static final int HTTP_TIMESTAMP_TIMEOUT = 3000;
}
