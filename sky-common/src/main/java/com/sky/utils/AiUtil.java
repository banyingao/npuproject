package com.sky.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class AiUtil {

    @Value("${sky.ai.key}")
    private String apiKey;

    public String callAI(String input) {
        try {
            URL url = new URL("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);

            // 🔥 Prompt（很关键）
            String prompt = "你是一个外卖点餐助手，请根据用户需求推荐菜品ID列表。\n" +
                    "要求：\n" +
                    "1. 只返回JSON\n" +
                    "2. 格式：{\"dishIds\":[1,2]}\n" +
                    "3. 不要解释\n" +
                    "用户需求：" + input;

            JSONObject body = new JSONObject();
            body.put("model", "qwen-turbo");

            JSONObject inputObj = new JSONObject();
            inputObj.put("prompt", prompt);

            body.put("input", inputObj);

            OutputStream os = conn.getOutputStream();
            os.write(body.toJSONString().getBytes("UTF-8"));
            os.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}