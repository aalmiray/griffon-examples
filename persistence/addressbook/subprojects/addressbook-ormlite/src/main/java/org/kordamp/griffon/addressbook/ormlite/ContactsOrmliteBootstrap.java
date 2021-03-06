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
package org.kordamp.griffon.addressbook.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import griffon.plugins.ormlite.OrmliteBootstrap;
import griffon.plugins.ormlite.exceptions.RuntimeSQLException;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactsDataProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;

@Named("contacts")
public class ContactsOrmliteBootstrap implements OrmliteBootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;

    @Override
    public void init(@Nonnull String sessionFactoryName, @Nonnull final ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, ContactEntity.class);
            Dao<ContactEntity, Integer> contactDao = DaoManager.createDao(connectionSource, ContactEntity.class);
            for (Contact contact : contactsDataProvider.getContacts()) {
                contactDao.create(ContactEntity.fromContact(contact));
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(sessionFactoryName, e);
        }
    }

    @Override
    public void destroy(@Nonnull String sessionFactoryName, @Nonnull ConnectionSource connectionSource) {

    }
}