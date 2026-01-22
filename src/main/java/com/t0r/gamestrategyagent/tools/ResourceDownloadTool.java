package com.t0r.gamestrategyagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.t0r.gamestrategyagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

public class ResourceDownloadTool {

    @Tool(description = "Download a resource from a given URL")
    public String downloadResource(@ToolParam(description = "URL of the resource to download") String url, @ToolParam(description = "Name of the file to save the downloaded resource") String fileName) {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/download";
        String filePath = fileDir + "/" + fileName;

        // 创建目录
        FileUtil.mkdir(fileDir);
        // 1. 定义目标文件
        File targetFile = new File(filePath);

        // 2. 发起请求，添加“伪装”头
        HttpResponse response = HttpRequest.get(url)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36") // 模拟 Chrome
                .header(Header.REFERER, "https://www.google.com") // 或者是该资源所在的网页地址（防盗链校验）
                .execute();

        // 3. 检查状态码并写出文件
        if (response.isOk()) {
            response.writeBody(targetFile);
            return "Resource downloaded successfully to: " + filePath;
        } else {
            // 这里可以打印出具体的错误码，比如 403, 404, 500
            return "Download failed. Server status: " + response.getStatus();
        }
    }
}


