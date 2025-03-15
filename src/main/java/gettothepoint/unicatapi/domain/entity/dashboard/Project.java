package gettothepoint.unicatapi.domain.entity.dashboard;

import gettothepoint.unicatapi.domain.entity.BaseEntity;
import gettothepoint.unicatapi.domain.entity.member.Member;
import gettothepoint.unicatapi.domain.entity.video.UploadVideo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subtitle;
    private String thumbnailUrl;
    private String videoUrl;
    private String scriptTone;
    private String imageStyle;

    @ManyToOne
    @JoinColumn
    private Member member;

    @OneToOne
    @JoinColumn
    private UploadVideo uploadVideo;

    @OneToMany(mappedBy = "project")
    private final List<Section> sections = new ArrayList<>();

    @Builder
    public Project(Member member) {
        this.member = member;
    }

    public void assignUploadVideo(UploadVideo uploadVideo) {
        this.uploadVideo = uploadVideo;
    }
}
