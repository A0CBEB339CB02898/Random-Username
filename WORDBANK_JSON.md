# JSON词库迁移说明

## 📋 概述

项目已从分散的TXT文件迁移到结构化的JSON词库配置，大幅提升了可维护性和可扩展性。

**迁移效果**: 24个TXT文件 → 2个JSON文件 (减少92%)

---

## 📁 新的词库结构

```
src/main/resources/dict/
├── wordbank-zh.json    # 中文词库（完整）
└── wordbank-en.json    # 英文词库（完整）
```

### JSON格式示例

```json
{
  "language": "zh",
  "version": "1.0.0",
  "description": "中文词库配置文件",
  
  "basic": {
    "adjectives": ["快乐的", "勇敢的", "聪慧的", ...],
    "nouns": ["熊猫", "考拉", "开发者", ...],
    "prefixes": ["user_", "vip_", "cool_", ...]
  },
  
  "styles": {
    "explorer": {
      "name": "探索者",
      "emoji": "🌍",
      "description": "强调路与远方的探索者风格",
      "templates": [
        "探索世界的_{random}",
        "冒险家_{N}",
        "未知领地开拓者_{random}"
      ]
    },
    "attitude": { ... },
    "jianghu": { ... },
    "romantic": { ... }
  },
  
  "times": {
    "morning": {
      "name": "早晨",
      "timeRange": "06:00-10:00",
      "description": "早晨时段，充满活力和希望",
      "templates": [
        "早起的鸟儿_{random}",
        "清晨的奋斗者_{random}"
      ]
    },
    "noon": { ... },
    "night": { ... },
    "weekend": { ... },
    "normal": { ... }
  }
}
```

---

## 🚀 使用方式

### 基本使用（无变化）

```java
// 默认自动加载JSON词库
UsernameGenerator generator = new UsernameGenerator();

GeneratorConfig config = GeneratorConfig.builder()
    .mode(GenerationMode.ADJ_NOUN_RANDOM)
    .language(Language.ZH)
    .build();

String username = generator.generate(config);
// 输出: "温柔的大佬_uWFn"
```

### 自定义词库路径

```java
GeneratorConfig config = GeneratorConfig.builder()
    .wordBankPath("classpath:custom/wordbank-zh.json")
    .build();
```

### 扩展词库

#### 添加新词

编辑 `wordbank-zh.json`:

```json
{
  "basic": {
    "adjectives": [
      "快乐的",
      "新增的形容词"  // ← 添加到这里
    ]
  }
}
```

#### 添加新风格

1. **编辑JSON**:
```json
{
  "styles": {
    "cyberpunk": {
      "name": "赛博朋克",
      "emoji": "🤖",
      "templates": ["数字游民_{random}"]
    }
  }
}
```

2. **添加枚举**:
```java
// Style.java
public enum Style {
    EXPLORER("explorer"),
    ATTITUDE("attitude"),
    JIANGHU("jianghu"),
    ROMANTIC("romantic"),
    CYBERPUNK("cyberpunk");  // ← 新增
}
```

#### 添加新时间类型

类似方式，在JSON中添加配置，然后在 `TimeType` 枚举中添加对应项。

---

## ✨ 优势

| 方面 | 之前(TXT) | 现在(JSON) |
|------|-----------|------------|
| **文件数量** | 24个 | 2个 |
| **结构化** | 扁平列表 | 层级化配置 |
| **元数据** | 无 | 内置支持 |
| **IDE支持** | 基本 | 语法高亮/自动补全 |
| **可维护性** | 分散管理 | 集中管理 |

---

## 🏗️ 技术架构

```
UsernameGenerator
    ↓
AutoWordLoader (自动格式检测)
    ├─→ JsonWordLoader  → wordbank-*.json
    └─→ UnifiedWordLoader → dict/**/*.txt (向后兼容)
```

### 核心类

- `WordBankConfig` - JSON配置模型
- `JsonWordLoader` - JSON加载器
- `AutoWordLoader` - 自动格式检测
- `TimeType` - 时间类型枚举
- `Style` - 风格类型枚举

### 依赖

- **Gson 2.10.1** - JSON解析

---

## 📝 版本管理

每个词库都包含版本信息，便于追踪更新：

```json
{
  "version": "1.0.0",
  "language": "zh",
  "description": "中文词库配置文件"
}
```

建议使用语义化版本号：
- **主版本**: 不兼容的重大变更
- **次版本**: 向后兼容的新功能
- **修订号**: 向后兼容的问题修正

---

## 🔄 迁移记录

- **2026-01-07**: 完成从TXT到JSON的迁移
  - 删除24个旧TXT文件
  - 创建2个JSON词库文件
  - 移除SnakeYAML依赖
  - 添加Gson依赖
  - 所有测试通过

---

## 📚 相关文档

- [README.md](./README.md) - 项目主文档
- [wordbank-zh.json](src/main/resources/random-username-dict/wordbank-zh.json) - 中文词库
- [wordbank-en.json](src/main/resources/random-username-dict/wordbank-en.json) - 英文词库

---

*最后更新: 2026-01-07*

