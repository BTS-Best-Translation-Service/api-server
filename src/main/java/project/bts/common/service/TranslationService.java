package project.bts.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TranslationService {

    @Value("${openai.api-key}")
    private String openAiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();

    public String translateWord(String word) {
        String prompt = "다음 영어 단어를 한국어로 번역해줘. 반드시 하나의 단어로만 번역해줘.\n영어: " + word + "\n한국어:";

        // JSON 빌드
        ObjectNode messageNode = objectMapper.createObjectNode();
        messageNode.put("role", "user");
        messageNode.put("content", prompt);

        ObjectNode root = objectMapper.createObjectNode();
        root.put("model", "gpt-3.5-turbo");
        root.put("temperature", 0.2);
        root.set("messages", objectMapper.createArrayNode().add(messageNode));

        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 생성 실패: " + e.getMessage());
        }

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + openAiApiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        jsonBody, MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("OpenAI 응답 실패: " + response);
            }

            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode
                    .get("choices").get(0)
                    .get("message").get("content")
                    .asText()
                    .trim();

        } catch (IOException e) {
            throw new RuntimeException("번역 요청 중 오류 발생: " + e.getMessage());
        }
    }
}
