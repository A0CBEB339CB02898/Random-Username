package github.A0CBEB339CB02898.randomusername.strategy;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 简化的随机策略：支持前缀、形容词+名词、单个名词等基础生成方式
 *
 * 通过 useAdjective 和 usePrefix 标志来控制生成方式：
 * - usePrefix=true, useAdjective=false: PREFIX_RANDOM (只有前缀+随机)
 * - usePrefix=false, useAdjective=true: ADJ_NOUN_RANDOM (形容词+名词+随机)
 * - usePrefix=false, useAdjective=false: NOUN_RANDOM (名词+随机)
 */
public class SimpleRandomStrategy extends UsernameStrategy {

    private final int mode;  // 0=PREFIX_ONLY, 1=ADJ_NOUN, 2=NOUN_ONLY

    public SimpleRandomStrategy(int mode) {
        this.mode = mode;
    }

    @Override
    public String generate(WordBank wordBank, GeneratorConfig config) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (mode == 0) {
            // PREFIX_RANDOM: prefix + random string
            String prefix = config.getPrefix();
            if (prefix == null || prefix.isEmpty()) {
                List<String> prefixes = wordBank.getPrefixes();
                if (prefixes.isEmpty()) {
                    throw new UsernameGeneratorException("配置中未指定前缀，词库中也未找到前缀");
                }
                prefix = prefixes.get(random.nextInt(prefixes.size()));
            }
            String randomStr = generateRandomString(config.getRandomLength(), config.isUseNumbers(), config.isUseLetters());
            return prefix + randomStr;

        } else if (mode == 1) {
            // ADJ_NOUN_RANDOM: adjective + noun + random string
            StringBuilder sb = new StringBuilder();
            List<String> adjs = wordBank.getAdjectives();
            if (!adjs.isEmpty()) {
                sb.append(adjs.get(random.nextInt(adjs.size())));
            }

            List<String> nouns = wordBank.getNouns();
            if (nouns.isEmpty()) {
                throw new UsernameGeneratorException("词库中缺少名词");
            }
            sb.append(nouns.get(random.nextInt(nouns.size())));

            String randomStr = generateRandomString(config.getRandomLength(), config.isUseNumbers(), config.isUseLetters());
            if (!randomStr.isEmpty()) {
                sb.append("_").append(randomStr);
            }
            return sb.toString();

        } else {
            // NOUN_RANDOM: noun + random string
            List<String> nouns = wordBank.getNouns();
            if (nouns.isEmpty()) {
                throw new UsernameGeneratorException("词库中缺少名词");
            }
            String noun = nouns.get(random.nextInt(nouns.size()));
            String randomStr = generateRandomString(config.getRandomLength(), config.isUseNumbers(), config.isUseLetters());
            return noun + (randomStr.isEmpty() ? "" : "_" + randomStr);
        }
    }
}
