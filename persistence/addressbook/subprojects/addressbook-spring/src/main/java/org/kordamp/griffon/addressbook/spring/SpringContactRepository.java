/*
 * Copyright 2016-2017-2017 Andres Almiray
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

import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.util.Collection;

public class SpringContactRepository implements ContactRepository {
    @Inject
    private JdbcTemplate jdbcTemplate;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return jdbcTemplate.query("SELECT * FROM contacts WHERE id = ?", new Object[]{id}, ROW_MAPPER).stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void save(@Nonnull final Contact contact) {
        if (contact.isNotPersisted()) {
            insert(contact);
        } else {
            update(contact);
        }
    }

    private void insert(@Nonnull final Contact contact) {
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
    }

    private void update(@Nonnull final Contact contact) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("UPDATE contacts SET name = ?, lastname = ?, address = ?, company = ?, email = ? WHERE id = ?");
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getLastname());
            ps.setString(3, contact.getAddress());
            ps.setString(4, contact.getCompany());
            ps.setString(5, contact.getEmail());
            ps.setLong(6, contact.getId());
            return ps;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        jdbcTemplate.update("DELETE FROM contacts WHERE id = ?", contact.getId());
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public Collection<Contact> findAll() {
        return jdbcTemplate.query("SELECT * FROM contacts", ROW_MAPPER);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM contacts");
    }

    private static final RowMapper<Contact> ROW_MAPPER = (rs, i) -> {
        Contact contact = Contact.builder()
            .name(rs.getString("name"))
            .lastname(rs.getString("lastname"))
            .address(rs.getString("address"))
            .company(rs.getString("company"))
            .email(rs.getString("email"))
            .build();
        contact.setId(rs.getLong("id"));
        return contact;
    };
}
