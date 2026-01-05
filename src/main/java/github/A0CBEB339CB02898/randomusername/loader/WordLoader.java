package github.A0CBEB339CB02898.randomusername.loader;

import github.A0CBEB339CB02898.randomusername.model.WordBank;
import github.A0CBEB339CB02898.randomusername.config.Language;

/**
 * 词库加载接口
 */
public interface WordLoader {
    /**
     * 加载词库
     * @param path 词库路径（可能是文件路径、classpath 路径或 URL）
     * @param language 语言
     * @return 加载后的 WordBank 实例
     * @throws Exception 如果加载过程中发生错误
     */
    WordBank load(String path, Language language) throws Exception;

    /**
     * 获取词库的最后修改时间戳（用于缓存失效检查）
     * @param path 词库路径
     * @return 时间戳，如果不支持或获取失败返回 0 或 -1
     */
    default long getTimestamp(String path) {
        return 0;
    }
}
