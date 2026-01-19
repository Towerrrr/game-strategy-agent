package com.t0r.gamestrategyagent;

import com.t0r.gamestrategyagent.app.GameApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class GameStrategyAgentApplicationTests {

    @Resource
    private GameApp gameApp;

    @Test
    void contextLoads() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是 T0r。";
        String answer = gameApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想学习如何玩泰拉瑞亚这个游戏";
        answer = gameApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我提到的游戏叫什么来着？刚跟你说过，帮我回忆一下";
        answer = gameApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

}
