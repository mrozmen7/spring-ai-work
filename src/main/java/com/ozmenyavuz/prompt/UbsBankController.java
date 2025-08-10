package com.ozmenyavuz.prompt;

import com.ozmenyavuz.ubs.BranchService;
import com.ozmenyavuz.ubs.IntentClassifier;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ubs")
public class UbsBankController {

    private final ChatClient chatClient;
    private final IntentClassifier intent;
    private final BranchService branchService;

    public UbsBankController(ChatClient.Builder builder,
                             IntentClassifier intent,
                             BranchService branchService) {
        this.chatClient = builder.build();   // default model/options from config
        this.intent = intent;
        this.branchService = branchService;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam String message,
                       @RequestParam(required = false) String city) {

        // 0) Guardrails (soft): modelin rolü ve sınırları
        final String systemGuardrails = """
            You are a virtual assistant for UBS Switzerland.
            You can ONLY discuss:
            - Account balances and transactions
            - Branch locations and hours
            - General banking services

            If asked about anything else, respond: "I can only help with banking-related questions."
            """;

        // 1) Hard filter: bankacılık dışı ise LLM'e bile gitme
        if (!intent.isBanking(message)) {
            return "I can only help with banking-related questions.";
        }

        // 2) Whitelist “tool”: şube saatleri gibi izinli veriler için doğrudan servis çağrısı
        String lower = message.toLowerCase();
        boolean asksBranchHours = lower.contains("branch") && (lower.contains("hour") || lower.contains("open"));
        if (asksBranchHours) {
            String chosenCity = city != null && !city.isBlank()
                    ? city
                    : intent.detectCity(message, branchService.getSupportedCities());

            if (chosenCity == null) {
                return "Please specify a city (e.g., /ubs/chat?message=branch%20hours&city=Zurich).";
            }

            String hours = branchService.getHours(chosenCity);
            return chosenCity + " branch hours: " + hours;
        }

        // 3) Genel bankacılık içerikleri: Guardrails + LLM
        return chatClient
                .prompt()
                .system(systemGuardrails)
                .user(message)
                .call()
                .content();
    }
}