# Random-Username

一个功能强大的随机用户名生成库。支持两种生成格式、5 种风格、中英文双语、时段形容词识别。

## 特性

- **两种简洁格式**：
  - `形容词 + 名词 + 随机后缀` (如：勇敢的冒险者_aBc2)
  - `名词 + 随机后缀` (如：冒险家_xY9k)
  
- **5 种个性风格**：DEFAULT（默认）、EXPLORER（探索者）、ATTITUDE（态度）、JIANGHU（江湖）、ROMANTIC（浪漫）

- **时段形容词识别**：根据注册时间自动选择凌晨、上午、下午、夜晚对应的形容词

- **多语言支持**：完整的中文和英文词库

- **智能回退机制**：多层优先级回退，确保总是生成有效用户名

- **词库缓存**：支持自动热更新，可关闭缓存节省内存

- **灵活配置**：支持自定义词库路径（本地或HTTP URL）

## 安装

### Maven 依赖

```xml
<dependency>
    <groupId>io.github.a0cbeb339cb02898</groupId>
    <artifactId>random-username</artifactId>
    <version>latest</version>
</dependency>
```

## 快速开始

### 基础用法

```java
import io.github.A0CBEB339CB02898.randomusername.UsernameGenerator;
import io.github.A0CBEB339CB02898.randomusername.config.*;

UsernameGenerator generator = new UsernameGenerator();

// 形容词 + 名词模式（默认风格）
String username1 = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .language(Language.ZH)
        .build()
);
System.out.println(username1);  // 输出：勇敢的冒险者_aBc2

// 纯名词模式
String username2 = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.NOUN_RANDOM)
        .language(Language.ZH)
        .style(Style.ROMANTIC)
        .build()
);
System.out.println(username2);  // 输出：诗人_xY9k
```

### 启用时段形容词

```java
// 启用时段形容词后，自动根据时间选择对应形容词
// 注意：此时会强制使用 ADJ_NOUN_RANDOM 格式（即使配置为 NOUN_RANDOM）
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.NOUN_RANDOM)     // 配置为纯名词
        .language(Language.ZH)
        .style(Style.EXPLORER)
        .enableTimeBasedAdjective(true)        // 启用时段形容词
        .registrationTime(LocalDateTime.now()) // 自动识别当前时段
        .build()
);

// 如果当前是上午，可能输出：朝气十足的探险者_aBc2
// （强制转换为 ADJ_NOUN_RANDOM 格式，并使用上午的时段形容词）
```

### 时段划分

当启用时段形容词时，系统会根据当前时间自动选择对应的形容词：

| 时段 | 时间范围 | 示例形容词（EXPLORER风格） |
|------|---------|---------------------------|
| 凌晨 | 00:00-06:00 | 黎明前的、蓄势待发的 |
| 上午 | 06:00-12:00 | 朝气十足的、意气风发的 |
| 下午 | 12:00-18:00 | 热血沸腾的、全力以赴的 |
| 夜晚 | 18:00-24:00 | 深思熟虑的、沉着冷静的 |

### 风格说明

#### 🎨 DEFAULT（默认风格）
通用风格，适合所有场景
```java
.style(Style.DEFAULT)
```
形容词：快乐、聪慧、温柔、勇敢、自由等  
名词：少年、侠客、梦想家、冒险者、旅人等

#### 🌍 EXPLORER（探索者）
适合冒险、探索、开拓精神的场景
```java
.style(Style.EXPLORER)
```
形容词：好奇、大胆、冒险、执着、坚毅等  
名词：冒险家、探险者、旅行者、开拓者等

#### 🔥 ATTITUDE（态度）
适合个性鲜明、态度坚定的用户
```java
.style(Style.ATTITUDE)
```
形容词：狂放、傲骨、桀骜、洒脱、自信等  
名词：反叛者、个性人、独行者、态度大师等

#### 🗡️ JIANGHU（江湖）
适合崇尚侠义、行走江湖的氛围
```java
.style(Style.JIANGHU)
```
形容词：侠义、江湖、恩仇、豪迈、义气等  
名词：侠客、大侠、剑客、浪子、快意恩仇者等

#### 🎭 ROMANTIC（浪漫）
适合温情、诗意、浪漫的用户
```java
.style(Style.ROMANTIC)
```
形容词：温情、梦幻、诗意、柔情、深情等  
名词：梦想家、诗人、心灵歌者、爱的传道者等

## 完整配置说明

```java
GeneratorConfig config = GeneratorConfig.builder()
    // 生成模式（必选）
    .mode(GenerationMode.ADJ_NOUN_RANDOM)
    
    // 语言（可选，默认为ZH中文）
    .language(Language.ZH)
    
    // 风格（可选，默认为DEFAULT）
    .style(Style.EXPLORER)
    
    // 随机后缀长度（可选，默认为4）
    .suffixLength(6)
    
    // 启用时段形容词（可选，默认为false）
    // 启用后会强制使用 ADJ_NOUN_RANDOM 格式
    .enableTimeBasedAdjective(true)
    
    // 注册时间（可选，用于时段识别，默认为当前时间）
    .registrationTime(LocalDateTime.now())
    
    // 后缀是否包含数字（可选，默认为true）
    .useNumbers(true)
    
    // 后缀是否包含字母（可选，默认为true）
    .useLetters(true)
    
    // 自定义词库路径（可选，支持本地路径或HTTP URL）
    .wordBankPath("path/to/wordbank.json")
    
    // 是否使用缓存（可选，默认为true）
    .useCache(true)
    
    .build();

String username = generator.generate(config);
```

