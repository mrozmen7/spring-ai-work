package com.ozmenyavuz.prompt;

import com.ozmenyavuz.chat.ChatController;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ArticleController {

    private final ChatClient chatClient;

    public ArticleController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @GetMapping(value = "/post/new", produces = MediaType.TEXT_MARKDOWN_VALUE)
    public String newPost(@RequestParam(
            value = "topic", defaultValue = "Sustainable Finance in 2025") String topic) {

        // 1) Kalıcı kurallar (rol + güvenilirlik)
        String system = """
                You are a senior financial content writer for a tier-1 bank.
                Be factual, concise, and avoid fabrications. If a claim is uncertain, say so.
                """;

        // 2) Kullanıcı şablonu: görev, yapı, kısıtlar, ÇIKTI net
        String userTemplate = """
                Blog Post Generator Guidelines (Finance Edition)
                
                Topic: {topic}
                
                1. Length & Purpose
                - ~500 words for a financial audience.
                - Educate readers on the selected banking/finance topic.
                
                2. Structure
                - Title (one line)
                - Introduction: why it matters to financial services
                - Body: 3 key insights with real-world banking examples/data/cases
                - Conclusion: actionable takeaways (e.g., contact a UBS advisor, explore UBS insights)
                
                3. Content Requirements
                - Reference current market trends/regulations/innovations when relevant
                - Prefer trustworthy statistics/sources (add 2 short references at the end)
                - Keep it understandable to both finance professionals and interested clients
                
                4. Tone & Style
                - Professional, reader-friendly, trustworthy (UBS-level communication)
                - Use subheadings and short paragraphs
                
                5. Output
                - Deliver a complete, ready-to-publish Markdown article
                - Put the suggested title at the top
                """;

        return chatClient
                .prompt()
                .system(system)
                .user(p -> p.text(userTemplate)
                        .param("topic", topic))
                .call()
                .content();

    }
}
