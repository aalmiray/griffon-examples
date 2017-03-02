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

import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

public class ExtListViewMatchers {
    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String SELECTOR_LIST_CELL = ".list-cell";

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Node> hasText(String value) {
        String descriptionText = "has text \"" + value + "\"";
        return typeSafeMatcher(ListView.class, descriptionText, node -> hasText(node, value));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean hasText(ListView listView,
                                   String value) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        NodeQuery nodeQuery = nodeFinder.from(listView);
        return nodeQuery.lookup(SELECTOR_LIST_CELL)
            .<Cell>match(cell -> hasCellText(cell, value))
            .tryQuery().isPresent();
    }

    private static boolean hasCellText(Cell cell,
                                       String value) {
        return !cell.isEmpty() && cell.getItem().toString().equals(value);
    }
}
