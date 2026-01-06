package github.A0CBEB339CB02898.randomusername;

import github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.loader.JsonWordLoader;
import github.A0CBEB339CB02898.randomusername.loader.WordBankConstants;
import github.A0CBEB339CB02898.randomusername.model.WordBank;
import github.A0CBEB339CB02898.randomusername.strategy.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户名生成器主类
 * 提供多种生成模式，支持中英文，可配置外部词库
 */
public class UsernameGenerator {
    /** 缓存项，包含词库和时间戳 */
    private static class CacheEntry {
        WordBank wordBank;
        long timestamp;

        CacheEntry(WordBank wordBank, long timestamp) {
            this.wordBank = wordBank;
            this.timestamp = timestamp;
        }
    }

    /** 词库缓存 */
    private final Map<String, CacheEntry> wordBankCache = new ConcurrentHashMap<>();
    /** 策略映射，按生成模式分类 */
    private final Map<GenerationMode, UsernameStrategy> strategies = new HashMap<>();
    
    /** JSON词库加载器 */
    private final JsonWordLoader wordLoader = new JsonWordLoader();

    /**
     * 构造函数，初始化生成策略
     */
    public UsernameGenerator() {
        // 基础随机策略：0=PREFIX_ONLY, 1=ADJ_NOUN, 2=NOUN_ONLY
        this.strategies.put(GenerationMode.PREFIX_RANDOM, new SimpleRandomStrategy(0));
        this.strategies.put(GenerationMode.ADJ_NOUN_RANDOM, new SimpleRandomStrategy(1));
        this.strategies.put(GenerationMode.NOUN_RANDOM, new SimpleRandomStrategy(2));

        // 模板策略
        this.strategies.put(GenerationMode.TIME_BASED, new TemplateBasedStrategy(true));
        this.strategies.put(GenerationMode.STYLE_BASED, new TemplateBasedStrategy(false));
    }

    /**
     * 根据给定配置生成用户名
     * @param config 生成配置
     * @return 生成的用户名字符串
     * @throws UsernameGeneratorException 如果词库加载失败或配置无效
     */
    public String generate(GeneratorConfig config) {
        try {
            WordBank wordBank = getWordBank(config);
            UsernameStrategy strategy = strategies.get(config.getMode());
            if (strategy == null) {
                throw new IllegalArgumentException("Unsupported generation mode: " + config.getMode());
            }
            return strategy.generate(wordBank, config);
        } catch (UsernameGeneratorException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameGeneratorException("Failed to generate username: " + e.getMessage(), e);
        }
    }

    /**
     * 获取词库，优先使用配置指定的路径，否则使用内置缓存
     * @param config 生成配置
     * @return WordBank 实例
     */
    private WordBank getWordBank(GeneratorConfig config) {
        Language lang = config.getLanguage();
        String path = config.getWordBankPath();
        boolean useCache = config.isUseCache();

        // 确定实际路径和缓存 Key
        String actualPath = (path != null && !path.isEmpty()) ? path :
                (WordBankConstants.CLASSPATH_PREFIX + "dict/");
        String cacheKey = (path != null && !path.isEmpty()) ? path : "DEFAULT_" + lang.name();

        // 如果不使用缓存，直接加载
        if (!useCache) {
            return loadWordBankInternal(actualPath, lang);
        }

        // 使用缓存：检查是否需要自动重新加载
        CacheEntry entry = wordBankCache.get(cacheKey);
        long currentTimestamp = wordLoader.getTimestamp(actualPath);

        if (entry == null) {
            // 缓存不存在，加载新词库
            WordBank wordBank = loadWordBankInternal(actualPath, lang);
            wordBankCache.put(cacheKey, new CacheEntry(wordBank, currentTimestamp));
            return wordBank;
        }

        // 如果获取到时间戳且不是classpath资源（-1），则检查是否需要重新加载
        if (currentTimestamp > 0 && currentTimestamp != entry.timestamp) {
            // 文件已更新，重新加载
            WordBank wordBank = loadWordBankInternal(actualPath, lang);
            wordBankCache.put(cacheKey, new CacheEntry(wordBank, currentTimestamp));
            return wordBank;
        }

        return entry.wordBank;
    }

    private WordBank loadWordBankInternal(String path, Language lang) {
        try {
            return wordLoader.load(path, lang);
        } catch (UsernameGeneratorException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameGeneratorException("从以下路径加载词库失败: " + path, e);
        }
    }
}
