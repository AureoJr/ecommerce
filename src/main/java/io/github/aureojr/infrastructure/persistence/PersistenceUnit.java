package io.github.aureojr.infrastructure.persistence;

import java.util.List;

/**
 * @author @aureojr
 * @since 30/12/16.
 */
public interface PersistenceUnit<T> {

    void save(Object values, Class entity);

    void update(Class entity, Object object);

    void delete(Class entity);

    T findById(Object entity);

    List<T> findAll(Class entity);

    List<T> find(Class entity, String where);
}
