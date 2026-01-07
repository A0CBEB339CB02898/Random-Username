package github.A0CBEB339CB02898.randomusername;

import io.github.A0CBEB339CB02898.randomusername.UsernameGenerator;
import io.github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import io.github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import io.github.A0CBEB339CB02898.randomusername.config.Language;
import io.github.A0CBEB339CB02898.randomusername.config.Style;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

/**
 * 用户名生成器测试
 * 新版本仅支持两种格式：ADJ_NOUN_RANDOM 和 NOUN_RANDOM
 */
public class UsernameGeneratorTest {

    @Test
    public void testGenerateAdjNounRandomDefault() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM (DEFAULT, ZH): " + username);
        assertNotNull(username);
        assertFalse(username.isEmpty());
        // 应该包含下划线和随机后缀
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateAdjNounRandomExplorer() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.EXPLORER)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM (EXPLORER, ZH): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateAdjNounRandomAttitude() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.ATTITUDE)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM (ATTITUDE, ZH): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateAdjNounRandomJianghu() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.JIANGHU)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM (JIANGHU, ZH): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateAdjNounRandomRomantic() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.ROMANTIC)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM (ROMANTIC, ZH): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateNounRandomDefault() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)
                .language(Language.ZH)
                .build();

        String username = generator.generate(config);
        System.out.println("NOUN_RANDOM (DEFAULT, ZH): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateNounRandomEnglish() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)
                .language(Language.EN)
                .style(Style.EXPLORER)
                .build();

        String username = generator.generate(config);
        System.out.println("NOUN_RANDOM (EXPLORER, EN): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateWithTimeBasedAdjectivesMorning() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 7:00 AM is morning
        LocalDateTime morningTime = LocalDateTime.of(2023, 10, 27, 7, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.EXPLORER)
                .enableTimeBasedAdjective(true)
                .registrationTime(morningTime)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM with TimeBasedAdjective (Morning, EXPLORER): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateWithTimeBasedAdjectivesNoon() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 12:00 PM is noon
        LocalDateTime noonTime = LocalDateTime.of(2023, 10, 27, 12, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.JIANGHU)
                .enableTimeBasedAdjective(true)
                .registrationTime(noonTime)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM with TimeBasedAdjective (Noon, JIANGHU): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateWithTimeBasedAdjectivesNight() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 23:00 (11 PM) is night
        LocalDateTime nightTime = LocalDateTime.of(2023, 10, 27, 23, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.ROMANTIC)
                .enableTimeBasedAdjective(true)
                .registrationTime(nightTime)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM with TimeBasedAdjective (Night, ROMANTIC): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateWithTimeBasedAdjectivesWeekend() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // Saturday
        LocalDateTime weekendTime = LocalDateTime.of(2023, 10, 28, 14, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.ATTITUDE)
                .enableTimeBasedAdjective(true)
                .registrationTime(weekendTime)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM with TimeBasedAdjective (Weekend, ATTITUDE): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testGenerateMultipleUsernames() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .build();

        // Generate 10 usernames and verify they are all different (with high probability)
        java.util.Set<String> usernames = new java.util.HashSet<>();
        for (int i = 0; i < 10; i++) {
            String username = generator.generate(config);
            usernames.add(username);
            System.out.println("Generated username " + (i+1) + ": " + username);
        }

        // With 10 generations, we should have at least 8 different ones (allowing for small collision probability)
        assertTrue("Should generate mostly different usernames", usernames.size() >= 8);
    }

    @Test
    public void testGenerateWithCustomSuffixLength() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)
                .language(Language.ZH)
                .suffixLength(8)
                .build();

        String username = generator.generate(config);
        System.out.println("NOUN_RANDOM with suffix length 8: " + username);
        assertNotNull(username);
        // Format: noun_xxxxxxxx (8 random chars)
        assertTrue("Username should contain underscore", username.contains("_"));
        String[] parts = username.split("_");
        assertEquals("Should have exactly 2 parts separated by underscore", 2, parts.length);
        assertEquals("Suffix should be 8 characters", 8, parts[1].length());
    }

    @Test
    public void testGenerateEnglishUsernames() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.EN)
                .style(Style.EXPLORER)
                .build();

        String username = generator.generate(config);
        System.out.println("ADJ_NOUN_RANDOM (EXPLORER, EN): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }
}

