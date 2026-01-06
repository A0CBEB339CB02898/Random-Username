package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.config.Language;
import github.A0CBEB339CB02898.randomusername.model.WordBank;

/**
 * 自动检测格式的词库加载器
 * 根据文件扩展名自动选择JSON或TXT加载器
 */
public class AutoWordLoader extends AbstractWordLoader {

    private final JsonWordLoader jsonLoader = new JsonWordLoader();
    private final UnifiedWordLoader txtLoader = new UnifiedWordLoader();

    @Override
    public WordBank load(String path, Language language) throws Exception {
        if (isJsonPath(path)) {
            return jsonLoader.load(path, language);
        } else {
            return txtLoader.load(path, language);
        }
    }

    @Override
    public long getTimestamp(String path) {
        if (isJsonPath(path)) {
            return jsonLoader.getTimestamp(path);
        } else {
            return txtLoader.getTimestamp(path);
        }
    }

    /**
     * 判断是否为JSON路径
     */
    private boolean isJsonPath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        // 如果路径明确包含.json，使用JSON加载器
        if (path.contains(".json")) {
            return true;
        }

        // 如果路径包含.txt，使用TXT加载器
        if (path.contains(".txt")) {
            return false;
        }

        // 默认情况（目录路径），优先使用JSON格式
        return true;
    }
}

