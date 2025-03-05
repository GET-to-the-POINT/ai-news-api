package taeniverse.unicatApi.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taeniverse.unicatApi.mvc.model.entity.OAuthLink;
import taeniverse.unicatApi.mvc.repository.OAuthLinkRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final OAuthLinkRepository oAuthLinkRepository;

    public List<String> getLinkedProviders(Long memberId) {
        return oAuthLinkRepository.findByMemberId(memberId)
                .stream()
                .map(OAuthLink::getProvider)
                .collect(Collectors.toList());
    }

}
