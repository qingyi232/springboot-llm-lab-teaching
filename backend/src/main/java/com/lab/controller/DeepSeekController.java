package com.lab.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lab.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

@RestController
@RequestMapping("/ai")
public class DeepSeekController {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekController.class);

    @Value("${deepseek.api-key:}")
    private String apiKey;

    @Value("${deepseek.api-url:https://api.deepseek.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${deepseek.model:deepseek-chat}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/chat/sse")
    public void chatSse(@RequestParam String question, HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            PrintWriter writer = response.getWriter();

            if (apiKey == null || apiKey.isEmpty() || apiKey.startsWith("sk-your")) {
                String mockReply = generateMockReply(question);
                streamOutput(writer, mockReply);
            } else {
                String fullResponse = getFullResponseFromDeepSeek(question);
                streamOutput(writer, fullResponse);
            }

            writer.write("data: [DONE]\n\n");
            writer.flush();
        } catch (Exception e) {
            log.error("SSE chat error", e);
        }
    }

    @PostMapping("/chat")
    public R chat(@RequestBody Map<String, String> params) {
        String question = params.get("question");

        if (apiKey == null || apiKey.isEmpty() || apiKey.startsWith("sk-your")) {
            return R.ok().put("data", generateMockReply(question));
        }

        try {
            String reply = getFullResponseFromDeepSeek(question);
            return R.ok().put("data", reply);
        } catch (Exception e) {
            log.error("Chat error", e);
            return R.error("AI服务暂时不可用，请稍后重试");
        }
    }

    private void streamOutput(PrintWriter writer, String content) {
        String cleaned = content.replace("\r\n", "\n").replace("\r", "\n");
        int chunkSize = 10;
        for (int i = 0; i < cleaned.length(); i += chunkSize) {
            String chunk = cleaned.substring(i, Math.min(i + chunkSize, cleaned.length()));
            sendSseChunk(writer, chunk);
            try {
                Thread.sleep(80);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendSseChunk(PrintWriter writer, String chunk) {
        if (writer == null || chunk == null || chunk.isEmpty()) return;
        String escaped = chunk.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
        writer.write("data: {\"content\":\"" + escaped + "\"}\n\n");
        writer.flush();
    }

    private String getFullResponseFromDeepSeek(String question) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("temperature", 0.7);
        body.put("max_tokens", 2000);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "你是一个高校实验教学支持系统的AI助手，专注于帮助学生解答课程内容、作业要求、实验操作等问题。请提供清晰、专业的回答。");
        messages.add(systemMsg);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", question);
        messages.add(userMsg);

        body.put("messages", messages);

        HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(body), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        JSONObject jsonResponse = JSON.parseObject(response.getBody());
        JSONArray choices = jsonResponse.getJSONArray("choices");
        return choices.getJSONObject(0).getJSONObject("message").getString("content");
    }

    private String generateMockReply(String question) {
        String q = question.toLowerCase();
        if (q.contains("数据结构") || q.contains("链表") || q.contains("算法")) {
            return "关于数据结构的问题，这是一个很好的学习方向！\n\n" +
                    "**数据结构**是计算机科学中最重要的基础课程之一。以下是一些建议：\n\n" +
                    "1. **链表**：链表是一种线性数据结构，每个节点包含数据域和指针域。建议从单链表开始学习，然后逐步掌握双向链表和循环链表。\n\n" +
                    "2. **栈和队列**：理解LIFO和FIFO的概念，掌握它们的应用场景。\n\n" +
                    "3. **树和图**：这是数据结构中较难的部分，建议先掌握二叉树的遍历，再学习平衡树和图的遍历算法。\n\n" +
                    "4. **排序算法**：掌握常见排序算法的时间复杂度和空间复杂度。\n\n" +
                    "如果你有具体的题目需要帮助，请随时告诉我！";
        } else if (q.contains("实验") || q.contains("预约")) {
            return "关于实验室预约，请参考以下步骤：\n\n" +
                    "1. 在系统首页查看可用的实验室列表\n" +
                    "2. 选择需要的实验室，查看开放时间和容纳人数\n" +
                    "3. 点击\"预约申请\"按钮，填写预约时间和说明\n" +
                    "4. 提交后等待管理员审核\n" +
                    "5. 审核通过后即可按时前往实验室\n\n" +
                    "**注意事项：**\n" +
                    "- 请提前至少1天进行预约\n" +
                    "- 如需取消预约，请在预约时间前4小时操作\n" +
                    "- 使用结束后请保持实验室整洁";
        } else if (q.contains("作业") || q.contains("成绩")) {
            return "关于作业和成绩查询：\n\n" +
                    "**查看作业：**\n" +
                    "- 在\"作业信息\"页面可以查看所有已发布的作业\n" +
                    "- 注意截止日期，及时提交\n\n" +
                    "**提交作业：**\n" +
                    "- 点击作业详情中的\"提交作业\"按钮\n" +
                    "- 填写提交内容，可上传附件\n" +
                    "- 提交后可在个人中心查看提交状态\n\n" +
                    "**查看成绩：**\n" +
                    "- 教师批改后，可在\"作业成绩\"中查看\n" +
                    "- 包括分数和教师评语\n\n" +
                    "有什么具体问题可以继续问我！";
        } else {
            return "你好！我是实验教学支持系统的AI助手。\n\n" +
                    "我可以帮助你：\n" +
                    "- 解答课程相关问题（数据结构、操作系统、计算机网络等）\n" +
                    "- 指导实验操作流程\n" +
                    "- 提供作业思路（不直接给答案）\n" +
                    "- 推荐学习资源\n" +
                    "- 解答系统使用问题\n\n" +
                    "请告诉我你的问题，我会尽力帮助你！\n\n" +
                    "*提示：当前使用的是离线模拟模式，配置DeepSeek API密钥后可获得更智能的回答。*";
        }
    }
}
