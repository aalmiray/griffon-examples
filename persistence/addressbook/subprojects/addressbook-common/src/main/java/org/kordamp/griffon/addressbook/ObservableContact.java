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
package org.kordamp.griffon.addressbook;

import griffon.javafx.beans.property.ResetableStringProperty;
import griffon.javafx.collections.ElementObservableList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;

import javax.annotation.Nonnull;

import static griffon.util.GriffonNameUtils.isBlank;
import static java.util.Objects.requireNonNull;

public class ObservableContact implements ElementObservableList.PropertyContainer {
    private final ResetableStringProperty name = new ResetableStringProperty(this, "name");
    private final ResetableStringProperty lastname = new ResetableStringProperty(this, "lastname");
    private final ResetableStringProperty address = new ResetableStringProperty(this, "address");
    private final ResetableStringProperty company = new ResetableStringProperty(this, "company");
    private final ResetableStringProperty email = new ResetableStringProperty(this, "email");
    private final ObjectProperty<Contact> contact = new SimpleObjectProperty<>(this, "contact", new Contact());
    private final MonadicBinding<Boolean> valid;
    private final MonadicBinding<Boolean> dirty;
    private final MonadicBinding<Boolean> ready;

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

        dirty = EasyBind.combine(
            name.dirtyProperty(),
            lastname.dirtyProperty(),
            address.dirtyProperty(),
            company.dirtyProperty(),
            email.dirtyProperty(),
            (a, b, c, d, e) -> a || b || c || d || e
        );

        ready = EasyBind.combine(valid, dirty, (v, d) -> v && d);
    }

    public ObservableContact(@Nonnull final Contact contact) {
        this();
        setContact(requireNonNull(contact, "Argument 'contact' must not be null"));
    }

    @Nonnull
    public StringProperty nameProperty() {
        return name.valueStringProperty();
    }

    @Nonnull
    public StringProperty lastnameProperty() {
        return lastname.valueStringProperty();
    }

    @Nonnull
    public StringProperty addressProperty() {
        return address.valueStringProperty();
    }

    @Nonnull
    public StringProperty companyProperty() {
        return company.valueStringProperty();
    }

    @Nonnull
    public StringProperty emailProperty() {
        return email.valueStringProperty();
    }

    @Nonnull
    public MonadicBinding<Boolean> readyProperty() {
        return ready;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getLastname() {
        return lastname.getValue();
    }

    public void setLastname(String lastname) {
        this.lastname.setValue(lastname);
    }

    public String getAddress() {
        return address.getValue();
    }

    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public String getCompany() {
        return company.getValue();
    }

    public void setCompany(String company) {
        this.company.setValue(company);
    }

    public String getEmail() {
        return email.getValue();
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public Contact getContact() {
        return contact.get();
    }

    public void setContact(Contact contact) {
        this.contact.set(contact);
        rebase();
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
        rebase();
    }

    public void updateWith(@Nonnull ObservableContact other) {
        setName(other.getName());
        setLastname(other.getLastname());
        setAddress(other.getAddress());
        setCompany(other.getCompany());
        setEmail(other.getEmail());
        rebase();
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

    private void rebase() {
        name.rebase();
        lastname.rebase();
        address.rebase();
        company.rebase();
        email.rebase();
    }
}
