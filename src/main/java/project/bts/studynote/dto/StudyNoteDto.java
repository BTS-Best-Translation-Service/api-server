package project.bts.studynote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class StudyNoteDto {

    @Getter
    @Setter
    public static class NoteWithTranslationRequest {
        private Long userId;
        private String videoTitle;
        private String word;
        private String translated; // 프론트에서 번역된 단어 넣어줌
        private String originalSentence;
        private String translatedSentence;
    }

    @Getter
    @AllArgsConstructor
    public static class TranslateResponse {
        private String word;
        private String translated;
    }

    @Getter
    @AllArgsConstructor
    public static class WordTranslationResponse {
        private String word;
        private String translated;
        private String originalSentence;
        private String translatedSentence;
    }

}
