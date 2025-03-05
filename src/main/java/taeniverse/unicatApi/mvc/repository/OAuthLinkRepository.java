package taeniverse.unicatApi.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taeniverse.unicatApi.mvc.model.entity.OAuthLink;

import java.util.List;

@Repository
public interface OAuthLinkRepository extends JpaRepository<OAuthLink, Long> {

    List<OAuthLink> findByMemberId(Long memberId);

}
