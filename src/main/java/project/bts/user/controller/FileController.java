package project.bts.user.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bts.common.dto.SrtDto.SrtLinkRequest;
import project.bts.common.dto.SrtDto.SrtRequest;
import project.bts.common.service.FileService;
import project.bts.user.domain.User;
import project.bts.user.repository.UserRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    private final UserRepository userRepository;

    @PostMapping("/srt")
    public ResponseEntity<Map<String, Object>> getSrt(@RequestBody SrtRequest request) {

        // UUID 기준으로 유저 존재 확인
        User user = userRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUserId(request.getUserId()); // 요청에서 받은 UUID 그대로 사용
                    return userRepository.save(newUser);
                });

        Map<String, Object> result = fileService.requestSrtFromExternalServer(
                request.getVideoTitle(),
                request.getVideoUrl()
        );

        Object srt = result.get("srt");

        // 응답 구성
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId().toString());
        response.put("srt", srt);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/srt-link")
    public ResponseEntity<Map<String, String>> getSrtFileLinkOnly(@RequestBody SrtLinkRequest request) {

        // 외부 서버에서 전체 JSON 받아오기
        Map<String, Object> result = fileService.requestSrtFromExternalServer(
                request.getVideoTitle(),
                request.getVideoUrl()
        );

        // s3Link만 추출
        String s3Link = (String) result.get("s3Link");

        Map<String, String> response = new HashMap<>();
        response.put("s3Link", s3Link);

        return ResponseEntity.ok(response);
    }
}