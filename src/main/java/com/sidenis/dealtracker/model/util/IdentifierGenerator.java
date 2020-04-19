package com.sidenis.dealtracker.model.util;

import java.util.UUID;

public final class IdentifierGenerator {
    private IdentifierGenerator() {
    }

    public static String createId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
