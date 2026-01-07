package io.github.A0CBEB339CB02898.randomusername.strategy;

import io.github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import io.github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import io.github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import io.github.A0CBEB339CB02898.randomusername.config.Style;
import io.github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 统一风格随机策略
 * 支持两种生成格式：
 * 1. 形容词 + 名词 + 随机后缀 (ADJ_NOUN_RANDOM)
 * 2. 名词 + 随机后缀 (NOUN_RANDOM)
 *
 * 集成时段形容词识别：
 * - 当 enableTimeBasedAdjective=true 时，强制使用 ADJ_NOUN_RANDOM 格式
 * - 根据时间段选择对应的形容词：凌晨、上午、下午、夜晚
 * - 优先使用风格的时段形容词，如无则回退到基础时段形容词
 */
public class StyleRandomStrategy extends UsernameStrategy {

    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        GenerationMode mode = config.getMode();
        Style style = config.getStyle() != null ? config.getStyle() : Style.DEFAULT;

        // 获取对应风格的词库
        WordBank.StyleWordBank styleWordBank = wordBank.getStyle(style.getKey());
        if (styleWordBank == null) {
            throw new UsernameGeneratorException("词库中未找到风格: " + style.name());
        }

        // 如果启用时段形容词，强制使用 ADJ_NOUN_RANDOM 格式（忽略 NOUN_RANDOM 配置）
        GenerationMode actualMode = config.isEnableTimeBasedAdjective()
            ? GenerationMode.ADJ_NOUN_RANDOM
            : mode;

        String username = buildUsername(actualMode, styleWordBank, wordBank, config);

        // 添加随机后缀
        String suffix = generateRandomString(config.getSuffixLength(), config.isUseNumbers(), config.isUseLetters());
        return username + "_" + suffix;
    }

    /**
     * 构建用户名的主体部分（不包括后缀）
     */
    private String buildUsername(GenerationMode mode, WordBank.StyleWordBank styleWordBank,
                                  WordBank wordBank, GeneratorConfig config) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (mode == GenerationMode.ADJ_NOUN_RANDOM) {
            // 形容词 + 名词
            String adjective = selectAdjective(styleWordBank, wordBank, config);
            String noun = selectNoun(styleWordBank, wordBank, random);
            return adjective + noun;

        } else if (mode == GenerationMode.NOUN_RANDOM) {
            // 仅名词（仅在未启用时段形容词时使用）
            return selectNoun(styleWordBank, wordBank, random);

        } else {
            throw new UsernameGeneratorException("不支持的生成模式: " + mode);
        }
    }

    /**
     * 选择形容词
     * 优先级：
     * - 若启用时段形容词：风格时段形容词 > 基础时段形容词 > 风格形容词 > 基础形容词
     * - 若未启用时段形容词：风格形容词 > 基础形容词
     */
    private String selectAdjective(WordBank.StyleWordBank styleWordBank, WordBank wordBank, GeneratorConfig config) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 如果启用时段形容词，优先使用时段形容词
        if (config.isEnableTimeBasedAdjective()) {
            String timeKey = getTimeKey(config.getRegistrationTime());

            // 1. 先尝试获取风格的时段形容词
            List<String> styleTimeAdjs = styleWordBank.getTimeAdjectives(timeKey);
            if (!styleTimeAdjs.isEmpty()) {
                return styleTimeAdjs.get(random.nextInt(styleTimeAdjs.size()));
            }

            // 2. 回退到基础时段形容词（从基础风格中获取）
            WordBank.StyleWordBank defaultStyle = wordBank.getStyles().get("default");
            if (defaultStyle != null) {
                List<String> basicTimeAdjs = defaultStyle.getTimeAdjectives(timeKey);
                if (!basicTimeAdjs.isEmpty()) {
                    return basicTimeAdjs.get(random.nextInt(basicTimeAdjs.size()));
                }
            }
        }

        // 3. 回退到风格形容词
        List<String> styleAdjs = styleWordBank.getAdjectives();
        if (!styleAdjs.isEmpty()) {
            return styleAdjs.get(random.nextInt(styleAdjs.size()));
        }

        // 4. 最后回退到基础形容词
        List<String> basicAdjs = wordBank.getAdjectives();
        if (basicAdjs.isEmpty()) {
            throw new UsernameGeneratorException("词库中缺少形容词");
        }
        return basicAdjs.get(random.nextInt(basicAdjs.size()));
    }

    /**
     * 选择名词
     * 优先级：风格名词 > 基础名词
     */
    private String selectNoun(WordBank.StyleWordBank styleWordBank, WordBank wordBank, ThreadLocalRandom random) {
        // 优先使用风格名词
        List<String> styleNouns = styleWordBank.getNouns();
        if (!styleNouns.isEmpty()) {
            return styleNouns.get(random.nextInt(styleNouns.size()));
        }

        // 回退到基础名词
        List<String> basicNouns = wordBank.getNouns();
        if (!basicNouns.isEmpty()) {
            return basicNouns.get(random.nextInt(basicNouns.size()));
        }

        throw new UsernameGeneratorException("词库中缺少名词");
    }

    /**
     * 根据时间获取时间段标识
     * 新的时段划分：
     * - early（凌晨）：00:00 - 06:00
     * - morning（上午）：06:00 - 12:00
     * - afternoon（下午）：12:00 - 18:00
     * - night（夜晚）：18:00 - 24:00
     */
    private String getTimeKey(LocalDateTime dateTime) {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }

        LocalTime time = dateTime.toLocalTime();

        // 凌晨：0:00 - 6:00
        if (time.isBefore(LocalTime.of(6, 0))) {
            return "early";
        }
        // 上午：6:00 - 12:00
        else if (time.isBefore(LocalTime.of(12, 0))) {
            return "morning";
        }
        // 下午：12:00 - 18:00
        else if (time.isBefore(LocalTime.of(18, 0))) {
            return "afternoon";
        }
        // 夜晚：18:00 - 24:00
        else {
            return "night";
        }
    }
}
