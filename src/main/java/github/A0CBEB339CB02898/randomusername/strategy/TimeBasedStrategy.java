package github.A0CBEB339CB02898.randomusername.strategy;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于注册时间的生成策略
 * 根据清晨、深夜、周末等不同时间段返回动态组合的用户名
 */
public class TimeBasedStrategy extends UsernameStrategy {
    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        LocalDateTime dt = config.getRegistrationTime();
        if (dt == null) dt = LocalDateTime.now();

        String timeKey = getTimeKey(dt);
        List<String> templates = wordBank.getTimeTemplates().get(timeKey);
        
        if (templates == null || templates.isEmpty()) {
            throw new UsernameGeneratorException("词库中未找到时间key为 " + timeKey + " 的模板");
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
