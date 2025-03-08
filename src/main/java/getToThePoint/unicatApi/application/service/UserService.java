package getToThePoint.unicatApi.application.service;

import getToThePoint.unicatApi.domain.entity.OAuthLink;
import getToThePoint.unicatApi.domain.repository.OAuthLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
