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
package org.kordamp.griffon.addressbook.mybatis;

import griffon.metadata.TypeProviderFor;
import griffon.plugins.mybatis.MybatisMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.kordamp.griffon.addressbook.Contact;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@TypeProviderFor(MybatisMapper.class)
public interface ContactMapper extends MybatisMapper {
    @Nullable
    @Select("SELECT * FROM contacts WHERE id = #{id}")
    Contact findById(Long id);

    @Insert("INSERT INTO contacts (name, lastname, address, company, email) VALUES (#{name}, #{lastname}, #{address}, #{company}, #{email})")
    @SelectKey(statement = "call identity()", keyProperty = "id", before = false, resultType = long.class)
    int insert(Contact contact);

    @Update("UPDATE contacts SET name = #{name}, lastname = #{lastname}, address = #{address}, company = #{company}, email = #{email} WHERE id = #{id}")
    int update(Contact contact);

    @Delete("DELETE FROM contacts WHERE id = #{id}")
    int delete(Contact contact);

    @Nonnull
    @Select("SELECT * FROM contacts")
    List<Contact> list();

    @Delete("DELETE FROM contacts")
    void clear();
}
