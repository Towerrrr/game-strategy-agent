package com.t0r.gamestrategyagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class GameAppTest {

    @Resource
    private GameApp gameApp;

    @Test
    void doChat() {
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是T0r，我想打蜂后，但我不知道该怎么做";
        GameReport GameReport = gameApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(GameReport);
    }

}