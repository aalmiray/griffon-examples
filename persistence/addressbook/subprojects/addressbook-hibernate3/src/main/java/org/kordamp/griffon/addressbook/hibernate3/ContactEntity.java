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
package org.kordamp.griffon.addressbook.hibernate3;

import griffon.metadata.TypeProviderFor;
import griffon.plugins.hibernate3.Hibernate3Mapping;
import org.kordamp.griffon.addressbook.Contact;

import javax.annotation.Nonnull;
import java.io.Serializable;

@TypeProviderFor(Hibernate3Mapping.class)
public class ContactEntity implements Serializable, Hibernate3Mapping {
    private Long id;
    private String name;
    private String lastname;
    private String address;
    private String company;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ContactEntity refreshWith(@Nonnull final Contact contact) {
        setName(contact.getName());
        setLastname(contact.getLastname());
        setAddress(contact.getAddress());
        setCompany(contact.getCompany());
        setEmail(contact.getEmail());
        return this;
    }

    @Nonnull
    public static ContactEntity fromContact(@Nonnull final Contact contact) {
        ContactEntity entity = new ContactEntity();
        entity.refreshWith(contact);
        entity.setId(contact.getId());
        return entity;
    }

    @Nonnull
    public static Contact toContact(@Nonnull final ContactEntity entity) {
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