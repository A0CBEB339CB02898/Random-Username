package io.github.A0CBEB339CB02898.randomusername.strategy;

import io.github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import io.github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import io.github.A0CBEB339CB02898.randomusername.config.Style;
import io.github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 模板策略：支持基于时间和风格的用户名生成
 */
public class TemplateBasedStrategy extends UsernameStrategy {

    private final boolean useTimeTemplate;

    public TemplateBasedStrategy(boolean useTimeTemplate) {
        this.useTimeTemplate = useTimeTemplate;
    }

    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        List<String> templates;

        if (useTimeTemplate) {
            LocalDateTime dt = config.getRegistrationTime();
            if (dt == null) dt = LocalDateTime.now();
            String timeKey = getTimeKey(dt);

            Map<String, List<String>> timeTemplates = wordBank.getTimeTemplates();
            templates = timeTemplates.get(timeKey);
            if (templates == null || templates.isEmpty()) {
                throw new UsernameGeneratorException("词库中未找到时间key为 " + timeKey + " 的模板");
            }
        } else {
            Style style = config.getStyle();
            if (style == null) style = Style.EXPLORER;

            Map<String, List<String>> styles = wordBank.getStyles();
            templates = styles.get(style.getKey());
            if (templates == null || templates.isEmpty()) {
                throw new UsernameGeneratorException("词库中未找到风格为 " + style.name() + " 的模板");
            }
        }

        String template = templates.get(ThreadLocalRandom.current().nextInt(templates.size()));
        return processTemplate(template, config);
    }

    private String getTimeKey(LocalDateTime dt) {
        DayOfWeek dow = dt.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return "weekend";
        }
        LocalTime time = dt.toLocalTime();
        if (time.isAfter(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(9, 0))) {
            return "morning";
        }
        if (time.isAfter(LocalTime.of(11, 0)) && time.isBefore(LocalTime.of(13, 0))) {
            return "noon";
        }
        if (time.isAfter(LocalTime.of(22, 0)) || time.isBefore(LocalTime.of(4, 0))) {
            return "night";
        }
        return "normal";
    }
}

