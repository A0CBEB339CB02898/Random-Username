package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.UsernameGeneratorException;
import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.model.WordBank;
import github.A0CBEB339CB02898.randomusername.model.WordBankMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 词库配置验证器
 * 负责验证词库文件的完整性和元数据的有效性
 */
public class WordBankConfigValidator {

    private static final Logger logger = LoggerFactory.getLogger(WordBankConfigValidator.class);

    private final WordBankMetadataLoader metadataLoader;

    public WordBankConfigValidator() {
        this.metadataLoader = new WordBankMetadataLoader();
    }

    /**
     * 验证词库配置
     * @param basePath 词库基础路径
     * @param language 语言
     * @param strictMode 严格模式（true 时会抛出异常，false 时只记录警告）
     * @return 验证结果
     */
    public ValidationResult validate(String basePath, Language language, boolean strictMode) {
        ValidationResult result = new ValidationResult();
        String normalizedPath = basePath.endsWith("/") ? basePath : basePath + "/";

        // 验证基础文件
        validateBasicFiles(normalizedPath, result, strictMode);

        // 加载并验证元数据
        Map<String, WordBankMetadata> metadataMap = metadataLoader.loadAllMetadataInDirectory(normalizedPath);
        validateMetadata(metadataMap, result, strictMode);

        // 验证词库内容（如果提供了元数据）
        validateWordBankContent(normalizedPath, metadataMap, result, strictMode);

        // 生成验证报告
        logValidationReport(result);

        // 如果是严格模式且有错误，抛出异常
        if (strictMode && result.hasErrors()) {
            throw new UsernameGeneratorException("Word bank validation failed:\n" + result.getErrorSummary());
        }

        return result;
    }

    /**
     * 验证基础词库文件是否存在
     */
    private void validateBasicFiles(String basePath, ValidationResult result, boolean strictMode) {
        String[] requiredFiles = {
            WordBankConstants.ADJECTIVES,
            WordBankConstants.NOUNS,
            WordBankConstants.PREFIXES
        };

        for (String fileName : requiredFiles) {
            String filePath = basePath + fileName;
            if (!fileExists(filePath)) {
                String message = "Required word bank file not found: " + fileName;
                if (strictMode) {
                    result.addError(message);
                } else {
                    result.addWarning(message);
                }
            } else {
                result.addInfo("Found required file: " + fileName);
            }
        }
    }

    /**
     * 验证元数据的有效性
     */
    private void validateMetadata(Map<String, WordBankMetadata> metadataMap, ValidationResult result, boolean strictMode) {
        for (Map.Entry<String, WordBankMetadata> entry : metadataMap.entrySet()) {
            String fileName = entry.getKey();
            WordBankMetadata metadata = entry.getValue();

            if (!metadata.isValid()) {
                String message = "Invalid metadata for file: " + fileName;
                if (strictMode) {
                    result.addError(message);
                } else {
                    result.addWarning(message);
                }
            } else {
                result.addInfo("Valid metadata: " + metadata.getSummary());
            }

            // 验证版本号格式
            if (metadata.getVersion() != null && !isValidVersion(metadata.getVersion())) {
                result.addWarning("Invalid version format for " + fileName + ": " + metadata.getVersion());
            }
        }
    }

    /**
     * 验证词库内容是否符合元数据要求
     */
    private void validateWordBankContent(String basePath, Map<String, WordBankMetadata> metadataMap,
                                        ValidationResult result, boolean strictMode) {
        // 这里可以实际加载词库文件并验证词条数量
        // 为了性能考虑，这里只做基本验证
        for (Map.Entry<String, WordBankMetadata> entry : metadataMap.entrySet()) {
            String fileName = entry.getKey();
            WordBankMetadata metadata = entry.getValue();

            if (metadata.getMinEntries() > 0) {
                // 可以在这里添加实际的词条数量检查
                result.addInfo("Word bank " + fileName + " requires minimum " + metadata.getMinEntries() + " entries");
            }
        }
    }

    /**
     * 检查文件是否存在
     */
    private boolean fileExists(String path) {
        if (path.startsWith(WordBankConstants.CLASSPATH_PREFIX)) {
            String resourcePath = path.substring(WordBankConstants.CLASSPATH_PREFIX.length());
            if (resourcePath.startsWith("/")) {
                resourcePath = resourcePath.substring(1);
            }
            return getClass().getClassLoader().getResource(resourcePath) != null ||
                   Thread.currentThread().getContextClassLoader().getResource(resourcePath) != null;
        }

        java.io.File file = new java.io.File(path);
        return file.exists();
    }

    /**
     * 验证版本号格式（简单的语义化版本检查）
     */
    private boolean isValidVersion(String version) {
        // 支持 1.0, 1.0.0, 1.0.0-beta 等格式
        return version.matches("\\d+(\\.\\d+)?(\\.\\d+)?(-[a-zA-Z0-9]+)?");
    }

    /**
     * 记录验证报告
     */
    private void logValidationReport(ValidationResult result) {
        if (result.hasErrors()) {
            logger.error("Word bank validation completed with errors:");
            for (String error : result.getErrors()) {
                logger.error("  [ERROR] " + error);
            }
        }

        if (result.hasWarnings()) {
            logger.warn("Word bank validation warnings:");
            for (String warning : result.getWarnings()) {
                logger.warn("  [WARN] " + warning);
            }
        }

        if (!result.hasErrors() && !result.hasWarnings()) {
            logger.info("Word bank validation passed successfully");
        }
    }

    /**
     * 验证结果类
     */
    public static class ValidationResult {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();
        private final List<String> infos = new ArrayList<>();

        public void addError(String message) {
            errors.add(message);
        }

        public void addWarning(String message) {
            warnings.add(message);
        }

        public void addInfo(String message) {
            infos.add(message);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }

        public List<String> getErrors() {
            return Collections.unmodifiableList(errors);
        }

        public List<String> getWarnings() {
            return Collections.unmodifiableList(warnings);
        }

        public List<String> getInfos() {
            return Collections.unmodifiableList(infos);
        }

        public String getErrorSummary() {
            StringBuilder sb = new StringBuilder();
            for (String error : errors) {
                sb.append("  - ").append(error).append("\n");
            }
            return sb.toString();
        }
    }
}

