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
package org.kordamp.griffon.addressbook.gsql;

import griffon.plugins.gsql.GsqlCallback;
import griffon.plugins.gsql.GsqlHandler;
import groovy.sql.DataSet;
import groovy.sql.GroovyRowResult;
import groovy.sql.Sql;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class GsqlContactRepository implements ContactRepository {
    @Inject
    private GsqlHandler gsqlHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return gsqlHandler.withSql((datasourceName, sql) -> {
            try {
                GroovyRowResult row = sql.firstRow("SELECT * FROM contacts WHERE id = ?", asList(id));
                if (row != null && !row.isEmpty()) {
                    return ContactMapper.fromMap(row);
                }
                return null;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
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
        gsqlHandler.withSql((datasourceName, sql) -> {
            DataSet dataSet = sql.dataSet("contacts");
            Map<String, Object> params = ContactMapper.toMap(contact);
            try {
                List<List<Object>> result = dataSet.executeInsert(params, "INSERT INTO contacts (name, lastname, address, company, email) VALUES (:name, :lastname, :address, :company, :email)");
                contact.setId(((Number) result.get(0).get(0)).longValue());
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        gsqlHandler.withSql((datasourceName, sql) -> {
            DataSet dataSet = sql.dataSet("contacts");
            Map<String, Object> params = ContactMapper.toMap(contact);
            try {
                dataSet.executeUpdate(params, "UPDATE contacts SET name = :name, lastname = :lastname, address = :address, company = :company, email = :email WHERE id = :id");
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        gsqlHandler.withSql((datasourceName, sql) -> {
            DataSet dataSet = sql.dataSet("contacts");
            try {
                dataSet.execute("DELETE FROM contacts WHERE id = ?", new Object[]{contact.getId()});
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Contact> findAll() {
        return gsqlHandler.withSql((datasourceName, sql) -> {
            try {
                DataSet dataSet = sql.dataSet("contacts");
                return (Collection<Contact>) dataSet.rows().stream()
                    .map(rs -> ContactMapper.fromMap((Map<String, Object>) rs))
                    .collect(Collectors.toList());
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public void clear() {
        gsqlHandler.withSql(new GsqlCallback<Void>() {
            @Override
            public Void handle(@Nonnull String datasourceName, @Nonnull Sql sql) {
                try {
                    sql.execute("DELETE FROM contacts");
                } catch (SQLException e) {
                    throw new IllegalStateException(e);
                }
                return null;
            }
        });
    }
}
