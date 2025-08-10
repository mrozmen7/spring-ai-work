package com.ozmenyavuz.multiModel;


import java.util.Set;

public record BankInfo(
        String name,
        String website,
        String hotline,
        Set<String> supportedCities
) {}