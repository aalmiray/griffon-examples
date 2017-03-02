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
package org.kordamp.griffon.addressbook.carbonado;

import com.amazon.carbonado.Automatic;
import com.amazon.carbonado.PrimaryKey;
import com.amazon.carbonado.Storable;
import org.kordamp.griffon.addressbook.Contact;

import javax.annotation.Nonnull;

@PrimaryKey("id")
public abstract class Contacts implements Storable {
    @Automatic
    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getLastname();

    public abstract void setLastname(String lastname);

    public abstract String getAddress();

    public abstract void setAddress(String address);

    public abstract String getCompany();

    public abstract void setCompany(String company);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public Contacts refreshWith(@Nonnull final Contact contact) {
        setName(contact.getName());
        setLastname(contact.getLastname());
        setAddress(contact.getAddress());
        setCompany(contact.getCompany());
        setEmail(contact.getEmail());
        return this;
    }

    @Nonnull
    public static Contacts fromContact(@Nonnull final Contacts entity, @Nonnull final Contact contact) {
        entity.refreshWith(contact);
        if (contact.getId() != null && entity.getId() == null) {
            entity.setId(contact.getId());
        }
        return entity;
    }

    @Nonnull
    public static Contact toContact(@Nonnull final Contacts entity) {
        Contact contact = Contact.builder()
            .name(entity.getName())
            .lastname(entity.getLastname())
            .address(entity.getAddress())
            .company(entity.getCompany())
            .email(entity.getEmail())
            .build();
        contact.setId(entity.getId());
        return contact;
    }
}
