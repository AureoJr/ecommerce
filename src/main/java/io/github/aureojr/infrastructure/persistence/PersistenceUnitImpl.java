package io.github.aureojr.infrastructure.persistence;

import io.github.aureojr.infrastructure.database.annotations.DefaultModel;
import io.github.aureojr.infrastructure.database.annotations.ID;
import io.github.aureojr.infrastructure.database.annotations.TransientField;

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
                conn = DriverManager.getConnection("127.0.0.1:3306/ecommerce","usuario","usuario123");
            } catch (SQLException e) {
                LOG.log(Level.SEVERE,e.getMessage(),e);
            }
        }
    }


    @Override
    public void save(T entity) {

        final StringBuilder insert = new StringBuilder();

        insert.append(INSERT);
        try{
            prepareParametersInsert(entity,insert);
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }

    }

    private void prepareParametersInsert(T entity, StringBuilder sb) throws SQLException {

        StringJoiner sj = getFields(entity);

        if(entity.getClass().isAnnotationPresent(DefaultModel.class)){
            LOG.log(Level.SEVERE, "A classe não possui a anotação DefaultModel");
            return;
        }

        replace(TABLE,entity.getClass().getSimpleName(),sb);
        finalizeInsert(sb,sj.toString(),entity);

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

    private StringJoiner getFields(T entity) {

        StringJoiner sj = new StringJoiner(", ");
        Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(TransientField.class))
                .forEach(field -> sj.add(field.getName()));
        return sj;
    }

    private boolean isAnnotated(T entity, String field) {
        try {
            return !entity.getClass().getField(field).isAnnotationPresent(TransientField.class);
        } catch (NoSuchFieldException e) {
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
        return false;
    }

    private int getNext(){
        count = count++;
        return count ;
    }

    private void finalizeInsert(StringBuilder sb,String fields, T entity) {
        StringJoiner sj = new StringJoiner(",");
        for(int i = 1; i < fields.length() - fields.replace(",", "").length() + 1; i ++ )
            sj.add("?");
        replace(VALUES,sj.toString(),sb);
    }

    private void replace(String search, String replace,StringBuilder sb) {
        sb.replace(sb.indexOf(search),sb.indexOf(search)+search.length(),replace);
    }

    @Override
    public void delete(T entity) {

        StringBuilder delete = new StringBuilder();
        delete.append(DELETE);

        try{
            prepareDelete(delete,entity);
        } catch (NoSuchFieldException | SQLException | IllegalAccessException e) {
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
    }

    private void prepareDelete(StringBuilder delete, T entity) throws NoSuchFieldException, SQLException, IllegalAccessException {
        long valor;
        replace(TABLE,entity.getClass().getSimpleName(),delete);

        valor = appendId(entity,delete);
        PreparedStatement ps = conn.prepareStatement(delete.toString());
        ps.setObject(1, valor);

        ps.executeUpdate();
    }

    private long appendId(T entity , StringBuilder sb) throws NoSuchFieldException, IllegalAccessException {
        ID id = entity.getClass().getAnnotation(ID.class);
        long valor = (long) entity.getClass().getField(id.name()).get(entity);
        replace(WHERE,id.name()+ " = ?", sb);
        return valor;
    }

    @Override
    public T findById(T entity) {

        StringBuilder select = new StringBuilder();
        select.append(SELECT);

        prepareSelect(select,entity);
        try(PreparedStatement ps = conn.prepareStatement(select.toString())) {
            long valor = appendId(entity, select);
            ps.setObject(1,valor);

            return parseObject(ps.getResultSet());

        } catch (NoSuchFieldException | IllegalAccessException | SQLException e){
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
        return null;
    }

    private T parseObject(ResultSet resultSet) throws SQLException {
         T entity = (T)new Object();

        Arrays.stream(entity.getClass().getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(entity, resultSet.getObject(field.getName()));
                    } catch (IllegalAccessException | SQLException e) {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                });

        return entity;
    }

    private void prepareSelect(StringBuilder select, T entity) {
        replace(TABLE, entity.getClass().getName(), select);
        StringJoiner sj =getFields(entity);
        replace(FIELDS,sj.toString(),select);
    }

    @Override
    public List<T> findAll(T entity) {
        StringBuilder select = new StringBuilder();
        List<T> returnList = new ArrayList<T>();
        select.append(SELECT);

        prepareSelect(select,entity);
        try(PreparedStatement ps = conn.prepareStatement(select.toString())) {

            ResultSet rs = ps.getResultSet();
            while (rs.next())
                returnList.add(parseObject(rs));

            return returnList;
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }

        return  null;
    }
}
