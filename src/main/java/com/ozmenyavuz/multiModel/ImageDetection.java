package com.ozmenyavuz.multiModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ImageDetection {

    private final ChatClient chatClient;
    @Value("classpath:/images/bank.jpeg")
    Resource sampleImage;

    public ImageDetection(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/image-to-text")
    public String imageToText() {
        log.info("Image is being processed: {}", sampleImage.getFilename());

        String result = chatClient.prompt()
                .user(p -> {
                    p.text("Can you please describe what you see in the following image?");
                    p.media(MimeTypeUtils.IMAGE_JPEG, sampleImage);
                })
                .call()
                .content();

        log.info("Model answer: {}", result);
        return result;
    }
}

