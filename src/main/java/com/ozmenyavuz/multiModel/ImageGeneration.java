package com.ozmenyavuz.multiModel;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageGeneration {

    private final OpenAiImageModel imageModel;

    @GetMapping("/generate")
    public ResponseEntity<Map<String, String>> generateImage(
            @RequestParam(defaultValue = "A zeppelin flying over the lake") String prompt) {
//        A zeppelin flying over the lake

        log.info("🖼️ Generating image for prompt: {}", prompt);

        ImageOptions options = OpenAiImageOptions.builder()
                .model("dall-e-3")
                .width(1024)
                .height(1024)
                .quality("hd")
                .style("natural")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
        ImageResponse imageResponse = imageModel.call(imagePrompt);

        String url = imageResponse.getResult()
                .getOutput()
                .getUrl();

        log.info("✅ Image generated: {}", url);

        return ResponseEntity.ok(Map.of(
                "prompt", prompt,
                "imageUrl", url
        ));
    }
}
