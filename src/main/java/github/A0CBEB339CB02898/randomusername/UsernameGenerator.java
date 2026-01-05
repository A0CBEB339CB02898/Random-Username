package github.A0CBEB339CB02898.randomusername;

import github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.loader.WordBankConstants;
import github.A0CBEB339CB02898.randomusername.loader.WordLoader;
import github.A0CBEB339CB02898.randomusername.loader.UnifiedWordLoader;
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
    /** 词库缓存 */
    private final Map<String, WordBank> wordBankCache = new ConcurrentHashMap<>();
    /** 策略映射，按生成模式分类 */
    private final Map<GenerationMode, UsernameStrategy> strategies = new HashMap<>();
    
    /** 统一词库加载器 */
    private final WordLoader wordLoader = new UnifiedWordLoader();

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
                (lang == Language.ZH ? WordBankConstants.CLASSPATH_PREFIX + "dict/zh/" : WordBankConstants.CLASSPATH_PREFIX + "dict/en/");
        String cacheKey = (path != null && !path.isEmpty()) ? path : "DEFAULT_" + lang.name();

        // 如果不使用缓存，直接加载
        if (!useCache) {
            return loadWordBankInternal(actualPath, lang);
        }

        // 简化缓存逻辑：直接使用缓存或加载
        return wordBankCache.computeIfAbsent(cacheKey, key -> loadWordBankInternal(actualPath, lang));
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

    /**
     * 清除词库缓存
     */
    public void clearCache() {
        wordBankCache.clear();
    }

    /**
     * 重新加载默认词库
     * @param lang 语言
     */
    public void reload(Language lang) {
        wordBankCache.remove("DEFAULT_" + lang.name());
    }

    /**
     * 重新加载指定路径的词库
     * @param path 词库路径
     */
    public void reload(String path) {
        if (path != null) {
            wordBankCache.remove(path);
        }
    }
}
