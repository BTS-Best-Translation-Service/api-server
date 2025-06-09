package project.bts.studynote.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.bts.studynote.dto.StudyNoteDto.NoteWithTranslationRequest;
import project.bts.studynote.dto.StudyNoteDto.TranslateResponse;
import project.bts.studynote.dto.StudyNoteDto.VideoNoteRequest;
import project.bts.studynote.dto.StudyNoteDto.WordTranslationResponse;
import project.bts.studynote.dto.VideoInfoResponse;
import project.bts.studynote.service.NoteService;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    /**
     * 영어 단어 → 번역만 반환
     */
    @PostMapping("/translate")
    public ResponseEntity<TranslateResponse> translateWord(@RequestBody Map<String, String> request) {
        String word = request.get("word");
        TranslateResponse response = noteService.translateWordOnly(word);
        return ResponseEntity.ok(response);
    }

    /**
     * 단어, 문장 등 전체 노트 저장
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveNote(@RequestBody NoteWithTranslationRequest request) {
        noteService.saveNoteWithTranslation(request);
        return ResponseEntity.ok("스터디노트에 단어가 성공적으로 저장되었습니다.");
    }

    @PostMapping("/list-by-video")
    public ResponseEntity<List<WordTranslationResponse>> getNotesByVideo(@RequestBody VideoNoteRequest request) {
        Long userId = request.getUserId();
        String videoTitle = request.getVideoTitle();

        List<WordTranslationResponse> responses = noteService.getNotesByUserAndVideo(userId, videoTitle);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/video-titles")
    public ResponseEntity<List<VideoInfoResponse>> getVideoTitles(@RequestParam Long userId) {
        List<VideoInfoResponse> titles = noteService.getVideoTitleListByUserId(userId);
        return ResponseEntity.ok(titles);
    }
}
