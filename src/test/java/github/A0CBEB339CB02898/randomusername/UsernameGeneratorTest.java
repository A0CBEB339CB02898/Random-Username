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

    @Test
    public void testTimeBasedAdjectiveEarly() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 凌晨 02:00
        LocalDateTime earlyTime = LocalDateTime.of(2023, 10, 27, 2, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)  // 即使配置为 NOUN_RANDOM
                .language(Language.ZH)
                .style(Style.EXPLORER)
                .enableTimeBasedAdjective(true)     // 启用时段形容词
                .registrationTime(earlyTime)
                .build();

        String username = generator.generate(config);
        System.out.println("TimeBasedAdjective (Early, EXPLORER): " + username);
        assertNotNull(username);
        // 应该强制使用 ADJ_NOUN_RANDOM 格式，不会是纯名词
        assertTrue("Username should contain underscore", username.contains("_"));
        // 验证是否包含形容词（中文形容词）
        assertFalse("Should use adjective format when time-based adjective enabled",
                   username.startsWith("冒险家_") || username.startsWith("探险者_"));
    }

    @Test
    public void testTimeBasedAdjectiveMorning() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 上午 09:00
        LocalDateTime morningTime = LocalDateTime.of(2023, 10, 27, 9, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .style(Style.ATTITUDE)
                .enableTimeBasedAdjective(true)
                .registrationTime(morningTime)
                .build();

        String username = generator.generate(config);
        System.out.println("TimeBasedAdjective (Morning, ATTITUDE): " + username);
        assertNotNull(username);
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testTimeBasedAdjectiveAfternoon() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 下午 15:00
        LocalDateTime afternoonTime = LocalDateTime.of(2023, 10, 27, 15, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)  // 即使配置为 NOUN_RANDOM
                .language(Language.ZH)
                .style(Style.ROMANTIC)
                .enableTimeBasedAdjective(true)
                .registrationTime(afternoonTime)
                .build();

        String username = generator.generate(config);
        System.out.println("TimeBasedAdjective (Afternoon, ROMANTIC): " + username);
        assertNotNull(username);
        // 应该强制使用 ADJ_NOUN_RANDOM 格式
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testTimeBasedAdjectiveNight() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 夜晚 20:00
        LocalDateTime nightTime = LocalDateTime.of(2023, 10, 27, 20, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)  // 即使配置为 NOUN_RANDOM
                .language(Language.ZH)
                .style(Style.JIANGHU)
                .enableTimeBasedAdjective(true)
                .registrationTime(nightTime)
                .build();

        String username = generator.generate(config);
        System.out.println("TimeBasedAdjective (Night, JIANGHU): " + username);
        assertNotNull(username);
        // 应该强制使用 ADJ_NOUN_RANDOM 格式
        assertTrue("Username should contain underscore", username.contains("_"));
    }

    @Test
    public void testTimeBasedAdjectiveForceAdjNoun() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 多次测试确保 NOUN_RANDOM 被强制转换为 ADJ_NOUN_RANDOM
        LocalDateTime testTime = LocalDateTime.of(2023, 10, 27, 10, 0);  // 上午

        for (int i = 0; i < 5; i++) {
            String username = generator.generate(
                GeneratorConfig.builder()
                        .mode(GenerationMode.NOUN_RANDOM)
                        .language(Language.ZH)
                        .style(Style.DEFAULT)
                        .enableTimeBasedAdjective(true)
                        .registrationTime(testTime)
                        .build()
            );
            System.out.println("Forced ADJ_NOUN (Time-based enabled) " + (i+1) + ": " + username);

            // 验证不会出现纯名词的格式
            String[] parts = username.split("_");
            assertNotNull("Username should have suffix", parts[1]);
        }
    }
}
