package com.ozmenyavuz.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class
ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai")
    String generation(String userInput) {
        return this.chatClient.prompt()
                .user("Was ist das Bank?")
                .call()
                .content();
    }
    // Canli = stream
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream() {
        try {
            return chatClient.prompt()
                    .user("I am visiting Hilton Head soon, can you give me 10 places I must visit?")
                    .stream()
                    .content();
        } catch (Exception e) {
            e.printStackTrace(); // terminalde hatayı gör
            return Flux.error(new RuntimeException("Streaming failed: " + e.getMessage()));
        }
    }
    @GetMapping("/bankUbs")
    public ChatResponse banking() {
        return chatClient.prompt()
                .user("Give me brief information about UBS Bank")
                .call()
                .chatResponse();
    }

}
