package gettothepoint.unicatapi.application.service;

import gettothepoint.unicatapi.domain.dto.project.ScriptRequest;
import gettothepoint.unicatapi.domain.dto.project.ScriptResponse;
import gettothepoint.unicatapi.domain.repository.ProjectRepository;
import gettothepoint.unicatapi.domain.repository.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class OpenAiService {

    private final ChatClient chatClient;
    private final SectionRepository sectionRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public OpenAiService(ChatClient.Builder chatClientBuilder, SectionRepository sectionRepository, ProjectRepository projectRepository) {
        this.chatClient = chatClientBuilder.build();
        this.sectionRepository = sectionRepository;
        this.projectRepository = projectRepository;
    }

    public ScriptResponse createScript(Long id, Long sectionId, ScriptRequest request) {

        sectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Section not found with id: " + sectionId));

        String tone = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id))
                .getScriptTone();

        String scriptTone = (tone == null || tone.isBlank()) ? "default" : tone;

        return generateAI(scriptTone, request);

    }

    private ScriptResponse generateAI(String tone, ScriptRequest request) {
        String promptText = String.format(
                "다음 스크립트를 읽고, '%s'에 맞춰 문장을 자연스럽게 다듬고 필요한 보충 내용을 추가해줘.%n%n원본 스크립트:%n%s",
                tone,
                request.script()
        );

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.7)
                .build();

        Prompt prompt = new Prompt(promptText, options);

        ScriptResponse response = chatClient.prompt()
                .user(prompt.getContents())
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });

        return new ScriptResponse(Objects.requireNonNull(response).newscript());
    }
}
