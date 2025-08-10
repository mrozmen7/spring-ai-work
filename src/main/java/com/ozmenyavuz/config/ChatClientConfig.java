package com.ozmenyavuz.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {
        return ChatClient.create(model);
    }
    /*
    @Bean
    public ChatClient chatClient(GeminiChatModel model) {
        return ChatClient.create(model);
    }
    */
}