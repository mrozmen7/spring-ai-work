package com.ozmenyavuz.ubs;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Demo data provider for branch hours.
 * In production, replace with a repository or external service call.
 */
@Service
public class BranchService {

    private final Map<String, String> hoursByCity = new LinkedHashMap<>();

    public BranchService() {
        // Demo hours — replace with real data source later
        hoursByCity.put("Zurich",   "Mon–Fri 09:00–17:00");
        hoursByCity.put("Geneva",   "Mon–Fri 09:30–17:30");
        hoursByCity.put("Basel",    "Mon–Fri 09:00–17:00");
        hoursByCity.put("Lausanne", "Mon–Fri 09:00–16:30");
        hoursByCity.put("Bern",     "Mon–Fri 09:00–17:00");
    }

    public String getHours(String city) {
        if (city == null || city.isBlank()) return "Not available";
        String key = city.substring(0, 1).toUpperCase(Locale.ROOT) + city.substring(1).toLowerCase(Locale.ROOT);
        return hoursByCity.getOrDefault(key, "Not available");
    }

    public Set<String> getSupportedCities() {
        return hoursByCity.keySet();
    }
}