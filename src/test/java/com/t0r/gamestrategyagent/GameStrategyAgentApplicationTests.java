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

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        String message = "我玩的是法师，肉山要怎么打？";
        String answer =  gameApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我玩的是法师，蜂后要怎么打？";
        String answer =  gameApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("推荐几个和泰拉类似的游戏？");

        // 测试网页抓取
        testMessage("最近泰拉有点玩不下去，看看百度网站（baidu.com）有没有什么其他的游戏推荐？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张泰拉的游玩图片为文件");

        // 测试文件操作
        testMessage("保存我的游戏档案为文件");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = gameApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }


}
