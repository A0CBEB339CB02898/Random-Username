package github.A0CBEB339CB02898.randomusername.config;

/**
 * 用户名风格定义
 */
public enum Style {
    /** 🌍 探索者维度（强调“路”与“远方”） */
    EXPLORER("explorer"),
    /** 🔥 情感与态度维度（强调“敢”与“生命力”） */
    ATTITUDE("attitude"),
    /** 🗡️ 江湖/武侠维度（强调“路”与“侠义”） */
    JIANGHU("jianghu"),
    /** 🎨 意象与浪漫维度（去除“路”字的直白，保留意境） */
    ROMANTIC("romantic");

    private final String key;

    Style(String key) {
        this.key = key;
    }

    /**
     * 获取风格对应的键名，用于词库匹配
     * @return 风格键名
     */
    public String getKey() {
        return key;
    }
}
