/*
 * Copyright 20162017 Andres Almiray
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
package org.kordamp.griffon.addressbook.gsql;

import griffon.util.CollectionUtils;
import org.kordamp.griffon.addressbook.Contact;

import javax.annotation.Nonnull;
import java.util.Map;

public final class ContactMapper {
    private ContactMapper() {
        // prevent instantiation
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(@Nonnull final Contact contact) {
        Map<String, Object> map = CollectionUtils.newMap(
            "name", contact.getName(),
            "lastname", contact.getLastname(),
            "address", contact.getAddress(),
            "company", contact.getCompany(),
            "email", contact.getEmail()
        );
        if (!contact.isNotPersisted()) {
            map.put("id", contact.getId());
        }
        return map;
    }

    @Nonnull
    public static Contact fromMap(@Nonnull Map<String, Object> map) {
        Contact contact = Contact.builder()
            .name(String.valueOf(map.get("name")))
            .lastname(String.valueOf(map.get("lastname")))
            .address(String.valueOf(map.get("address")))
            .company(String.valueOf(map.get("company")))
            .email(String.valueOf(map.get("email")))
            .build();
        contact.setId(((Number) map.get("id")).longValue());
        return contact;
    }
}
