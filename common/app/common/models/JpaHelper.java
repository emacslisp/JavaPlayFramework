package common.models;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

public class JpaHelper<T, ID> {

    private final Class<T> entityClass;

    
    public JpaHelper (Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public static void commitTransaction() {
       JPA.em().getTransaction().commit();
    }
    
    public static void beginTransaction() {
       JPA.em().getTransaction().begin();
    }
    
    public static void rollback() {
        JPA.em().getTransaction().rollback();
     }
    
    public static void commitAndBeginNewTransaction() {
    	commitTransaction();
    	beginTransaction();
    }

    public static void flushAndClearSession() {
    	getHibernateSession().flush();
    	getHibernateSession().clear();
    }


    @SuppressWarnings("unchecked")
    public static List<Object[]> findList(String sql, Object... args) {
        return createSqlQuery(sql, args).list();
    }
    
  
   
    public static boolean exists(String sql, Object... args) {
        return createSqlQuery(sql, args).uniqueResult() != null;
    }
    
    @SuppressWarnings("unchecked")
    public static List<Integer> findIntegerListSql(String sqlQuery, Object... params) {
        List<Number> result = createSqlQuery(sqlQuery, params).list();
        return result.stream().map(r-> {
            return r != null? r.intValue() : null;
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static List<String> findStringListSql(String sqlQuery, Object... params) {
        return createSqlQuery(sqlQuery, params).list();
    }

    public static Integer findUniqueIntegerSql(String sqlQuery, Object... params) {
        Number n = (Number) createSqlQuery(sqlQuery, params).uniqueResult();
        return n == null? null : n.intValue();
    }

    public static String findUniqueStringSql(String sqlQuery, Object... params) {
        return (String) createSqlQuery(sqlQuery, params).uniqueResult();
    }


    
    public static int executeUpdate(String sql, Object... params) {
        return createSqlQuery(sql, params).executeUpdate();
    }
    
    public static void insert(Collection<?> entities) {
        for (Object entity : entities) {
            insert(entity);
        }
    }

    public static Object insert(Object entity) {
        JPA.em().persist(entity);
        Session sess = getHibernateSession();
        sess.flush();
        sess.setReadOnly(entity, true);
        return entity;
    }
    
    public static void update(Object entity) {
        Session sess = getHibernateSession();
        sess.evict(entity);
        sess.update(entity);
        sess.flush();
        sess.setReadOnly(entity, true);
    }
    
    public static void delete(Object entity) {
        JPA.em().remove(entity);
    }
    
    public T findById(ID id) {
        return id != null? JPA.em().find(entityClass, id) : null;
    }
    
    
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getHibernateSession().createCriteria(entityClass).list();
    }
    
    @SuppressWarnings("unchecked")
    public T findUniqueSql(String sqlQuery, Object... params) {
        return (T) createSqlQuery(sqlQuery, params).addEntity(entityClass).uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public T findUniqueHql(String hqlQuery, Object... params) {
        return (T) createHqlQuery(hqlQuery, params).uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findListHql(String hqlQuery, Object... params) {
        return createHqlQuery(hqlQuery, params).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findListSql(String sqlQuery, Object... params) {
        return createSqlQuery(sqlQuery, params).addEntity(entityClass).list();
    }
    
    @SuppressWarnings("unchecked")
    public Map<ID, T> asMap(List<Object[]> results) {
        Map<ID, T> map = new LinkedHashMap<>();
        for (Object[] result : results) {
            map.put((ID) result[0], (T) result[1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public Multimap<ID, T> asMultiMap(Collection<Object[]> results) {
        Multimap<ID, T> map = ArrayListMultimap.create();
        for (Object[] result : results) {
            map.put((ID) result[0], (T) result[1]);
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, T> asMapWithStringKey(List<Object[]> results) {
        Map<String, T> map = new LinkedHashMap<>();
        for (Object[] result : results) {
            map.put((String) result[0], (T) result[1]);
        }
        return map;
    }
    
    
    public static Session getHibernateSession() {
        return JPA.em().unwrap(org.hibernate.Session.class);
    }
    
    public static SQLQuery createSqlQuery(String sqlQuery, Object... params) {
        Session session = getHibernateSession();
        SQLQuery q = session.createSQLQuery(sqlQuery);
        setParameters(q, params);
        return q;
    }

    
    public static Query createHqlQuery(String hqlQuery, Object... params) {
        Session session = getHibernateSession();
        Query q = session.createQuery(hqlQuery);
        setParameters(q, params);
        return q;
    }
    
    private static void setParameters(Query q, Object... params) {
        for (int i = 0; i < params.length; i++) {
            Object value = params[i];
            String name = "p"+(i+1);
            if (value instanceof Collection) {
                q.setParameterList(name, (Collection<?>) value);
            } else {
                q.setParameter(name, value);
            }
        }
    }
}
