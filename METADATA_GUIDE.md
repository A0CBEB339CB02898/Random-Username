# 词库元数据功能使用指南

## 概述

从版本 1.1.0 开始，Random-Username 支持词库元数据功能。每个词库文件都可以有一个对应的 `.meta.yml` 元数据文件，用于描述词库的详细信息。

## 元数据文件格式

元数据文件使用 YAML 格式，命名规则为：`{词库文件名}.meta.yml`

例如：
- `adjectives.txt` 对应 `adjectives.meta.yml`
- `style_explorer.txt` 对应 `style_explorer.meta.yml`

### 标准字段

```yaml
name: "词库名称"
description: "词库描述"
version: "1.0.0"
language: "zh"  # zh 或 en
category: "basic"  # basic, style, time
keywords:
  - "关键词1"
  - "关键词2"
minEntries: 50  # 最小词条数量（用于验证）
maxEntries: 1000  # 最大词条数量（可选）
author: "作者名称"
createdDate: "2026-01-06"
lastUpdated: "2026-01-06"
filePath: "dict/zh/adjectives.txt"
required: true  # 是否为必需词库
customProperties:  # 自定义属性
  encoding: "UTF-8"
  wordType: "adjective"
```

## 使用方法

### 1. 基本使用（自动加载元数据）

```java
UsernameGenerator generator = new UsernameGenerator();

GeneratorConfig config = GeneratorConfig.builder()
    .language(Language.ZH)
    .build();

// 生成用户名时会自动加载并关联元数据
String username = generator.generate(config);
```

### 2. 启用验证功能

```java
UsernameGenerator generator = new UsernameGenerator();

// 启用验证，加载词库时会自动验证元数据
generator.enableValidation();

GeneratorConfig config = GeneratorConfig.builder()
    .language(Language.ZH)
    .build();

String username = generator.generate(config);
```

### 3. 手动验证词库配置

```java
UsernameGenerator generator = new UsernameGenerator();

// 严格模式：遇到错误会抛出异常
WordBankConfigValidator.ValidationResult result = 
    generator.validateWordBank("classpath:dict/zh/", Language.ZH, true);

// 非严格模式：只记录警告，不抛出异常
WordBankConfigValidator.ValidationResult result2 = 
    generator.validateWordBank("classpath:dict/zh/", Language.ZH, false);

// 查看验证结果
System.out.println("错误数: " + result.getErrors().size());
System.out.println("警告数: " + result.getWarnings().size());
```

### 4. 获取词库统计信息

```java
UsernameGenerator generator = new UsernameGenerator();

GeneratorConfig config = GeneratorConfig.builder()
    .language(Language.ZH)
    .build();

// 获取包含元数据的统计信息
String statistics = generator.getWordBankStatistics(config);
System.out.println(statistics);
```

输出示例：
```
WordBank Statistics:
  Adjectives: 150
  Nouns: 200
  Prefixes: 50
  Styles: 4 types
  Time Templates: 5 types
  Metadata entries: 6

Metadata Details:
  - adjectives.txt: 中文形容词词库 (v1.0.0) - 包含各种形容词，用于生成富有表现力的中文用户名 [zh/basic]
  - nouns.txt: 中文名词词库 (v1.0.0) - 包含各种名词，用于生成生动的中文用户名 [zh/basic]
  - style_explorer.txt: 探索者风格词库 (v1.0.0) - 强调'路'与'远方'的探索者风格模板 [zh/style]
```

### 5. 直接加载元数据

```java
WordBankMetadataLoader loader = new WordBankMetadataLoader();

// 加载单个元数据文件
WordBankMetadata metadata = loader.loadMetadata("classpath:dict/zh/adjectives.meta.yml");

System.out.println("名称: " + metadata.getName());
System.out.println("版本: " + metadata.getVersion());
System.out.println("语言: " + metadata.getLanguage());
System.out.println("类别: " + metadata.getCategory());
System.out.println("最小词条数: " + metadata.getMinEntries());

// 批量加载目录下所有元数据
Map<String, WordBankMetadata> metadataMap = 
    loader.loadAllMetadataInDirectory("classpath:dict/zh/");

metadataMap.forEach((fileName, meta) -> {
    System.out.println(fileName + ": " + meta.getSummary());
});
```

## 元数据验证

元数据验证器会检查：

1. **必需文件是否存在**
   - adjectives.txt
   - nouns.txt
   - prefixes.txt

2. **元数据格式是否有效**
   - name、language、category 是否为空
   - minEntries 是否为非负数
   - maxEntries 是否大于 minEntries

3. **版本号格式是否正确**
   - 支持 1.0, 1.0.0, 1.0.0-beta 等格式

## 自定义词库示例

### 创建自定义风格词库

1. 创建词库文件 `my_custom_style.txt`：
```
[STYLE_CUSTOM]
{random}的{adjective}{noun}
超级{adjective}的{noun}
```

2. 创建元数据文件 `my_custom_style.meta.yml`：
```yaml
name: "我的自定义风格"
description: "这是一个自定义风格的词库"
version: "1.0.0"
language: "zh"
category: "style"
keywords:
  - "自定义"
  - "个性化"
minEntries: 2
author: "Your Name"
createdDate: "2026-01-06"
lastUpdated: "2026-01-06"
filePath: "custom/my_custom_style.txt"
required: false
customProperties:
  styleKey: "custom"
  theme: "personalized"
```

3. 使用自定义词库：
```java
GeneratorConfig config = GeneratorConfig.builder()
    .language(Language.ZH)
    .wordBankPath("/path/to/custom/")
    .build();

String username = generator.generate(config);
```

## 元数据的好处

1. **自文档化**：清楚描述每个词库的用途和内容
2. **版本管理**：跟踪词库的版本和更新历史
3. **质量保证**：通过验证确保词库符合规范
4. **可维护性**：方便团队协作和词库管理
5. **扩展性**：支持自定义属性，满足特殊需求

## 最佳实践

1. 为所有词库文件创建元数据文件
2. 保持版本号的语义化（遵循 SemVer）
3. 及时更新 lastUpdated 字段
4. 使用有意义的 keywords 便于搜索
5. 设置合理的 minEntries 确保词库质量
6. 在生产环境启用验证功能
7. 定期检查和更新元数据

## 注意事项

- 元数据文件是可选的，不影响词库的正常使用
- 如果元数据文件不存在，加载器会静默跳过
- 验证功能默认关闭，需要手动启用
- 严格模式验证会在发现错误时抛出异常
- 支持 HTTP 源的元数据加载

## 示例项目结构

```
src/main/resources/dict/
├── zh/
│   ├── adjectives.txt
│   ├── adjectives.meta.yml
│   ├── nouns.txt
│   ├── nouns.meta.yml
│   ├── prefixes.txt
│   ├── prefixes.meta.yml
│   ├── style_explorer.txt
│   ├── style_explorer.meta.yml
│   └── ...
└── en/
    ├── adjectives.txt
    ├── adjectives.meta.yml
    ├── nouns.txt
    ├── nouns.meta.yml
    └── ...
```

