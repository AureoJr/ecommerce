package io.github.aureojr.infrastructure.persistence;

import java.util.List;

/**
 * @author @aureojr
 * @since 30/12/16.
 */
public interface PersistenceUnit<T> {

    void save(T entity);

    void delete(T entity);

    T findById(T entity);

    List<T> findAll(T entity);

}
