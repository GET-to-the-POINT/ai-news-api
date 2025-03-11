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
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Service
public class YoutubeDataService {

    private final YoutubeOAuth2Service youtubeoAuth2Service;  // OAuth2Service 의존성 주입

    // YouTube 서비스 생성
    private YouTube getYouTubeService(OAuth2AccessToken accessToken) throws GeneralSecurityException, IOException {
        return youtubeoAuth2Service.getYouTubeService(accessToken);  // OAuth2Service를 통해 YouTube 서비스 생성
    }

    // YouTube 동영상 조회수, 좋아요 등을 가져오는 메서드
    public String getVideoData(String videoId, OAuth2AccessToken accessToken) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getYouTubeService(accessToken);  // OAuth2 인증을 통해 서비스 객체 생성

        YouTube.Videos.List request = youtubeService.videos().list(List.of("statistics"));
        request.setId(Collections.singletonList(videoId));

        VideoListResponse response = request.execute();

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
    }

    // YouTube 동영상 조회수, 좋아요 등을 가져오는 메서드 (여러 동영상)
    public String getVideosData(String[] videoIds, OAuth2AccessToken accessToken) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getYouTubeService(accessToken);

        // 여러 개의 동영상 ID를 한 번에 전달
        YouTube.Videos.List request = youtubeService.videos().list(List.of("statistics"));
        request.setId(Arrays.asList(videoIds));  // 여러 동영상 ID를 리스트로 전달


        VideoListResponse response = request.execute();

        // 여러 동영상의 정보를 추출
        StringBuilder result = new StringBuilder();
        if (response.getItems() != null && !response.getItems().isEmpty()) {
            for (Video video : response.getItems()) {
                BigInteger viewCount = video.getStatistics().getViewCount();
                BigInteger likeCount = video.getStatistics().getLikeCount();
                BigInteger commentCount = video.getStatistics().getCommentCount();

                result.append(String.format("동영상 ID: %s\n조회수: %d\n좋아요 수: %d\n댓글 수: %d\n\n",
                        video.getId(), viewCount, likeCount, commentCount));
            }
        } else {
            return "동영상 정보를 찾을 수 없습니다.";
        }

        return result.toString();
    }
}
