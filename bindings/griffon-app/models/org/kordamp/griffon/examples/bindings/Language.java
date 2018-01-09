/*
 * Copyright 2016-2018 Andres Almiray
 *
 * This file is part of Griffon Examples
 *
 * Griffon Examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Griffon Examples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Griffon Examples. If not, see <http://www.gnu.org/licenses/>.
 */
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
