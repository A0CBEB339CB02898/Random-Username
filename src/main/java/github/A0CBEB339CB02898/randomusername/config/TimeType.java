package github.A0CBEB339CB02898.randomusername.config;

/**
 * 时间类型定义
 * 用于基于时间段的用户名生成
 */
public enum TimeType {
    /** 早晨时段 (06:00-10:00) */
    MORNING("morning"),
    /** 中午时段 (11:00-14:00) */
    NOON("noon"),
    /** 夜晚时段 (18:00-23:00) */
    NIGHT("night"),
    /** 周末时段 */
    WEEKEND("weekend"),
    /** 普通/默认时段 */
    NORMAL("normal");

    private final String key;

    TimeType(String key) {
        this.key = key;
    }

    /**
     * 获取时间类型对应的键名，用于词库匹配
     * @return 时间类型键名
     */
    public String getKey() {
        return key;
    }
}

