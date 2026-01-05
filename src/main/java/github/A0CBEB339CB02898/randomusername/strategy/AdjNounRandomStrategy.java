package github.A0CBEB339CB02898.randomusername.strategy;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机形容词 + 名词 + 随机字符串生成策略
 */
public class AdjNounRandomStrategy extends UsernameStrategy {
    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        List<String> adjs = wordBank.getAdjectives();
        List<String> nouns = wordBank.getNouns();
        
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String adj = adjs.isEmpty() ? "" : adjs.get(random.nextInt(adjs.size()));
        
        if (nouns.isEmpty()) {
            throw new UsernameGeneratorException("词库中未找到名词，无法执行AdjNounRandomStrategy");
        }
        String noun = nouns.get(random.nextInt(nouns.size()));
        
        String randomStr = generateRandomString(config.getRandomLength(), config.isUseNumbers(), config.isUseLetters());
        
        return adj + noun + (randomStr.isEmpty() ? "" : "_" + randomStr);
    }
}
