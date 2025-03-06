package getToThePoint.unicatApi.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class OAuthLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String provider;

    @ManyToOne
    @JoinColumn
    private Member member;

    @Builder
    public OAuthLink(String email, String provider, Member member) {
        this.email = email;
        this.provider = provider;
        this.member = member;
    }
}
