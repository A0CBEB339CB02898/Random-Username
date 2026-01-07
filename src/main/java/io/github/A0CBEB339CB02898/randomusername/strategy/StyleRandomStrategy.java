package io.github.A0CBEB339CB02898.randomusername.strategy;

import io.github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import io.github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import io.github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import io.github.A0CBEB339CB02898.randomusername.config.Style;
import io.github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 统一风格随机策略
 * 支持两种生成格式：
 * 1. 形容词 + 名词 + 随机后缀 (ADJ_NOUN_RANDOM)
 * 2. 名词 + 随机后缀 (NOUN_RANDOM)
 *
 * 集成时段形容词识别：
 * - 当 enableTimeBasedAdjective=true 时，根据时间段选择对应风格的形容词
 * - 如果风格无时段形容词，则回退到基础形容词
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

        String username = buildUsername(mode, styleWordBank, wordBank, config);

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
            String noun = selectNoun(styleWordBank, random);
            return adjective + noun;

        } else if (mode == GenerationMode.NOUN_RANDOM) {
            // 仅名词
            return selectNoun(styleWordBank, random);

        } else {
            throw new UsernameGeneratorException("不支持的生成模式: " + mode);
        }
    }

    /**
     * 选择形容词
     * 优先级：时段形容词（若启用） > 风格形容词 > 基础形容词
     */
    private String selectAdjective(WordBank.StyleWordBank styleWordBank, WordBank wordBank, GeneratorConfig config) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 如果启用时段形容词，先尝试获取时段形容词
        if (config.isEnableTimeBasedAdjective()) {
            String timeKey = getTimeKey(config.getRegistrationTime());
            List<String> timeAdjs = styleWordBank.getTimeAdjectives(timeKey);

            if (!timeAdjs.isEmpty()) {
                return timeAdjs.get(random.nextInt(timeAdjs.size()));
            }
        }

        // 回退到风格形容词
        List<String> styleAdjs = styleWordBank.getAdjectives();
        if (!styleAdjs.isEmpty()) {
            return styleAdjs.get(random.nextInt(styleAdjs.size()));
        }

        // 最后回退到基础形容词
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
    private String selectNoun(WordBank.StyleWordBank styleWordBank, ThreadLocalRandom random) {
        // 优先使用风格名词
        List<String> styleNouns = styleWordBank.getNouns();
        if (!styleNouns.isEmpty()) {
            return styleNouns.get(random.nextInt(styleNouns.size()));
        }

        // 理论上这里不应该发生，因为每个风格都应该有名词
        throw new UsernameGeneratorException("风格词库中缺少名词: " + styleWordBank.getName());
    }

    /**
     * 根据时间获取时间段标识
     */
    private String getTimeKey(LocalDateTime dateTime) {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }

        DayOfWeek dow = dateTime.getDayOfWeek();
        // 周末判断
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return "weekend";
        }

        LocalTime time = dateTime.toLocalTime();
        // 清晨：5:00 - 9:00
        if (time.isAfter(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(9, 0))) {
            return "morning";
        }
        // 正午：11:00 - 13:00
        if (time.isAfter(LocalTime.of(11, 0)) && time.isBefore(LocalTime.of(13, 0))) {
            return "noon";
        }
        // 夜晚：22:00 - 4:00
        if (time.isAfter(LocalTime.of(22, 0)) || time.isBefore(LocalTime.of(4, 0))) {
            return "night";
        }

        // 其他时段视为"normal"
        return "normal";
    }
}

