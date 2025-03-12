package gettothepoint.unicatapi.application.service.video;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import gettothepoint.unicatapi.infrastructure.security.youtube.YoutubeOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class YoutubeDataService {

    private final YoutubeOAuth2Service youtubeoAuth2Service;  // OAuth2Service 의존성 주입

    private YouTube getYouTubeService(OAuth2AccessToken accessToken) {
        return youtubeoAuth2Service.getYouTubeService(accessToken);  // OAuth2Service를 통해 YouTube 서비스 생성
    }

    // YouTube 동영상 조회수, 좋아요 등을 가져오는 메서드
    public String getVideoData(String linkId, OAuth2AccessToken accessToken) {
        try {
            YouTube youtubeService = getYouTubeService(accessToken);  // OAuth2 인증을 통해 서비스 객체 생성

            YouTube.Videos youtubeRequest = youtubeService.videos();
            YouTube.Videos.List youtubeVideos = youtubeRequest.list(List.of("statistics"));
            youtubeVideos.setId(Collections.singletonList(linkId));

            VideoListResponse response = youtubeVideos.execute();

            // 동영상 정보 추출
            if (response.getItems() != null && !response.getItems().isEmpty()) {
                Video video = response.getItems().get(0);
                BigInteger viewCount = video.getStatistics().getViewCount();
                BigInteger likeCount = video.getStatistics().getLikeCount();
                BigInteger commentCount = video.getStatistics().getCommentCount();

                return String.format("조회수: %d, 좋아요 수: %d, 댓글 수: %d",
                        viewCount, likeCount, commentCount);
            } else {
                return "동영상 정보를 찾을 수 없습니다.";
            }
        } catch (IOException e) {
            return "동영상 정보를 가져오는 도중 에러가 발생했습니다: " + e.getMessage();
        }
    }
}
