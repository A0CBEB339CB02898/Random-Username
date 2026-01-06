# 快速开始：词库元数据功能

## 什么是词库元数据？

词库元数据功能允许你为每个词库文件添加描述性信息，包括名称、版本、描述、关键词等。这使得词库管理更加规范化和专业化。

## 5分钟快速上手

### 1. 创建元数据文件

为你的词库文件创建一个 `.meta.yml` 文件。例如，如果你有 `custom_words.txt`，创建 `custom_words.meta.yml`：

```yaml
name: 我的自定义词库
description: 包含特定领域的专业词汇
version: 1.0.0
language: zh
category: basic
keywords:
  - 自定义
  - 专业术语
minEntries: 10
author: Your Name
createdDate: 2026-01-06
lastUpdated: 2026-01-06
filePath: custom/custom_words.txt
required: false
```

### 2. 使用词库（自动加载元数据）

```java
UsernameGenerator generator = new UsernameGenerator();

GeneratorConfig config = GeneratorConfig.builder()
    .language(Language.ZH)
    .wordBankPath("custom/")  // 你的词库目录
    .build();

String username = generator.generate(config);
```

元数据会自动加载，无需额外代码！

### 3. 查看词库信息

```java
String stats = generator.getWordBankStatistics(config);
System.out.println(stats);
```

输出：
```
WordBank Statistics:
  Adjectives: 50
  Nouns: 80
  Prefixes: 20
  Styles: 2 types
  Time Templates: 3 types
  Metadata entries: 5

Metadata Details:
  - custom_words.txt: 我的自定义词库 (v1.0.0) - 包含特定领域的专业词汇 [zh/basic]
  ...
```

### 4. 启用验证（可选）

```java
UsernameGenerator generator = new UsernameGenerator();
generator.enableValidation();  // 加载时自动验证

// 或手动验证
var result = generator.validateWordBank("custom/", Language.ZH, false);
if (result.hasErrors()) {
    result.getErrors().forEach(System.out::println);
}
```

## 元数据字段说明

| 字段 | 必需 | 说明 | 示例 |
|------|------|------|------|
| name | ✅ | 词库名称 | `我的词库` |
| description | ✅ | 词库描述 | `包含xxx词汇` |
| version | ✅ | 版本号 | `1.0.0` |
| language | ✅ | 语言代码 | `zh` 或 `en` |
| category | ✅ | 类别 | `basic`, `style`, `time` |
| keywords | ❌ | 关键词列表 | `[词汇, 专业]` |
| minEntries | ❌ | 最小词条数 | `50` |
| maxEntries | ❌ | 最大词条数 | `1000` |
| author | ❌ | 作者 | `张三` |
| createdDate | ❌ | 创建日期 | `2026-01-06` |
| lastUpdated | ❌ | 更新日期 | `2026-01-06` |
| filePath | ❌ | 文件路径 | `dict/zh/xxx.txt` |
| required | ❌ | 是否必需 | `true` 或 `false` |

## 常见问题

### Q: 必须创建元数据文件吗？
**A:** 不必须。元数据文件是可选的，没有元数据文件不影响词库正常使用。

### Q: 元数据文件放在哪里？
**A:** 与对应的词库文件放在同一目录。例如：
```
dict/zh/
  ├── adjectives.txt
  ├── adjectives.meta.yml  ← 这里
  ├── nouns.txt
  └── nouns.meta.yml       ← 这里
```

### Q: 支持哪些加载方式？
**A:** 支持三种方式：
1. Classpath: `classpath:dict/zh/`
2. 文件系统: `/path/to/dict/`
3. HTTP: `http://example.com/dict/`

### Q: 验证失败会怎样？
**A:** 
- 默认（关闭验证）：不影响，静默跳过
- 宽松模式：记录警告，继续运行
- 严格模式：抛出异常，停止运行

### Q: 如何更新元数据？
**A:** 直接修改 `.meta.yml` 文件，保存即可。系统会在下次加载时自动读取。

### Q: 可以自定义字段吗？
**A:** 可以！使用 `customProperties` 添加任意自定义字段（未来版本将支持）。

## 最佳实践

1. ✅ 为重要的词库添加元数据
2. ✅ 使用语义化版本号（如 1.0.0, 1.1.0, 2.0.0）
3. ✅ 及时更新 `lastUpdated` 字段
4. ✅ 设置合理的 `minEntries` 确保质量
5. ✅ 生产环境建议启用验证

## 下一步

- 📖 查看完整文档：[METADATA_GUIDE.md](./METADATA_GUIDE.md)
- 🔍 查看示例：`src/main/resources/dict/zh/*.meta.yml`
- 🧪 运行测试：`mvn test -Dtest=MetadataTest`

## 需要帮助？

- 查看测试代码：`src/test/java/.../MetadataTest.java`
- 查看实施总结：[IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md)
- 提交 Issue 或 PR

---

**提示**：元数据功能完全向后兼容，不会影响现有代码。你可以逐步为词库添加元数据，享受更好的管理体验！

