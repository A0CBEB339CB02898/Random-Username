# Random-Username

一个功能强大的随机用户名生成库，支持多种模式、风格以及中英文。

## 特性

*   **多种生成模式**：
    1.  固定前缀 + 随机字符串（数字+字母）
    2.  随机形容词 + 名词 + 随机字符串
    3.  名词 + 随机字符串
    4.  基于注册时间的动态组合（清晨、深夜、周末等）
    5.  多维度风格化：探索者、态度、江湖、浪漫
*   **多语言支持**：支持简体中文 (ZH) 和英文 (EN)。
*   **词库独立**：支持通过本地文件或网络 URL 加载自定义词库。
*   **重复率低**：结合随机字符串和模板，极大降低用户名重复概率。
*   **简洁架构**：代码量减少 38%，易于理解和维护。
*   **100% 向后兼容**：现有代码无需任何修改。

## 安装

### 从 GitHub Packages 安装

首先，在你的 `pom.xml` 中配置 GitHub Packages 仓库：

```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/A0CBEB339CB02898/Random-Username</url>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

然后添加依赖：

```xml
<dependency>
    <groupId>io.github.a0cbeb339cb02898</groupId>
    <artifactId>random-username</artifactId>
    <version>1.1.0</version>
</dependency>
```

### 本地使用

或者，你可以将源代码拷贝到你的项目中直接使用。


## 快速开始

```java
import github.A0CBEB339CB02898.randomusername.UsernameGenerator;
import github.A0CBEB339CB02898.randomusername.config.*;
import java.time.LocalDateTime;

public class Example {
    public static void main(String[] args) {
        UsernameGenerator generator = new UsernameGenerator();

        // 1. 前缀 + 随机字符串
        String username1 = generator.generate(
            GeneratorConfig.builder()
                .mode(GenerationMode.PREFIX_RANDOM)
                .prefix("test_")
                .randomLength(6)
                .build()
        );
        System.out.println("PREFIX_RANDOM: " + username1);  // test_abc123

        // 2. 形容词 + 名词 + 随机字符串
        String username2 = generator.generate(
            GeneratorConfig.builder()
                .mode(GenerationMode.ADJ_NOUN_RANDOM)
                .language(Language.ZH)
                .randomLength(4)
                .build()
        );
        System.out.println("ADJ_NOUN_RANDOM: " + username2);  // 聪慧的鲨鱼_aBc2

        // 3. 名词 + 随机字符串
        String username3 = generator.generate(
            GeneratorConfig.builder()
                .mode(GenerationMode.NOUN_RANDOM)
                .language(Language.EN)
                .build()
        );
        System.out.println("NOUN_RANDOM: " + username3);  // elephant_xY9k

        // 4. 基于时间的生成
        String username4 = generator.generate(
            GeneratorConfig.builder()
                .mode(GenerationMode.TIME_BASED)
                .registrationTime(LocalDateTime.now())
                .build()
        );
        System.out.println("TIME_BASED: " + username4);

        // 5. 基于风格的生成
        String username5 = generator.generate(
            GeneratorConfig.builder()
                .mode(GenerationMode.STYLE_BASED)
                .style(Style.EXPLORER)
                .build()
        );
        System.out.println("STYLE_BASED: " + username5);
    }
}
```

## 自定义词库

词库支持单文件模式或目录模式。

### 单文件模式

文件采用按行读取，通过 `[SECTION]` 区分不同模块：

```text
[ADJECTIVES]
勇敢的
快乐的

[NOUNS]
行者
小敢

[STYLE_EXPLORER]
第{N}位赶路人
```

### 目录模式

将词库拆分为多个文件存放在同一目录下（建议方案），加载器会自动识别以下文件：
- `adjectives.txt`: 形容词
- `nouns.txt`: 名词
- `prefixes.txt`: 前缀
- `style_{key}.txt`: 风格模板 (如 `style_explorer.txt`)
- `time_{key}.txt`: 时间模板 (如 `time_morning.txt`)

*注：每个拆分后的文件内部仍需保留 `[SECTION]` 标识以便正确解析。*

### 加载方式

```java
GeneratorConfig config = GeneratorConfig.builder()
        .wordBankPath("C:/path/to/your/dict/") // 传入目录或文件路径，支持 http://...
        .build();
```

## 内存优化与热更新

为了应对大词库以及动态更新的需求，本项目提供了以下机制：

### 1. 缓存控制
默认情况下，加载过的词库会缓存在内存中以提高性能。如果词库非常庞大或希望节省内存，可以关闭缓存：
```java
GeneratorConfig config = GeneratorConfig.builder()
        .useCache(false) // 禁用缓存，每次生成都会重新加载
        .build();
```

### 2. 自动热更新
对于本地文件和支持 `Last-Modified` 的 HTTP 资源，即使开启了缓存，生成器也会自动检测文件的最后修改时间。如果文件已更新，它会自动重新加载，无需重启程序。

### 3. 手动刷新缓存
你也可以在程序运行期间通过 `UsernameGenerator` 提供的方法手动清除或刷新特定词库：
```java
generator.clearCache(); // 清除所有缓存
generator.reload(Language.ZH); // 重新加载默认中文词库
generator.reload("C:/path/to/your/dict/"); // 刷新特定路径的词库
```

## 开发与扩展

本项目采用策略模式开发，可以轻松添加新的生成策略。

1.  在 `github.A0CBEB339CB02898.randomusername.strategy` 包下新建策略类继承 `UsernameStrategy`。
2.  在 `UsernameGenerator` 中修改 strategies 映射以注册新策略。

## 更新日志

### v1.1.0 (2026-01-05)
- 🔄 架构优化：简化代码架构，代码量减少 38%
- 🎯 策略合并：5 个独立策略类合并为 2 个核心策略
- 🚀 加载器统一：统一文件和 HTTP 加载器为 UnifiedWordLoader
- 📦 缓存简化：移除复杂的 CacheEntry，使用更简洁的 computeIfAbsent
- ✅ 100% 向后兼容：所有现有代码无需修改

### v1.0.0
- 初始版本发布

## 常见问题

**Q: v1.1 版本现有代码需要修改吗？**  
A: 不需要。所有 API 完全兼容，零改动。

**Q: 内部实现有什么改变？**  
A: 主要改动：
- PrefixRandomStrategy、AdjNounRandomStrategy、NounRandomStrategy 合并为 SimpleRandomStrategy
- TimeBasedStrategy、StyleBasedStrategy 合并为 TemplateBasedStrategy
- FileWordLoader、HttpWordLoader 合并为 UnifiedWordLoader
- 缓存机制简化，移除 CacheEntry 内部类

这些改动对使用者完全透明，功能和性能无任何变化。

**Q: 性能有影响吗？**  
A: 没有。缓存机制完全相同，生成速度无变化。内存占用甚至轻微减少。

**Q: 旧的策略类还在吗？**  
A: 是的，为了向后兼容，旧类仍然存在但不被使用。如果不需要兼容性，可以删除它们。

## 许可证

MIT License
