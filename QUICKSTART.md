# 快速入门指南 - 随机用户名生成器 v2.0.0

## 基本使用

### 1. 形容词+名词模式（ADJ_NOUN_RANDOM）

最常用的模式，生成格式：`形容词+名词_xxxx`

```java
UsernameGenerator generator = new UsernameGenerator();

// 中文，默认风格
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .build()
);
// 示例输出：勇敢的冒险者_aBc2

// 中文，指定风格
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.EXPLORER)
        .build()
);
// 示例输出：热血的探险者_dxiR

// 英文
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.EN)
        .style(Style.ROMANTIC)
        .build()
);
// 示例输出：TenderDreamer_8WVk
```

### 2. 纯名词模式（NOUN_RANDOM）

生成格式：`名词_xxxx`

```java
UsernameGenerator generator = new UsernameGenerator();

String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.JIANGHU)
        .build()
);
// 示例输出：侠客_xY9k
```

## 高级特性

### 1. 时段形容词识别

根据注册时间自动选择适合的形容词。系统会根据时间段选择不同的形容词：
- 早晨（5:00-9:00）：朝气蓬勃、意气风发等
- 正午（11:00-13:00）：火热、充满活力等
- 夜晚（22:00-4:00）：沉静、深邃等
- 周末：悠闲、放松等

```java
UsernameGenerator generator = new UsernameGenerator();

// 早晨，探索者风格
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.EXPLORER)
        .enableTimeBasedAdjective(true)
        .registrationTime(LocalDateTime.of(2023, 10, 27, 7, 0))
        .build()
);
// 示例输出：朝气十足的疆界突破者_geP3
// （使用了 EXPLORER 风格的"早晨"时段形容词）

// 正午，江湖风格
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.JIANGHU)
        .enableTimeBasedAdjective(true)
        .registrationTime(LocalDateTime.of(2023, 10, 27, 12, 0))
        .build()
);
// 示例输出：日午铁血的浪子_FlXo
```

### 2. 自定义后缀长度

默认后缀长度为 4，可自定义：

```java
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.NOUN_RANDOM)
        .language(Language.ZH)
        .suffixLength(8)  // 后缀长度为 8
        .build()
);
// 示例输出：侠客_fRCru3zF
```

### 3. 配置随机字符类型

默认同时包含字母和数字，可自定义：

```java
// 仅包含字母
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .useNumbers(false)  // 不包含数字
        .useLetters(true)   // 包含字母
        .build()
);
// 示例输出：勇敢的冒险者_aBcD

// 仅包含数字
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .useNumbers(true)   // 包含数字
        .useLetters(false)  // 不包含字母
        .build()
);
// 示例输出：勇敢的冒险者_1234
```

## 风格说明

### 🎨 DEFAULT（默认风格）
通用风格，适合所有场景
- 形容词：快乐、聪慧、温柔、勇敢、自由等
- 名词：少年、侠客、梦想家、冒险者等

### 🌍 EXPLORER（探索者）
适合冒险、探索、开拓精神的用户
- 形容词：好奇、大胆、冒险、执着等
- 名词：冒险家、探险者、旅行者、疆界突破者等

### 🔥 ATTITUDE（态度）
适合个性鲜明、态度坚定的用户
- 形容词：狂放、傲骨、桀骜、洒脱等
- 名词：反叛者、个性人、独行者、个性信徒等

### 🗡️ JIANGHU（江湖）
适合崇尚侠义、行走江湖的用户
- 形容词：侠义、江湖、恩仇、快意等
- 名词：侠客、大侠、剑客、浪子等

### 🎭 ROMANTIC（浪漫）
适合温情、诗意、浪漫的用户
- 形容词：温情、梦幻、诗意、柔情等
- 名词：梦想家、诗人、心灵歌者、爱的传道者等

## 完整配置示例

