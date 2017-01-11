package io.github.aureojr.infrastructure.persistence;

import com.github.javafaker.Faker;
import io.github.aureojr.core.catalog.domain.Category;
import io.github.aureojr.core.catalog.domain.CategoryImpl;
import io.github.aureojr.core.catalog.domain.Product;
import io.github.aureojr.core.catalog.domain.ProductImpl;
import io.github.aureojr.infrastructure.database.annotations.DefaultModel;
import io.github.aureojr.infrastructure.database.annotations.ID;
import io.github.aureojr.infrastructure.database.annotations.TransientField;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
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

    @Value("${app.database.url}")
    private static String urlConnection;

    @Value("${app.database.port}")
    private static String port;

    @Value("${app.database.database}")
    private static String databaseName;

    @Value("${app.database.username}")
    private static String username;

    @Value("${app.database.password}")
    private static String password;

    @Value("${app.database.create}")
    private static Boolean createDatabase;

    private static Connection conn;

    private int count =0;

    public PersistenceUnitImpl() {
        if(conn == null){
            forceLoad();

            try {
                conn = DriverManager.getConnection(urlConnection+":"+port+"/"+databaseName,username,password);
            } catch (SQLException e) {
                LOG.log(Level.SEVERE,e.getMessage(),e);
            }
        }
    }

    @PostConstruct
    private void faker(){
        Faker faker = new Faker(new Locale("pt-BR"));

        CategoryImpl c = new CategoryImpl();
        c.setEnabled(true);
        c.setName("Carros");

        save(c, CategoryImpl.class);
        //Definindo 10 categorias
        for (int i = 1; i <= 10; i++){
            CategoryImpl category = new CategoryImpl();
            category.setEnabled(true);
            category.setName(faker.commerce().department());

            save(category, CategoryImpl.class);
        }

        for (int i = 0; i < 100 ; i++){

            ProductImpl p = new ProductImpl();
            p.setQuantityAvailable((int) (Math.random() % 1000));
            p.setEnabled(true);
            CategoryImpl cat = new CategoryImpl();
            Double d = Math.random() * 1000 % 10;
            cat.setId(Long.parseLong(String.valueOf(d.intValue())));
            p.setMainCategory((Category) findById(cat));
            p.setColor(faker.color().name());
            p.setDefaultImage("https://unsplash.it/400/500");
            p.setDescription(faker.shakespeare().hamletQuote());
            Double price = Math.random() * 10000 % 1000;
            DecimalFormat df = new DecimalFormat("00.00");
            p.setPrice(Double.parseDouble(df.format(price).replace(',','.')));
            p.setSize((String.valueOf(Math.random() * 10000 % 1000)));
            p.setWeight((String.valueOf(Math.random() * 10000 % 1000)));
            p.setName(faker.commerce().productName());

            save(p, ProductImpl.class);
        }
    }

    public void init(){
            final class SqlExecuter extends SQLExec {
                public SqlExecuter() {
                    Project project = new Project();
                    project.init();
                    setProject(project);
                    setTaskType("sql");
                    setTaskName("sql");
                }
            }

            SqlExecuter exec = new SqlExecuter();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            exec.setSrc(new File(classLoader.getResource("database.sql").getFile()));
            exec.setDriver(DatabaseDriver.MARIADB.getDriverClassName());
            exec.setPassword(password);
            exec.setUserid(username);
            exec.setUrl(urlConnection+":"+port);
            exec.execute();
    }

    private void forceLoad() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource("application.properties");
            input = url.openStream();

            prop.load(input);
            urlConnection = prop.getProperty("app.database.url");
            databaseName = prop.getProperty("app.database.database") ;
            port = prop.getProperty("app.database.port") ;
            username = prop.getProperty("app.database.username") ;
            password = prop.getProperty("app.database.password") ;
            createDatabase = Boolean.getBoolean(prop.getProperty("app.database.create")) ;
            init();
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getMessage(),e);
        }
    }


    @Override
    public void save(Object values, Class entity) {

        final StringBuilder insert = new StringBuilder();

        insert.append(INSERT);
        try{
            prepareParametersInsert(values,entity,insert);
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }

    }

    private void prepareParametersInsert(Object values,Class entity, StringBuilder sb) throws SQLException {

        StringJoiner sj = getFieldsWithoutID(entity);

        if(!entity.isAnnotationPresent(DefaultModel.class)){
            LOG.log(Level.SEVERE, "A classe não possui a anotação DefaultModel");
            return;
        }

        replace(TABLE,getTableName(entity),sb);
        finalizeInsert(sb,sj.toString());
        replace(FIELDS,sj.toString(),sb);

        try(PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            count = 1;
            for (String fieldName : sj.toString().split(",")) {
                setPsValue(ps, entity, values, fieldName);
            }

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
    }

    private void setPsValue(PreparedStatement ps, Class entity, Object values,String fieldName) throws SQLException {

        try {
            Field field = values.getClass().getDeclaredField(fieldName.trim());
            field.setAccessible(true);
            ps.setObject(count, isDefaultTableModel(field,entity) ? getIdValue(field,entity) : field.get(values));
            count ++;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
    }

    private Object getIdValue(Field field,Object objectInstance) {
//        try {
//            Object obj = field.get(objectInstance);
//            Field idField = obj.getClass().getDeclaredField("id");
//            return idField.get(obj);
//        } catch (IllegalAccessException | NoSuchFieldException e ) {
//            LOG.log(Level.SEVERE, e.getMessage(),e);
 //       }
        Double d = Math.random() * 100 % 10;
        return d.intValue();
    }

    private boolean isDefaultTableModel(Field field,Object objectInstance) throws IllegalAccessException {

       return field.isAnnotationPresent(DefaultModel.class) || field.getName().equals("mainCategory");

    }

    private StringJoiner getFields(Class entity) {

        StringJoiner sj = new StringJoiner(", ");
        Arrays.stream(entity.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(TransientField.class))
                .forEach(field -> sj.add(field.getName()));
        return sj;
    }

    private StringJoiner getFieldsWithoutID(Class entity) {

        StringJoiner sj = new StringJoiner(", ");
        Arrays.stream(entity.getDeclaredFields())
                .filter((field) -> (!field.isAnnotationPresent(TransientField.class)
                            && !field.isAnnotationPresent(ID.class))
                )
                .forEach(field -> sj.add(field.getName()));
        return sj;
    }

    private boolean isAnnotated(Class entity, String field) {
        try {
            return !entity.getField(field).isAnnotationPresent(TransientField.class)
                    || !entity.getField(field).isAnnotationPresent(ID.class);
        } catch (NoSuchFieldException e) {
            LOG.log(Level.SEVERE, e.getMessage(),e);
        }
        return false;
    }

    private void finalizeInsert(StringBuilder sb,String fields) {
        StringJoiner sj = new StringJoiner(",");
        for(String s : fields.split(",") )
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
        Long valor;
        replace(TABLE,getTableName(entity),delete);

        valor = appendId(entity,delete);
        PreparedStatement ps = conn.prepareStatement(delete.toString());
        ps.setObject(1, valor);

        ps.executeUpdate();
    }

    private long appendId(Object entity , StringBuilder sb) throws NoSuchFieldException, IllegalAccessException {
        String idName = "id";
        Field field = entity.getClass().getDeclaredField(idName);
        field.setAccessible(true);
        if (field.isAnnotationPresent(ID.class)) {
            ID id = field.getAnnotation(ID.class);
            idName = id.name();
        }

        Long valor = (Long)field.get(entity);
        replace(WHERE,idName+ " = ?", sb);
        return valor;
    }

    @Override
    public T findById(Object entity) {

        StringBuilder select = new StringBuilder();
        select.append(SELECT);

        prepareSelect(select,entity.getClass());
        try(PreparedStatement ps = conn.prepareStatement(select.toString())) {
            Long valor = appendId(entity, select);
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
        if(select.toString().contains("mainCategory"))
        replace("mainCategory,", " ", select);
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
