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
import griffon.util.AbstractMapResourceBundle;

import javax.annotation.Nonnull;
import java.util.Map;

import static griffon.util.CollectionUtils.map;
import static java.util.Arrays.asList;

public class Config extends AbstractMapResourceBundle {
    @Override
    protected void initialize(@Nonnull Map<String, Object> entries) {
        map(entries)
            .e("application", map()
                .e("title", "Addressbook")
                .e("startupGroups", asList("addressbook"))
                .e("autoShutdown", true)
            )
            .e("mvcGroups", map()
                .e("addressbook", map()
                    .e("model", "org.kordamp.griffon.addressbook.AddressbookModel")
                    .e("view", "org.kordamp.griffon.addressbook.AddressbookView")
                    .e("controller", "org.kordamp.griffon.addressbook.AddressbookController")
                )
            );
    }
}