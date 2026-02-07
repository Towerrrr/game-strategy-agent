package com.t0r.gamestrategyagent.controller;

import com.t0r.gamestrategyagent.app.GameApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private GameApp gameApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @GetMapping("/game_app/chat/sync")
    public String doChatWithGameAppSync(String message, String chatId) {
        return gameApp.doChat(message, chatId);
    }
}

