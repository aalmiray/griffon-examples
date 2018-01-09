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

import griffon.plugins.glazedlists.javafx.PropertyContainer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;

import javax.annotation.Nonnull;

import static griffon.util.GriffonNameUtils.isBlank;
import static java.util.Objects.requireNonNull;

public class ObservableContact implements PropertyContainer {
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final StringProperty lastname = new SimpleStringProperty(this, "lastname");
    private final StringProperty address = new SimpleStringProperty(this, "address");
    private final StringProperty company = new SimpleStringProperty(this, "company");
    private final StringProperty email = new SimpleStringProperty(this, "email");
    private final ObjectProperty<Contact> contact = new SimpleObjectProperty<>(this, "contact", new Contact());
    private final MonadicBinding<Boolean> valid;

    public ObservableContact() {
        contact.addListener((v, o, n) -> {
            setName(n != null ? n.getName() : "");
            setLastname(n != null ? n.getLastname() : "");
            setAddress(n != null ? n.getAddress() : "");
            setCompany(n != null ? n.getCompany() : "");
            setEmail(n != null ? n.getEmail() : "");
        });

        valid = EasyBind.combine(
            nameProperty(),
            lastnameProperty(),
            addressProperty(),
            companyProperty(),
            emailProperty(),
            (a, b, c, d, e) -> !isBlank(a) && !isBlank(b) && !isBlank(c) && !isBlank(d) && !isBlank(e)
        );
    }

    public ObservableContact(@Nonnull Contact contact) {
        this();
        setContact(requireNonNull(contact, "Argument 'contact' must not be null"));
    }

    @Nonnull
    public StringProperty nameProperty() {
        return name;
    }

    @Nonnull
    public StringProperty lastnameProperty() {
        return lastname;
    }

    @Nonnull
    public StringProperty addressProperty() {
        return address;
    }

    @Nonnull
    public StringProperty companyProperty() {
        return company;
    }

    @Nonnull
    public StringProperty emailProperty() {
        return email;
    }

    @Nonnull
    public MonadicBinding<Boolean> validProperty() {
        return valid;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLastname() {
        return lastname.get();
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getCompany() {
        return company.get();
    }

    public void setCompany(String company) {
        this.company.set(company);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public Contact getContact() {
        return contact.get();
    }

    public void setContact(Contact contact) {
        this.contact.set(contact);
    }

    @Override
    public String toString() {
        return getName() + " " + getLastname();
    }

    public void updateContact() {
        Contact contact = getContact();
        contact.setName(getName());
        contact.setLastname(getLastname());
        contact.setAddress(getAddress());
        contact.setCompany(getCompany());
        contact.setEmail(getEmail());
    }

    @Nonnull
    @Override
    public Property<?>[] properties() {
        return new Property<?>[]{
            nameProperty(),
            lastnameProperty(),
            addressProperty(),
            companyProperty(),
            emailProperty()
        };
    }
}
