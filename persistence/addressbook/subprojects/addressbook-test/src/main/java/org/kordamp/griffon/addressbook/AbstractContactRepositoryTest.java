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
package org.kordamp.griffon.addressbook;

import griffon.core.test.GriffonUnitRule;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public abstract class AbstractContactRepositoryTest {
    static {
        // System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        // System.setProperty("griffon.full.stacktrace", "true");
    }

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    @Inject private ContactRepository contactRepository;
    @Inject private ContactsDataProvider contactsDataProvider;

    @Test
    public void repositoryOperations() {
        // expect:
        assertThat(contactRepository.findAll(), empty());
        assertThat(contactRepository.findById(1L), nullValue());

        // given:
        List<Contact> contacts = contactsDataProvider.getContacts();
        Contact contact1 = contacts.get(0);
        Contact contact2 = contacts.get(1);

        // when:
        contactRepository.save(contact1);
        Collection<Contact> all = contactRepository.findAll();
        // then:
        assertThat(contact1.getId(), notNullValue());
        assertThat(all, contains(contact1));

        // when:
        contactRepository.save(contact2);
        all = contactRepository.findAll();
        // then:
        assertThat(contact2.getId(), notNullValue());
        assertThat(all, contains(contact1, contact2));
        // when:
        contactRepository.delete(contact2);
        all = contactRepository.findAll();
        // then:
        assertThat(all, contains(contact1));

        // when:
        contact1.setEmail("foo@acme.com");
        contactRepository.save(contact1);
        Contact other = contactRepository.findById(contact1.getId());
        assertThat(other.getEmail(), equalTo("foo@acme.com"));

        // given:
        assertThat(contactRepository.findAll().size(), equalTo(1));
        // when:
        contactRepository.clear();
        // then:
        assertThat(contactRepository.findAll().size(), equalTo(0));
    }
}
