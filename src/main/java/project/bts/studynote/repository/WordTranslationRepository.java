package project.bts.studynote.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.bts.studynote.domain.WordTranslation;
import project.bts.studynote.dto.VideoInfoResponse;

public interface WordTranslationRepository extends JpaRepository<WordTranslation, Long> {

    List<WordTranslation> findAllByUserIdAndVideoTitle(Long userId, String videoTitle);

    @Query("SELECT DISTINCT new project.bts.studynote.dto.VideoInfoResponse(w.videoTitle, w.youtubeUrl) " +
            "FROM WordTranslation w WHERE w.userId = :userId")
    List<VideoInfoResponse> findDistinctVideoInfoByUserId(@Param("userId") Long userId);

}
