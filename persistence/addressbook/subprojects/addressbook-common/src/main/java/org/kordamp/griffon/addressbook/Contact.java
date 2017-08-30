/*
 * Copyright 2016-2017 Andres Almiray
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
package org.kordamp.griffon.addressbook;

import lombok.Builder;
import lombok.Data;

import javax.annotation.Nonnull;
import java.io.Serializable;

@Data
public class Contact implements Serializable {
    private static final long serialVersionUID = 5068269804165649560L;

    private Long id;
    private String name;
    private String lastname;
    private String address;
    private String company;
    private String email;

    @Builder
    public static Contact create(@Nonnull String name, @Nonnull String lastname, @Nonnull String address, @Nonnull String company, @Nonnull String email) {
        Contact c = new Contact();
        c.setName(name);
        c.setLastname(lastname);
        c.setAddress(address);
        c.setCompany(company);
        c.setEmail(email);
        return c;
    }

    public boolean isNotPersisted() {
        return null == id;
    }

    public String asString() {
        return name + " " + lastname;
    }
}
