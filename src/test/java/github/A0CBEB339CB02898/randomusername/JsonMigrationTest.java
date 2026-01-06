package github.A0CBEB339CB02898.randomusername;

import github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.config.Language;
import org.junit.Test;

/**
 * JSON词库测试
 */
public class JsonMigrationTest {

    @Test
    public void testJsonWordBank() {
        UsernameGenerator generator = new UsernameGenerator();

        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .build();

        String username = generator.generate(config);
        System.out.println("Generated username: " + username);

        assert username != null && !username.isEmpty();
    }
}

