package project.bts.studynote.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bts.common.service.TranslationService;
import project.bts.studynote.domain.WordTranslation;
import project.bts.studynote.dto.StudyNoteDto.NoteWithTranslationRequest;
import project.bts.studynote.dto.StudyNoteDto.TranslateResponse;
import project.bts.studynote.dto.StudyNoteDto.WordTranslationResponse;
import project.bts.studynote.dto.VideoInfoResponse;
import project.bts.studynote.repository.WordTranslationRepository;

@RequiredArgsConstructor
@Service
public class NoteService {

    private final WordTranslationRepository wordTranslationRepository;
    private final TranslationService translationService;

    /**
     * 1. 단어만 번역 (저장 X)
     */
    public TranslateResponse translateWordOnly(String word) {
        String translated = translationService.translateWord(word);
        return new TranslateResponse(word, translated);
    }

    public TranslateResponse saveNoteWithTranslation(NoteWithTranslationRequest request) {
        WordTranslation note = new WordTranslation();
        note.setUserId(request.getUserId());
        note.setVideoTitle(request.getVideoTitle());
        note.setWord(request.getWord());
        note.setTranslated(request.getTranslated());
        note.setOriginalSentence(request.getOriginalSentence());
        note.setTranslatedSentence(request.getTranslatedSentence());
        note.setYoutubeUrl(request.getYoutubeUrl());

        wordTranslationRepository.save(note);

        return new TranslateResponse(request.getWord(), request.getTranslated());
    }

    public List<WordTranslationResponse> getNotesByUserAndVideo(Long userId, String videoTitle) {
        List<WordTranslation> notes = wordTranslationRepository.findAllByUserIdAndVideoTitle(userId, videoTitle);

        return notes.stream()
                .map(note -> new WordTranslationResponse(
                        note.getWord(),
                        note.getTranslated(),
                        note.getOriginalSentence(),
                        note.getTranslatedSentence()
                ))
                .toList();
    }

    public List<VideoInfoResponse> getVideoTitleListByUserId(Long userId) {
        return wordTranslationRepository.findDistinctVideoInfoByUserId(userId);
    }
}