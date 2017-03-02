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
package org.kordamp.griffon.addressbook.springdata;

import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;
import org.kordamp.griffon.addressbook.springdata.beans.ContactEntity;
import org.kordamp.griffon.addressbook.springdata.beans.ContactEntityJpaRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class SpringDataContactRepository implements ContactRepository {
    @Inject
    private ContactEntityJpaRepository repository;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        ContactEntity entity = repository.findOne(id);
        return entity == null ? null : ContactEntity.toContact(entity);
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
        ContactEntity entity = ContactEntity.fromContact(contact);
        repository.save(entity);
        contact.setId(entity.getId());
        repository.flush();
    }

    private void update(@Nonnull final Contact contact) {
        ContactEntity entity = repository.findOne(contact.getId());
        entity.refreshWith(contact);
        repository.save(entity);
        repository.flush();
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        repository.delete(contact.getId());
        repository.flush();
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public Collection<Contact> findAll() {
        return repository.findAll().stream()
            .map(ContactEntity::toContact)
            .collect(toList());
    }

    @Override
    public void clear() {
        repository.deleteAll();
    }
}
