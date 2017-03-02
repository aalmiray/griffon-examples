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
package org.kordamp.griffon.addressbook;

import griffon.javafx.test.FunctionalJavaFXRunner;
import griffon.javafx.test.GriffonTestFXClassRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import javax.inject.Inject;
import java.util.List;

import static org.testfx.api.FxAssert.verifyThat;

@RunWith(FunctionalJavaFXRunner.class)
public abstract class AbstractFunctionalTest {
    @ClassRule
    public static GriffonTestFXClassRule testfx = new GriffonTestFXClassRule("mainWindow");

    @Inject
    private ContactsDataProvider contactsDataProvider;

    private static final String NAME = "name";
    private static final String LASTNAME = "lastname";
    private static final String ADDRESS = "address";
    private static final String COMPANY = "company";
    private static final String email = "email";

    @Test
    public void _01_sanity_check() throws Exception {
        testfx.clickOn("#contacts");

        List<Contact> contacts = contactsDataProvider.getContacts();
        verifyThat("#contacts", ListViewMatchers.hasItems(contacts.size()));
        for (Contact contact : contacts) {
            ObservableContact value = new ObservableContact(contact);
            verifyThat("#contacts", ExtListViewMatchers.hasText(value.toString()));
        }

        verifyThat("#newbutton", NodeMatchers.isEnabled());
        verifyThat("#savebutton", NodeMatchers.isDisabled());
        verifyThat("#deletebutton", NodeMatchers.isDisabled());
        verifyThat("#printbutton", NodeMatchers.isEnabled());
    }

    @Test
    public void _02_select_a_contact_from_the_list() {
        List<Contact> contacts = contactsDataProvider.getContacts();
        Contact contact = contacts.get(0);
        testfx.doubleClickOn(contact.asString());

        verifyThat("#name", TextInputControlMatchers.hasText(contact.getName()));
        verifyThat("#lastname", TextInputControlMatchers.hasText(contact.getLastname()));
        verifyThat("#address", TextInputControlMatchers.hasText(contact.getAddress()));
        verifyThat("#company", TextInputControlMatchers.hasText(contact.getCompany()));
        verifyThat("#email", TextInputControlMatchers.hasText(contact.getEmail()));
    }

    @Test
    public void _04_add_a_contact() {
        testfx.clickOn("#newbutton");

        testfx.clickOn("#name").write(NAME + 0);
        testfx.clickOn("#lastname").write(LASTNAME + 0);
        testfx.clickOn("#address").write(ADDRESS + 0);
        testfx.clickOn("#company").write(COMPANY + 0);
        testfx.clickOn("#email").write(email + 0);

        testfx.clickOn("#savebutton");

        verifyThat("#contacts", ListViewMatchers.hasItems(3));
        verifyThat("#contacts", ExtListViewMatchers.hasText(NAME + 0 + " " + LASTNAME + 0));
    }

    @Test
    public void _05_edit_a_contact() {
        testfx.clickOn("#name").eraseText(1).write("1");

        testfx.clickOn("#savebutton");

        verifyThat("#contacts", ListViewMatchers.hasItems(3));
        verifyThat("#contacts", ExtListViewMatchers.hasText(NAME + 1 + " " + LASTNAME + 0));
    }

    @Test
    public void _06_delete_a_contact() {
        List<Contact> contacts = contactsDataProvider.getContacts();
        testfx.doubleClickOn(contacts.get(0).asString());

        testfx.doubleClickOn(NAME + 1 + " " + LASTNAME + 0);

        testfx.clickOn("#deletebutton");

        verifyThat("#contacts", ListViewMatchers.hasItems(contacts.size()));
        for (Contact contact : contacts) {
            ObservableContact value = new ObservableContact(contact);
            verifyThat("#contacts", ExtListViewMatchers.hasText(value.toString()));
        }
    }
}
