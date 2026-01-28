package com.t0r.gamestrategyagent.agent.model;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyManusTest {

    @Resource
    private MyManus myManus;

    @Test
    void run() {
        String userPrompt = """  
                我正在玩泰拉瑞亚，请帮我介绍如何击杀 boss 蜂后，
                并结合一些网络图片，制定一份详细的攻略
                """;
        String answer = myManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }

}