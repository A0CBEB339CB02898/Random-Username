import io.github.A0CBEB339CB02898.randomusername.UsernameGenerator;
import io.github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import io.github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import io.github.A0CBEB339CB02898.randomusername.config.Language;
import io.github.A0CBEB339CB02898.randomusername.config.Style;
import java.time.LocalDateTime;

public class TestGeneratedUsernames {
    public static void main(String[] args) {
        UsernameGenerator generator = new UsernameGenerator();

        System.out.println("========== Testing New Generation Formats ==========\n");

        // Test ADJ_NOUN_RANDOM with different styles
        System.out.println("1. ADJ_NOUN_RANDOM (adjective+noun+suffix) - Chinese:");
        testStyle(generator, GenerationMode.ADJ_NOUN_RANDOM, Language.ZH, Style.DEFAULT, false);
        testStyle(generator, GenerationMode.ADJ_NOUN_RANDOM, Language.ZH, Style.EXPLORER, false);
        testStyle(generator, GenerationMode.ADJ_NOUN_RANDOM, Language.ZH, Style.ATTITUDE, false);
        testStyle(generator, GenerationMode.ADJ_NOUN_RANDOM, Language.ZH, Style.JIANGHU, false);
        testStyle(generator, GenerationMode.ADJ_NOUN_RANDOM, Language.ZH, Style.ROMANTIC, false);

        // Test NOUN_RANDOM
        System.out.println("\n2. NOUN_RANDOM (noun+suffix) - Chinese:");
        testStyle(generator, GenerationMode.NOUN_RANDOM, Language.ZH, Style.DEFAULT, false);
        testStyle(generator, GenerationMode.NOUN_RANDOM, Language.ZH, Style.EXPLORER, false);

        // Test English
        System.out.println("\n3. ADJ_NOUN_RANDOM - English:");
        testStyle(generator, GenerationMode.ADJ_NOUN_RANDOM, Language.EN, Style.EXPLORER, false);

        // Test time-based adjectives
        System.out.println("\n4. ADJ_NOUN_RANDOM with time-based adjectives - Chinese:");
        System.out.println("   Morning (EXPLORER):");
        testWithTime(generator, Language.ZH, Style.EXPLORER, LocalDateTime.of(2023, 10, 27, 7, 0));
        System.out.println("   Noon (JIANGHU):");
        testWithTime(generator, Language.ZH, Style.JIANGHU, LocalDateTime.of(2023, 10, 27, 12, 0));
        System.out.println("   Night (ROMANTIC):");
        testWithTime(generator, Language.ZH, Style.ROMANTIC, LocalDateTime.of(2023, 10, 27, 23, 0));
        System.out.println("   Weekend (ATTITUDE):");
        testWithTime(generator, Language.ZH, Style.ATTITUDE, LocalDateTime.of(2023, 10, 28, 14, 0)); // Saturday

        System.out.println("\n========== Testing Complete ==========");
    }

    private static void testStyle(UsernameGenerator generator, GenerationMode mode, Language lang, Style style, boolean enableTime) {
        for (int i = 0; i < 3; i++) {
            String username = generator.generate(
                GeneratorConfig.builder()
                    .mode(mode)
                    .language(lang)
                    .style(style)
                    .enableTimeBasedAdjective(enableTime)
                    .build()
            );
            System.out.println("   " + style.name() + ": " + username);
        }
    }

    private static void testWithTime(UsernameGenerator generator, Language lang, Style style, LocalDateTime time) {
        for (int i = 0; i < 2; i++) {
            String username = generator.generate(
                GeneratorConfig.builder()
                    .mode(GenerationMode.ADJ_NOUN_RANDOM)
                    .language(lang)
                    .style(style)
                    .enableTimeBasedAdjective(true)
                    .registrationTime(time)
                    .build()
            );
            System.out.println("      " + username);
        }
    }
}

