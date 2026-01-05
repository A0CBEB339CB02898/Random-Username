package github.A0CBEB339CB02898.randomusername.strategy;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.config.Style;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于风格的用户名生成策略
 */
public class StyleBasedStrategy extends UsernameStrategy {
    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        Style style = config.getStyle();
        if (style == null) style = Style.EXPLORER;

        List<String> templates = wordBank.getStyles().get(style.getKey());
        
        if (templates == null || templates.isEmpty()) {
            throw new UsernameGeneratorException("词库中未找到风格为 " + style.name() + " 的模板");
        }
        
        String template = templates.get(ThreadLocalRandom.current().nextInt(templates.size()));
        return processTemplate(template, config);
    }
}
