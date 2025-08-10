package com.ozmenyavuz.multiModel;

import com.ozmenyavuz.ubs.BranchService;
import com.ozmenyavuz.ubs.IntentClassifier;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ozmenbank")
public class OzmenBankController {

    private final ChatClient chatClient;
    private final IntentClassifier intent;
    private final BranchService branchService;

    public OzmenBankController(ChatClient.Builder builder,
                               IntentClassifier intent,
                               BranchService branchService) {
        this.chatClient = builder.build();   // config'teki varsayılan model/opsiyonlarla ChatClient
        this.intent = intent;
        this.branchService = branchService;
    }

    // Marka bilgisi
    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public BankInfo info() {
        return new BankInfo(
                "OZMENBANK",
                "https://www.ozmenbank.example",
                "+41 800 000 000",
                branchService.getSupportedCities()
        );
    }

    // Marka guardrail'leri (kalıcı kurallar)
    private static final String SYSTEM_GUARDRAILS = """
        You are a virtual assistant for OZMENBANK.
        You can ONLY discuss:
        - Account balances and transactions
        - Branch locations and hours
        - General banking services

        If asked about anything else, respond in Turkish:
        "I can only help with banking-related questions."
        """;

    // Ana sohbet uç noktası
    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam String message,
                       @RequestParam(required = false) String city) {

        // 1) Sert filtre: bankacılık dışını hiç LLM'e göndermiyoruz
        if (!intent.isBanking(message)) {
            return "I can only assist with banking questions.";
        }

        // 2) Whitelist 'tool': şube saatleri
        String lower = message.toLowerCase();
        boolean asksBranchHours = lower.contains("branch") || lower.contains("şube");
        boolean hoursHint = lower.contains("hour") || lower.contains("saat") || lower.contains("açık");

        if (asksBranchHours && hoursHint) {
            String chosenCity = (city != null && !city.isBlank())
                    ? city
                    : intent.detectCity(message, branchService.getSupportedCities());

            if (chosenCity == null) {
                return "Please specify a city (e.g., /ozmenbank/chat?message=branch%20hours&city=Zurich).";            }
            String hours = branchService.getHours(chosenCity);
            return chosenCity + " Branch operating hours: " + hours;
        }

        // 3) Genel bankacılık: guardrails + LLM
        return chatClient
                .prompt()
                .system(SYSTEM_GUARDRAILS)
                .user(message)
                .call()
                .content();
    }
}