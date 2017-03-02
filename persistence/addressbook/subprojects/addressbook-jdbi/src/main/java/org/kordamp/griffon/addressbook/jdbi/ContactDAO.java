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
package org.kordamp.griffon.addressbook.jdbi;

import org.kordamp.griffon.addressbook.Contact;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(ContactMapper.class)
public interface ContactDAO {
    @SqlQuery("SELECT * FROM contacts WHERE id = :id")
    Contact findById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM contacts")
    List<Contact> list();

    @SqlUpdate("INSERT INTO contacts (name, lastname, address, company, email) VALUES (:name, :lastname, :address, :company, :email)")
    @GetGeneratedKeys
    long insert(@Bind("name") String name, @Bind("lastname") String lastname, @Bind("address") String address, @Bind("company") String company, @Bind("email") String email);

    @SqlUpdate("UPDATE contacts SET name = :name, lastname = :lastname, address = :address, company = :company, email = :email WHERE id = :id")
    void update(@Bind("id") Long id, @Bind("name") String name, @Bind("lastname") String lastname, @Bind("address") String address, @Bind("company") String company, @Bind("email") String email);

    @SqlUpdate("DELETE FROM contacts WHERE id = :id")
    void delete(@Bind("id") long id);

    void close();

    @SqlUpdate("DELETE FROM contacts")
    void clear();
}