## 高级用法

### 生成多个不同的用户名

```java
UsernameGenerator generator = new UsernameGenerator();
List<String> usernames = new ArrayList<>();

for (int i = 0; i < 10; i++) {
    String username = generator.generate(
        GeneratorConfig.builder()
            .mode(GenerationMode.ADJ_NOUN_RANDOM)
            .language(Language.ZH)
            .style(Style.ROMANTIC)
            .build()
    );
    usernames.add(username);
}

usernames.forEach(System.out::println);
```

### 禁用缓存以节省内存

```java
String username = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .useCache(false)  // 每次生成都重新加载词库
        .build()
);
```

### 加载自定义词库

```java
// 本地文件路径
String username1 = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .wordBankPath("C:/path/to/custom-wordbank.json")
        .build()
);

// HTTP URL
String username2 = generator.generate(
    GeneratorConfig.builder()
        .mode(GenerationMode.ADJ_NOUN_RANDOM)
        .wordBankPath("http://example.com/wordbank.json")
        .build()
);
```

## 词库格式

词库采用 JSON 格式，支持中英文和多种风格。基础结构如下：

```json
{
  "language": "zh",
  "version": "2.0.0",
  "basic": {
    "adjectives": ["快乐的", "聪慧的"],
    "nouns": ["少年", "侠客"]
  },
  "styles": {
    "default": {
      "name": "默认风格",
      "adjectives": ["快乐的", "聪慧的"],
      "nouns": ["少年", "侠客"],
      "timeAdjectives": {
        "early": ["清晨的", "朝气蓬勃的"],
        "morning": ["朝气十足的", "神采奕奕的"],
        "afternoon": ["骄阳似火的", "炽热的"],
        "night": ["沉静的", "深邃的"]
      }
    }
  }
}
```

### 关键字段说明

- `basic.adjectives`：全局基础形容词（所有风格的后备）
- `basic.nouns`：全局基础名词（所有风格的后备）
- `styles[].adjectives`：该风格的形容词列表
- `styles[].nouns`：该风格的名词列表
- `styles[].timeAdjectives`：该风格的时段形容词（early、morning、afternoon、night）

## 形容词选择优先级

### 启用时段形容词时

```
1. 风格的时段形容词
   ↓ 如果风格没有该时段的形容词
2. 默认风格的时段形容词
   ↓ 如果都没有时段形容词
3. 风格的基础形容词
   ↓ 如果风格也没有
4. 全局基础形容词
```

### 禁用时段形容词时

```
1. 风格的基础形容词
   ↓ 如果风格没有形容词
2. 全局基础形容词
```

## 版本历史

### v2.0.0 (2026-01-08)
- ✨ 新增时段形容词识别功能
- 🔄 重设计词库结构，支持风格级词库
- 📝 时段划分优化：凌晨、上午、下午、夜晚 4 个时段
- 🚀 启用时段形容词时强制使用 ADJ_NOUN_RANDOM 格式
- 🔧 添加基础名词的回退机制
- 📚 整合文档，简化使用说明

### v1.1.0 (2026-01-05)
- 架构简化，支持两种生成格式
- 新增 5 种个性风格
- 完整的中英文双语词库

## 常见问题

**Q: 启用时段形容词后，即使配置为 NOUN_RANDOM 也会变成 ADJ_NOUN_RANDOM？**  
A: 是的。时段形容词的核心意义在于为用户名赋予时间特质，因此必须有形容词与名词搭配。这是设计特性，不是 BUG。

**Q: 如果某个时段的形容词不存在会怎样？**  
A: 系统会自动回退：风格基础形容词 → 全局基础形容词。确保总能生成有效用户名。

**Q: 词库缓存会不会导致新词库不被加载？**  
A: 不会。系统会自动检测文件修改时间，如果词库已更新就会自动重新加载。

**Q: 支持哪些 HTTP 响应头？**  
A: 目前支持 `Last-Modified` 头来检测远程文件是否更新。

**Q: 性能如何？**  
A: 非常快。单次用户名生成通常在 1ms 以内，词库加载（包括 HTTP）也很高效。

## 架构设计

### 核心组件

1. **UsernameGenerator** - 主生成器，管理词库缓存和策略调用
2. **StyleRandomStrategy** - 统一的生成策略，实现两种格式和时段识别
3. **JsonWordLoader** - JSON 词库加载器，支持本地和 HTTP
4. **WordBank** - 词库数据模型
5. **GeneratorConfig** - 生成配置，支持灵活的参数组合

### 生成流程

```
GeneratorConfig
    ↓
UsernameGenerator.generate()
    ↓ 检查缓存或加载词库
WordBank
    ↓
StyleRandomStrategy.generate()
    ├─ 选择风格词库
    ├─ 选择形容词（考虑时段识别）
    ├─ 选择名词
    └─ 生成随机后缀
    ↓
username_suffix
```

## 代码质量

- ✅ 无编译警告
- ✅ 100% 单元测试通过
- ✅ 线程安全（使用 ConcurrentHashMap 缓存）
- ✅ 优雅的异常处理
- ✅ 清晰的代码注释

## 许可证

MIT License

