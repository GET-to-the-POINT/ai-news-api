package taeniverse.unicatApi.mvc.repository;

import com.google.api.services.youtube.model.VideoStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import taeniverse.unicatApi.mvc.model.entity.UploadVideo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VideoUpdateRepository extends JpaRepository<UploadVideo, Long> {
    @Query(value = "SELECT youtube_video_id FROM upload_video", nativeQuery = true)
    List<String> findAllVideoIds();
    Optional<UploadVideo> findByYoutubeVideoId(String youtubeVideoId);

}