package io.github.aureojr.infrastructure.persistence;

import java.util.List;

/**
 * @author @aureojr
 * @since 30/12/16.
 */
public interface PersistenceUnit<T> {

    void save(Class entity);

    void delete(Class entity);

    T findById(Class entity);

    List<T> findAll(Class entity);

}
