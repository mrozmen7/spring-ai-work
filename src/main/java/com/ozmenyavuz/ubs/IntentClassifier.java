package com.ozmenyavuz.ubs;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IntentClassifier {

    // Minimal keyword set (extend as needed)
    private static final Set<String> BANKING_KEYWORDS = Set.of(
            "account", "balance", "transaction", "transfer", "branch", "hour", "open", "atm",
            "card", "debit", "credit", "loan", "mortgage", "iban", "swift", "interest",
            "fee", "statement", "wire", "payment", "exchange", "fx", "rate"
    );

    /**
     * Returns true if the message contains common banking keywords.
     */
    public boolean isBanking(String text) {
        if (text == null || text.isBlank()) return false;
        String lower = text.toLowerCase(Locale.ROOT);
        for (String kw : BANKING_KEYWORDS) {
            if (lower.contains(kw)) return true;
        }
        return false;
    }

    /**
     * Tries to detect a supported city name inside the free text.
     * Example supportedCities: ["Zurich","Geneva","Basel","Lausanne","Bern"]
     */
    public String detectCity(String text, Set<String> supportedCities) {
        if (text == null || supportedCities == null || supportedCities.isEmpty()) return null;

        // Build a case-insensitive regex like: \b(Zurich|Geneva|Basel|Lausanne|Bern)\b
        String alternation = String.join("|", supportedCities);
        Pattern cityPattern = Pattern.compile("\\b(" + alternation + ")\\b", Pattern.CASE_INSENSITIVE);

        Matcher m = cityPattern.matcher(text);
        if (m.find()) {
            return capitalize(m.group(1));
        }
        return null;
    }

    private String capitalize(String s) {
        if (s == null || s.isBlank()) return s;
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT);
    }
}