package com.t0r.gamestrategyagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

public class WebScrapingTool {

    // 定义一个通用的浏览器 UA
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    @Tool(description = "Scrape the content of a web page")
    public String scrapeWebPage(@ToolParam(description = "URL of the web page to scrape") String url) {
        try {
            // 链式调用配置请求头
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT) // 1. 伪装 User-Agent (必须)
                    .referrer("https://www.google.com") // 2. 伪装来源 (建议)
                    .timeout(30000) // 3. 设置超时时间 30秒 (防止网络慢报错)
                    .ignoreContentType(true) // 4. 忽略内容类型检查 (防止解析非 HTML 报错)
                    .header("Accept-Language", "en-US,en;q=0.9") // 5. 模拟语言偏好
                    .get();

            return doc.html();
        } catch (org.jsoup.HttpStatusException e) {
            // 专门捕获 HTTP 状态码错误
            return "HTTP Error fetching URL. Status=" + e.getStatusCode() + ", URL=" + url;
        } catch (IOException e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}