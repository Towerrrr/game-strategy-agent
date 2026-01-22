package com.t0r.gamestrategyagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class GameApp {

    @Resource
    private VectorStore gameAppVectorStore;

    @Resource
    private ToolCallback[] allTools;

    private final ChatClient chatClient;

    // 构造函数注入 ChatModel 和 SystemPrompt 资源文件
    public GameApp(ChatModel dashscopeChatModel,
                   @Value("classpath:document/SystemPrompt.md") org.springframework.core.io.Resource systemPromptResource) {

        // 1. 读取 SystemPrompt.md 文件内容
        String systemPrompt;
        try {
            // Spring Framework 6.0+ 支持直接 getContentAsString
            systemPrompt = systemPromptResource.getContentAsString(StandardCharsets.UTF_8);
            log.info("成功加载 System Prompt，长度: {}", systemPrompt.length());
        } catch (IOException e) {
            log.error("无法加载 SystemPrompt.md，将使用默认空提示词", e);
            systemPrompt = "你是一位名为“超级泰君”的资深《泰拉瑞亚》专家。" +
                    "你精通全平台（PC、主机、移动端）的各类游戏，从硬核魂类游戏、竞技射击到休闲经营都能信手拈来。" +
                    "你不仅拥有海量的游戏知识库，更擅长根据玩家的实际操作水平、游戏进度和资源情况提供个性化的定制建议。";
        }

        // 2. 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();

        // 3. 构建 ChatClient
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(systemPrompt) // 使用读取到的文件内容
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
//                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答
                .advisors(new QuestionAnswerAdvisor(gameAppVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
//                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}