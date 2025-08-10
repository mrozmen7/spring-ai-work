package com.ozmenyavuz.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvestmentController {

    private final ChatClient chatClient;

    public InvestmentController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/vacation/unstructured")
    String unstructured() {
        return chatClient.prompt()
                .user("I want to plan a trip to Turkey. Give me a list of things to do?")
                .call()
                .content();
    }

    @GetMapping("/vacation/structured")
    public Itinerary structured() {
        return chatClient.prompt()
                .user(""" 
                        Create a JSON array of 2 investment recommendations for a Swiss private banking client.
                        
                        Each item should contain:
                        - investmentType (e.g. 'ETF', 'Bond', 'Real Estate')
                        - region (e.g. 'Europe', 'Asia-Pacific')
                        - riskLevel ('Low', 'Medium', 'High')
                                - expectedReturn (e.g. '4%', '7.5%')
                        
                        Make sure the data is realistic and relevant to 2025 trends.
                        """)
                .call()
                .entity(Itinerary.class);
    }
}
