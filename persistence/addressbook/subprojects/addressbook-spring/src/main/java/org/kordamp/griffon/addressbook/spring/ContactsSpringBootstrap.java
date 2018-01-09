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
package org.kordamp.griffon.addressbook.spring;

import griffon.core.GriffonApplication;
import griffon.core.event.EventHandler;
import org.kordamp.griffon.addressbook.ContactsDataProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.PreparedStatement;

@Named("contacts")
public class ContactsSpringBootstrap implements EventHandler {
    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
    private ContactsDataProvider contactsDataProvider;

    public void onStartupStart(@Nonnull GriffonApplication application) {
        contactsDataProvider.getContacts().forEach(contact -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO contacts (name, lastname, address, company, email) VALUES (?, ?, ?, ?, ?)", new String[]{"id"});
                ps.setString(1, contact.getName());
                ps.setString(2, contact.getLastname());
                ps.setString(3, contact.getAddress());
                ps.setString(4, contact.getCompany());
                ps.setString(5, contact.getEmail());
                return ps;
            }, keyHolder);
            contact.setId(keyHolder.getKey().longValue());
        });
    }
}