```java
GeneratorConfig config = GeneratorConfig.builder()
    // 生成模式（必选）
    .mode(GenerationMode.ADJ_NOUN_RANDOM)
    
    // 语言（可选，默认为中文）
    .language(Language.ZH)
    
    // 风格（可选，默认为 DEFAULT）
    .style(Style.ROMANTIC)
    
    // 随机后缀长度（可选，默认为 4）
    .suffixLength(6)
    
    // 启用时段形容词（可选，默认为 false）
    .enableTimeBasedAdjective(true)
    
    // 注册时间（可选，用于时段识别，默认为当前时间）
    .registrationTime(LocalDateTime.now())
    
    // 后缀是否包含数字（可选，默认为 true）
    .useNumbers(true)
    
    // 后缀是否包含字母（可选，默认为 true）
    .useLetters(true)
    
    // 自定义词库路径（可选）
    .wordBankPath("path/to/custom/wordbank.json")
    
    // 是否使用缓存（可选，默认为 true）
    .useCache(true)
    
    .build();

String username = generator.generate(config);
```

## 生成样本集合

### 中文样本

**DEFAULT 风格**
- 快乐的少年_8pQw
- 温暖的战士_ceSn
- 聪慧的诗人_ecdZ

**EXPLORER 风格**
- 热血的探险者_dxiR
- 执着的远航者_9VnZ
- 大胆的疆界突破者_KvSA

**ATTITUDE 风格**
- 狂放的自我宣言者_MgKG
- 桀骜的个性高手_BpW5
- 自信的反叛者_4Jx2

**JIANGHU 风格**
- 恩仇的剑客_pSmR
- 烈日中驰骋的江湖人_VALA
- 豪迈的浪子_H1cK

**ROMANTIC 风格**
- 诗意的爱的传道者_TO5d
- 星空下遥望的感情上的守护者_eMyk
- 温情的梦想家_3FsP

### 英文样本

**EXPLORER 风格**
- ExpansiveDreamer_8WVk
- BoldAdventurer_5LpW
- DaringVoyager_ZXjF

**ROMANTIC 风格**
- TenderDreamer_8WVk
- PoetryKeeper_3RsN

**ATTITUDE 风格**
- ConfidentIndividual_3RsN
- WildRebel_8JqM

## 常见用例

### 用例 1：为新注册用户生成默认用户名

```java
UsernameGenerator generator = new UsernameGenerator();
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.DEFAULT)
        .build()
);
```

### 用例 2：根据用户个性特征生成用户名

```java
// 冒险爱好者
String adventurerUsername = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.EXPLORER)
        .build()
);

// 个性用户
String individualUsername = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.ATTITUDE)
        .build()
);
```

### 用例 3：营造时段氛围的用户名

```java
// 早晨注册，生成早晨风格的用户名
String morningUsername = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .enableTimeBasedAdjective(true)
        .registrationTime(LocalDateTime.now())
        .build()
);
```

### 用例 4：保证用户名多样性

```java
// 生成 10 个不同的用户名供用户选择
List<String> usernames = new ArrayList<>();
for (int i = 0; i < 10; i++) {
    usernames.add(generator.generate(
        GeneratorConfig.builder()
            .mode(GenerationMode.ADJ_NOUN_RANDOM)
            .language(Language.ZH)
            .style(Style.ROMANTIC)
            .build()
    ));
}
```

## 注意事项

1. **不兼容 v1.x**：该版本为 2.0.0，与 1.x 版本不兼容。不支持以下 1.x 的 API：
   - `GenerationMode.PREFIX_RANDOM`
   - `GenerationMode.TIME_BASED`
   - `GenerationMode.STYLE_BASED`
   - `config.getPrefix()`
   - `config.getRandomLength()`

2. **使用 suffixLength 替代 randomLength**：新版本用 `suffixLength` 替代了旧版的 `randomLength`

3. **默认启用词库缓存**：为了性能，词库默认缓存。可通过 `useCache(false)` 禁用

4. **时段形容词需显式启用**：时段形容词识别默认关闭，使用时需设置 `enableTimeBasedAdjective(true)`

## 问题反馈

如有问题或建议，欢迎提交 Issue 或 Pull Request。

