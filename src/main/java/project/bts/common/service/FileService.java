package project.bts.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FileService {

    private final RestTemplate restTemplate;

    public Map<String, Object> requestSrtFromExternalServer(String videoTitle, String videoUrl) {
        // 번역 서버로 POST 요청 → S3 링크 반환
        String targetUrl = "http://54.180.24.69:8000/process-audio";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("videoTitle", videoTitle);
        requestBody.put("videoUrl", videoUrl);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, entity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("외부 서버 SRT 응답 파싱 실패: " + e.getMessage());
        }
    }
}
