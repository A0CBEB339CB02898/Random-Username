package github.A0CBEB339CB02898.randomusername;

import github.A0CBEB339CB02898.randomusername.config.GenerationMode;
import github.A0CBEB339CB02898.randomusername.config.GeneratorConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class CacheReloadTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private void writeString(File file, String content) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }

    @Test
    public void testNoCacheReload() throws IOException {
        File dictFile = tempFolder.newFile("words.txt");
        writeString(dictFile, "[NOUNS]\nApple");

        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)
                .wordBankPath(dictFile.getAbsolutePath())
                .useCache(false)
                .build();

        assertEquals("Apple", generator.generate(config));

        // Update file
        writeString(dictFile, "[NOUNS]\nBanana");
        assertEquals("Banana", generator.generate(config));
    }

    @Test
    public void testCacheWithAutoReload() throws IOException, InterruptedException {
        File dictFile = tempFolder.newFile("words_cache.txt");
        writeString(dictFile, "[NOUNS]\nCherry");

        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)
                .wordBankPath(dictFile.getAbsolutePath())
                .useCache(true)
                .build();

        assertEquals("Cherry", generator.generate(config));

        // Wait a bit to ensure timestamp changes (filesystem resolution)
        Thread.sleep(1100);

        // Update file
        writeString(dictFile, "[NOUNS]\nDurian");
        
        // Should reload automatically because timestamp changed
        assertEquals("Durian", generator.generate(config));
    }

    @Test
    public void testManualReload() throws IOException {
        File dictFile = tempFolder.newFile("words_manual.txt");
        writeString(dictFile, "[NOUNS]\nEggplant");

        UsernameGenerator generator = new UsernameGenerator();
        GeneratorConfig config = GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)
                .wordBankPath(dictFile.getAbsolutePath())
                .useCache(true)
                .build();

        assertEquals("Eggplant", generator.generate(config));

        // Update file
        writeString(dictFile, "[NOUNS]\nFig");
        
        // Manually reload
        generator.reload(dictFile.getAbsolutePath());
        assertEquals("Fig", generator.generate(config));
    }
}
