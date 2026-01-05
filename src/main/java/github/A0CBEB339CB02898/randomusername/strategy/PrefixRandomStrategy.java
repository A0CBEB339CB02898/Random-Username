package github.A0CBEB339CB02898.randomusername.strategy;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于固定前缀 + 随机字符串生成策略
 */
public class PrefixRandomStrategy extends UsernameStrategy {
    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        String prefix = config.getPrefix();
        
        // 如果配置中没有前缀，则尝试从词库中获取
        if (prefix == null || prefix.isEmpty()) {
            List<String> prefixes = wordBank.getPrefixes();
            if (prefixes != null && !prefixes.isEmpty()) {
                prefix = prefixes.get(ThreadLocalRandom.current().nextInt(prefixes.size()));
            } else {
                throw new UsernameGeneratorException("配置中未指定前缀，词库中也未找到前缀");
            }
        }
        
        String randomStr = generateRandomString(config.getRandomLength(), config.isUseNumbers(), config.isUseLetters());
        return prefix + randomStr;
    }
}
