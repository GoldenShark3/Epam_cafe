package com.epam.jwd.cafe.dao;

import com.epam.jwd.cafe.dao.field.EntityField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.BaseEntity;
import java.util.List;

public interface Dao<T extends BaseEntity> {

    void create(T entity) throws DaoException;

    T update(T entity) throws DaoException;

    void deleteById(int id) throws DaoException;

    List<T> findByField(String searchableField, EntityField<T> nameOfField) throws DaoException;

    List<T> findAll() throws DaoException;
}
