package project.bts.studynote.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.bts.studynote.domain.WordTranslation;

public interface WordTranslationRepository extends JpaRepository<WordTranslation, Long> {

    List<WordTranslation> findAllByUserIdAndVideoTitle(Long userId, String videoTitle);

    @Query("SELECT DISTINCT w.videoTitle FROM WordTranslation w WHERE w.userId = :userId")
    List<String> findDistinctVideoTitlesByUserId(@Param("userId") Long userId);
}
