package io.github.aureojr.infrastructure.persistence;

import io.github.aureojr.infrastructure.database.annotations.DefaultModel;
import io.github.aureojr.infrastructure.database.annotations.ID;
import io.github.aureojr.infrastructure.database.annotations.TransientField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author @aureojr
 * @since 30/12/16.
 */
@Component("persistenceUnit")
public class PersistenceUnitImpl<T> implements PersistenceUnit<T> {

    private static final String INSERT = "INSERT INTO $table ($fields) VALUES ($values)";
    private static final String UPDATE = "UPDATE $table SET $values $where";
    private static final String DELETE = "DELETE FROM $table WHERE $where";
    private static final String SELECT = "SELECT $fields FROM $table WHERE $where";
    private static final String TABLE = "$table";
    private static final String FIELDS= "$fields";
    private static final String VALUES= "$values";
    private static final String WHERE = "$where";
    private static final Logger LOG = Logger.getLogger(PersistenceUnitImpl.class.getName());

    private static Connection conn;

    private int count =0;

    public PersistenceUnitImpl() {
        if(conn == null){
            try {
                conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ecommerce","root","alfred2016");
            } catch (SQLException e) {
                LOG.log(Level.SEVERE,e.getMessage(),e);
            }
        }
    }


    @Override
    public void save(Class entity) {

        final StringBuilder insert = new StringBuilder();

        insert.append(INSERT);
        try{
            prepareParametersInsert(entity,insert);
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }

    }

    private void prepareParametersInsert(Class entity, StringBuilder sb) throws SQLException {

        StringJoiner sj = getFields(entity);

        if(entity.isAnnotationPresent(DefaultModel.class)){
            LOG.log(Level.SEVERE, "A classe não possui a anotação DefaultModel");
            return;
        }

        replace(TABLE,entity.getSimpleName(),sb);
        finalizeInsert(sb,sj.toString());

        PreparedStatement ps = conn.prepareStatement(sb.toString());


        Arrays.stream(sj.toString().split(","))
                .filter(field ->  isAnnotated(entity,field))
                .forEach(fieldName -> {
                    try {
                        Field field = entity.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        ps.setObject(getNext(),field.get(entity));
                    } catch (NoSuchFieldException | SQLException | IllegalAccessException e) {
                        LOG.log(Level.SEVERE, e.getMessage(),e);
                    }
                });

        ps.executeUpdate();
        ps.close();

    }

    private StringJoiner getFields(Class entity) {

        StringJoiner sj = new StringJoiner(", ");
        Arrays.stream(entity.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(TransientField.class))
                .forEach(field -> sj.add(field.getName()));
        return sj;
    }

    private boolean isAnnotated(Class entity, String field) {
        try {
            return !entity.getField(field).isAnnotationPresent(TransientField.class);
        } catch (NoSuchFieldException e) {
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
        return false;
    }

    private int getNext(){
        count = count++;
        return count ;
    }

    private void finalizeInsert(StringBuilder sb,String fields) {
        StringJoiner sj = new StringJoiner(",");
        for(int i = 1; i < fields.length() - fields.replace(",", "").length() + 1; i ++ )
            sj.add("?");
        replace(VALUES,sj.toString(),sb);
    }

    private void replace(String search, String replace,StringBuilder sb) {
        sb.replace(sb.indexOf(search),sb.indexOf(search)+search.length(),replace);
    }

    @Override
    public void delete(Class entity) {

        StringBuilder delete = new StringBuilder();
        delete.append(DELETE);

        try{
            prepareDelete(delete,entity);
        } catch (NoSuchFieldException | SQLException | IllegalAccessException e) {
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
    }

    private void prepareDelete(StringBuilder delete, Class entity) throws NoSuchFieldException, SQLException, IllegalAccessException {
        long valor;
        replace(TABLE,getTableName(entity),delete);

        valor = appendId(entity,delete);
        PreparedStatement ps = conn.prepareStatement(delete.toString());
        ps.setObject(1, valor);

        ps.executeUpdate();
    }

    private long appendId(Object entity , StringBuilder sb) throws NoSuchFieldException, IllegalAccessException {
        ID id = entity.getClass().getAnnotation(ID.class);
        long valor = (long) entity.getClass().getField(id.name()).get(entity);
        replace(WHERE,id.name()+ " = ?", sb);
        return valor;
    }

    @Override
    public T findById(Object entity) {

        StringBuilder select = new StringBuilder();
        select.append(SELECT);

        prepareSelect(select,entity.getClass());
        try(PreparedStatement ps = conn.prepareStatement(select.toString())) {
            long valor = appendId(entity, select);
            ps.setObject(1,valor);

            return parseObject(ps.getResultSet(),entity.getClass());

        } catch (NoSuchFieldException | IllegalAccessException | SQLException e){
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
        return null;
    }

    private T parseObject(ResultSet resultSet,Class entity) throws SQLException {
        T ret = null;
        try {
            ret = (T) entity.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        T finalRet = ret;
        Arrays.stream(entity.getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(finalRet, resultSet.getObject(field.getName()));
                    } catch (IllegalAccessException | SQLException e) {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                });

        return ret;
    }

    private void prepareSelect(StringBuilder select, Class entity) {
        replace(TABLE, getTableName(entity), select);
        StringJoiner sj = getFields(entity);
        replace(FIELDS,sj.toString(),select);
    }
    private void prepareSelect(StringBuilder select, Class entity, String where) {
        prepareSelect(select,entity);
        replace(WHERE,where == null ? " 1 = 1 " : where,select);
    }

    private String getTableName(Class entity) {
        DefaultModel defaultModel = (DefaultModel) entity.getDeclaredAnnotation(DefaultModel.class);

        return defaultModel.name();
    }

    @Override
    public List<T> findAll(Class entity) {

        return defaultFind(entity, null);
    }

    public List<T> defaultFind(Class entity,String where) {
        StringBuilder select = new StringBuilder();
        List<T> returnList = new ArrayList<>();
        select.append(SELECT);

        prepareSelect(select, entity, where);
        try (PreparedStatement ps = conn.prepareStatement(select.toString())) {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next())
                returnList.add(parseObject(rs, entity));

            return returnList;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<T> find(Class entity, String where) {
        return defaultFind(entity,where);
    }

    @Override
    public void update(Class entity, Object object) {

    }
}
