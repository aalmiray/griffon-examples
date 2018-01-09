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
package org.kordamp.griffon.addressbook.jdbi;

import org.kordamp.griffon.addressbook.Contact;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactMapper implements ResultSetMapper<Contact> {
    @Override
    public Contact map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        Contact contact = Contact.builder()
            .name(r.getString("name"))
            .lastname(r.getString("lastname"))
            .address(r.getString("address"))
            .company(r.getString("company"))
            .email(r.getString("email"))
            .build();
        contact.setId(r.getLong("id"));
        return contact;
    }
}
