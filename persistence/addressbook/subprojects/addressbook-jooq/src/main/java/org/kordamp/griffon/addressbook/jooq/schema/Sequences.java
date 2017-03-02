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
/**
 * This class is generated by jOOQ
 */
package org.kordamp.griffon.addressbook.jooq.schema;


import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;

import javax.annotation.Generated;


/**
 * Convenience access to all sequences in PUBLIC
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Sequences {

    /**
     * The sequence <code>PUBLIC.SYSTEM_SEQUENCE_A678873C_A88E_4C9C_BF04_50D54A5BAFA1</code>
     */
    public static final Sequence<Long> SYSTEM_SEQUENCE_A678873C_A88E_4C9C_BF04_50D54A5BAFA1 = new SequenceImpl<Long>("SYSTEM_SEQUENCE_A678873C_A88E_4C9C_BF04_50D54A5BAFA1", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT);
}
