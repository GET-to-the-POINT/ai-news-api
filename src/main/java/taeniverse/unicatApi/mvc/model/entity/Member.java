package taeniverse.unicatApi.mvc.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Member 엔티티의 기본키 자동 생성
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<OAuthLink> oAuthLinks = new ArrayList<>();

    private Instant createAt;
    private Instant updateAt;

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @PrePersist
    protected void onCreate() {
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = Instant.now();
    }
}