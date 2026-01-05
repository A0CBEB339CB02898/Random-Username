package github.A0CBEB339CB02898.randomusername;

import github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import github.A0CBEB339CB02898.randomusername.config.Language;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class UsernameGeneratorTest {

    @Test
    public void testGeneratePrefixRandom() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.PREFIX_RANDOM)
                .prefix("test_")
                .randomLength(6)
                .build();
        
        String username = generator.generate(config);
        System.out.println("PrefixRandom: " + username);
        assertTrue(username.startsWith("test_"));
        assertEquals(11, username.length()); // "test_" (5) + 6 = 11
    }

    @Test
    public void testGenerateAdjNounRandom() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .build();
        
        String username = generator.generate(config);
        System.out.println("AdjNounRandom (ZH): " + username);
        assertNotNull(username);
        assertFalse(username.isEmpty());
    }

    @Test
    public void testGenerateTimeBased() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.TIME_BASED)
                .build();
        
        String username = generator.generate(config);
        System.out.println("TimeBased: " + username);
        assertNotNull(username);
    }

    @Test
    public void testGenerateTimeBasedNoon() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        // 12:00 PM is noon
        LocalDateTime noonTime = LocalDateTime.of(2023, 10, 27, 12, 0);
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.TIME_BASED)
                .registrationTime(noonTime)
                .language(Language.ZH)
                .build();
        
        String username = generator.generate(config);
        System.out.println("TimeBased Noon: " + username);
        // "正午的阳光", "午后闲暇", "忙里偷闲的小员"
        assertTrue(username.contains("正午") || username.contains("午后") || username.contains("忙里偷闲"));
    }

    @Test
    public void testGeneratePrefixFromWordBank() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.PREFIX_RANDOM)
                .language(Language.ZH)
                .randomLength(4)
                .build();
        
        String username = generator.generate(config);
        System.out.println("PrefixRandom from WordBank: " + username);
        // [PREFIXES] user_, vip_, super_, cool_, hot_, lucky_ ...
        boolean match = username.startsWith("user_") || username.startsWith("vip_") || 
                        username.startsWith("super_") || username.startsWith("cool_") || 
                        username.startsWith("hot_") || username.startsWith("lucky_") || 
                        username.startsWith("the_");
        assertTrue("Username should start with one of the prefixes from word bank", match);
    }

    @Test
    public void testGenerateStyleBased() throws Exception {
        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.STYLE_BASED)
                .language(Language.ZH)
                .build();
        
        String username = generator.generate(config);
        System.out.println("StyleBased: " + username);
        assertNotNull(username);
    }
}
