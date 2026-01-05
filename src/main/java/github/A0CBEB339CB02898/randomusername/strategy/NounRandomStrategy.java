package github.A0CBEB339CB02898.randomusername.strategy;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 名词 + 随机字符串生成策略
 */
public class NounRandomStrategy extends UsernameStrategy {
    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        List<String> nouns = wordBank.getNouns();
        if (nouns.isEmpty()) {
            throw new UsernameGeneratorException("词库中未找到名词，无法执行NounRandomStrategy");
        }
        String noun = nouns.get(ThreadLocalRandom.current().nextInt(nouns.size()));
        
        String randomStr = generateRandomString(config.getRandomLength(), config.isUseNumbers(), config.isUseLetters());
        
        return noun + (randomStr.isEmpty() ? "" : "_" + randomStr);
    }
}
