package com.epam.jwd.cafe.dao;

import java.util.List;

public interface Dao<T> {

    void create(T entity);

    T update(T entity);

    void deleteById(int id);

    List<T> findById(int id);

}
