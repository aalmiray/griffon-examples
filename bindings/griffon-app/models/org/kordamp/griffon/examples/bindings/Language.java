package org.kordamp.griffon.examples.bindings;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum Language {
    ENGLISH("en"),
    SPANISH("es"),
    GERMAN("de"),
    UNKNOWN("en");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    @Nonnull
    public String getCode() {
        return code;
    }

    @Nonnull
    public static Language fromLocale(@Nonnull Locale locale) {
        if (locale == null) {
            return ENGLISH;
        }

        switch (locale.getLanguage().toLowerCase()) {
            case "en": return ENGLISH;
            case "es": return SPANISH;
            case "de": return GERMAN;
        }
        return ENGLISH;
    }
}
