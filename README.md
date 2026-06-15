# Telegram Web Monitor Alarm

基于 SikuliX 的 Telegram Web 聊天监控工具，检测@提及和私聊消息并播放警报音。

## 功能特性

- ✅ 图像识别监控 Telegram Web 界面
- ✅ 检测@提及图标
- ✅ 检测未读消息徽章
- ✅ 检测私聊图标
- ✅ OCR 文本识别 (@符号检测)
- ✅ 自定义警报声音
- ✅ 桌面通知
- ✅ 可配置检查间隔和灵敏度

## 系统要求

- Java 11 或更高版本
- Maven 3.6+
- SikuliX 依赖 (自动下载)
- Telegram Web 在浏览器中打开

## 快速开始

### 1. 构建项目

```powershell
cd C:\prj\mntAlmTg
mvn clean package
```

### 2. 准备文件

确保以下文件存在：

- `alarm.mp3` - 警报声音文件 (放在项目根目录)
- `patterns/mention_icon.png` - @提及图标截图
- `patterns/unread_badge.png` - 未读徽章截图
- `patterns/dm_icon.png` - 私聊图标截图

### 3. 运行监控

```powershell
# 方式 1: 使用 Maven
mvn exec:java -Dexec.mainClass="com.mntalm.Main"

# 方式 2: 使用 JAR
java -jar target\mntAlmTg-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### 4. 打开 Telegram Web

在浏览器中打开 https://web.telegram.org 并登录

## 配置说明

编辑 `config.json`:

```json
{
  "checkIntervalMs": 2000,        // 检查间隔 (毫秒)
  "searchTimeoutMs": 1000,        // 图像搜索超时 (毫秒)
  "patternSimilarity": 0.8,       // 图像匹配相似度 (0.0-1.0)
  "ocrEnabled": false,            // 是否启用 OCR 文本识别
  "alarmSoundPath": "alarm.mp3",  // 警报声音文件路径
  "desktopNotification": true,    // 是否显示桌面通知
  "telegramWindow": "Telegram"    // Telegram 窗口标题
}
```

## 图案文件准备

### 如何获取图案文件

1. **截图工具**: 使用截图工具截取 Telegram Web 界面元素
2. **保存位置**: 放入 `src/main/resources/patterns/` 目录
3. **重新构建**: 运行 `mvn clean package`

### 需要截取的图案

| 图案文件 | 说明 | 截图位置 |
|---------|------|---------|
| `mention_icon.png` | @提及图标 | 聊天列表中的@图标 |
| `unread_badge.png` | 未读徽章 | 聊天项上的数字徽章 |
| `dm_icon.png` | 私聊图标 | 私聊会话图标 |

## 项目结构

```
C:\prj\mntAlmTg\
├── pom.xml                      # Maven 配置
├── config.json                  # 配置文件
├── alarm.mp3                    # 警报声音 (需自备)
├── patterns\                    # 图像图案目录
│   ├── mention_icon.png        # @提及图标
│   ├── unread_badge.png        # 未读徽章
│   └── dm_icon.png             # 私聊图标
├── src\main\java\com\mntalm\
│   ├── Main.java               # 主入口
│   ├── TelegramMonitor.java    # 监控核心逻辑
│   ├── AlarmPlayer.java        # 警报播放器
│   └── Config.java             # 配置管理
└── target\                     # 构建输出
```

## 使用说明

### 基本使用

1. 启动程序
2. 打开 Telegram Web 并确保在屏幕上可见
3. 程序会自动检测@提及和私聊
4. 发现通知时播放警报音

### 调整灵敏度

如果误报太多：
- 降低 `patternSimilarity` (如 0.7)
- 增加 `checkIntervalMs` (如 5000)

如果漏报：
- 提高 `patternSimilarity` (如 0.9)
- 启用 `ocrEnabled: true`

### 调试模式

查看日志输出：

```powershell
# 显示详细日志
$env:LOG_LEVEL="DEBUG"
java -jar target\mntAlmTg-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 故障排除

### 问题：找不到图案

**解决**:
1. 确保 Telegram Web 在屏幕上可见
2. 检查图案文件是否存在
3. 调整 `patternSimilarity` 值
4. 重新截取图案 (Telegram UI 可能更新)

### 问题：警报不播放

**解决**:
1. 检查 `alarm.mp3` 文件是否存在
2. 检查系统音量
3. 确认音频格式支持 (MP3/WAV)

### 问题：OCR 识别慢

**解决**:
1. 设置 `ocrEnabled: false` 禁用 OCR
2. 增加 `checkIntervalMs`
3. OCR 需要额外依赖，建议仅用图像识别

## 高级功能

### 自定义警报

可以修改 `AlarmPlayer.java` 支持：
- 多个警报声音
- 循环播放
- 音量控制

### 添加更多检测

编辑 `TelegramMonitor.java` 添加：
- 关键词检测
- 特定联系人检测
- 时间范围检测

## 依赖说明

| 依赖 | 用途 |
|------|------|
| SikuliX | 图像识别和屏幕操作 |
| Tesseract | OCR 文本识别 (可选) |
| Logback | 日志记录 |
| Gson | JSON 配置解析 |

## 注意事项

⚠️ **重要**:
- 程序需要屏幕常亮
- Telegram Web 必须保持打开和可见
- 不要最小化浏览器窗口
- 图案文件需要随 Telegram UI 更新而更新

## 许可证

MIT License

## 作者

Internal Tool - For Personal Use

---

**最后更新**: 2026-06-15
