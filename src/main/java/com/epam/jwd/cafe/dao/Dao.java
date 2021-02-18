package com.epam.jwd.cafe.dao;

import com.epam.jwd.cafe.model.BaseEntity;
import java.util.List;

public interface Dao<T extends BaseEntity> {

    void create(T entity);

    T update(T entity);

    void deleteById(int id);

    List<T> findAll();

}
