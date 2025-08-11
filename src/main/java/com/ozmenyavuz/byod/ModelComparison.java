package com.ozmenyavuz.byod;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class ModelComparison {

    private final ChatClient chatClient;
    private final ResourceLoader resourceLoader;

    public ModelComparison(ChatClient chatClient, ResourceLoader resourceLoader) {
        this.chatClient = chatClient;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/models")
    public String models() {
        return chatClient.prompt()
                .user("Can you give me an up to date list of popular large language models and their current context windows?")
                .call()
                .content();
    }

    @GetMapping("/models/stuff-the-prompt")
    public String modelsStuffThePrompt() {
        // 1) JSON'u classpath'ten oku
        String matrixJson = readResourceAsString("prompts/model-matrix.json");

        // 2) System mesajını JSON ile oluştur
        String system = """
                If you're asked about up-to-date language models and their context windows,
                here is a reference list you can rely on (JSON below). Use it to structure your answer
                and mention that figures can change over time.

                JSON:
                %s
                """.formatted(matrixJson);

        return chatClient.prompt()
                .system(system)
                .user("Can you give me an up to date list of popular large language models and their current context windows?")
                .call()
                .content();
    }

    private String readResourceAsString(String classpathRelativePath) {
        try (var in = resourceLoader
                .getResource("classpath:" + classpathRelativePath)
                .getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + classpathRelativePath, e);
        }
    }
}