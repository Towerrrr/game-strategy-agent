# Game Strategy Agent 前端实现说明（Vue3 + TypeScript）

## 1. 目标

后续在新工作区中，基于本说明生成一个可运行的前端项目，完成与当前后端聊天接口的联调，重点支持：

- 普通同步聊天
- SSE 流式聊天
- 中文内容稳定显示（避免乱码）


## 2. 技术栈要求

- 框架：Vue 3
- 语言：TypeScript
- 构建工具：Vite
- 状态管理：可选（优先轻量实现，必要时使用 Pinia）
- HTTP：优先使用原生 `fetch`
- 样式：CSS / SCSS 均可（先保证功能与可读性）


## 3. 后端信息（当前项目）

- 服务端口：`8123`
- 上下文路径：`/api`
- 基础前缀：`http://localhost:8123/api`

### 3.1 接口列表

1. 同步聊天（返回完整文本）  
`GET /ai/game_app/chat/sync?message={message}&chatId={chatId}`

2. 流式聊天（SSE）  
`GET /ai/game_app/chat/sse?message={message}&chatId={chatId}`  
说明：`Content-Type` 为 `text/event-stream;charset=UTF-8`

3. 流式聊天（SseEmitter）  
`GET /ai/game_app/chat/sse/emitter?message={message}&chatId={chatId}`  
说明：`Content-Type` 为 `text/event-stream;charset=UTF-8`


## 4. 页面与交互要求

至少实现一个单页聊天界面，包含：

- `chatId` 输入框（可手动输入，默认给一个随机值）
- 提问输入框
- 发送按钮
- 模式切换（`sync` / `sse`）
- 消息列表（区分用户与助手）
- 流式输出进行中状态（如“生成中...”）
- 错误提示区域（网络错误、接口错误）


## 5. 流式传输实现要求（重点）

### 5.1 推荐方案

优先使用 `fetch + ReadableStream + TextDecoder('utf-8')` 读取 SSE。

关键点：

- `TextDecoder` 必须使用 UTF-8
- 分片解码时使用 `decoder.decode(chunk, { stream: true })`
- 结束时再调用一次 `decoder.decode()` 刷尾部缓存
- 以 SSE 协议解析 `data:` 行并拼接到当前助手消息

### 5.2 兼容方案

可提供 `EventSource` 版本作为备选，但需处理：

- URL query 参数编码（`encodeURIComponent`）
- 连接关闭与重连策略（本项目可先不自动重连）

### 5.3 防乱码约束

- 前端只按 UTF-8 解码
- 不做 `escape/unescape` 或手工二次转码
- 不对文本做错误的 `atob/btoa` 编解码
- 展示层直接渲染正常 Unicode 字符串


## 6. 数据结构建议

```ts
type ChatRole = 'user' | 'assistant' | 'system';

interface ChatMessage {
  id: string;
  role: ChatRole;
  content: string;
  createdAt: number;
  status?: 'pending' | 'streaming' | 'done' | 'error';
}
```


## 7. 前端项目结构建议

```text
src/
  api/
    chat.ts
  components/
    ChatInput.vue
    ChatMessageList.vue
    ChatModeSwitch.vue
  composables/
    useChat.ts
  types/
    chat.ts
  App.vue
  main.ts
```


## 8. API 封装要求

在 `src/api/chat.ts` 中至少提供：

- `chatSync(message: string, chatId: string): Promise<string>`
- `chatSse(message: string, chatId: string, onDelta: (text: string) => void): Promise<void>`

并统一：

- baseURL 配置（支持 `.env`）
- 请求超时与错误包装
- 可中断流式请求（`AbortController`）


## 9. 验收标准

1. 能成功调用同步接口并显示完整回复
2. 能成功调用流式接口并逐字/逐段显示回复
3. 中文回复显示正常，无乱码
4. 发送新问题时不会覆盖历史记录
5. 流式请求可被中断，UI 状态正确恢复


## 10. 非目标（首版不做）

- 登录鉴权
- 多会话持久化到后端
- 复杂富文本渲染（Markdown 可后续加）
- 复杂视觉系统与主题切换


## 11. 生成前端时的执行指令（给后续 Codex）

在新工作区中执行：

1. 用 Vue3 + TypeScript + Vite 初始化项目
2. 按本文件第 4～9 节实现功能
3. 优先保证流式与中文显示稳定
4. 完成后提供启动命令、目录说明、关键代码说明

