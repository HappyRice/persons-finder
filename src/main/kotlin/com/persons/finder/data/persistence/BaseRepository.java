package com.persons.finder.data.persistence;

import com.persons.finder.data.model.BaseModel;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class BaseRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    private Session getSession() {
        final Session session = this.entityManager.unwrap(Session.class);
        session.enableFilter("notDeleted");
        return session;
    }

    private Session getSessionWithDeleted() {
        final Session session = this.entityManager.unwrap(Session.class);
        session.disableFilter("notDeleted");
        return session;
    }

    public void persist(final Object entity) {
        this.entityManager.persist(entity);
    }

    public <T extends BaseModel> T merge(final T entity) {
        return this.entityManager.merge(entity);
    }

    public void saveOrUpdate(final Object entity) {
        final Session session = this.entityManager.unwrap(Session.class);
        session.saveOrUpdate(entity);
    }

    public void delete(final Object entity) {
        this.getSessionWithDeleted().delete(entity);
    }

    protected <T extends BaseModel> T getById(final Class<T> clazz, final long id) {
        return this.getSessionWithDeleted().get(clazz, id);
    }

    protected <T extends BaseModel> T getSingleResult(final Class<T> clazz) {
        final CriteriaBuilder builder = this.getSessionWithDeleted().getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(clazz);
        final Root<T> variableRoot = criteria.from(clazz);
        criteria.select(variableRoot);
        return this.getSessionWithDeleted().createQuery(criteria).uniqueResult();
    }

    protected void reloadEntity(final BaseModel object) {
        this.getSessionWithDeleted().refresh(object);
    }

    protected Query<?> createQueryWithDeleted(final String queryString) {
        return this.getSessionWithDeleted().createQuery(queryString);
    }

    protected Query<?> createQuery(final String queryString) {
        return this.getSession().createQuery(queryString);
    }

    protected Query<?> getNamedQuery(final String queryName) {
        return this.getSession().getNamedQuery(queryName);
    }

    protected Query<?> getNamedQueryWithDeleted(final String queryName) {
        return this.getSessionWithDeleted().getNamedQuery(queryName);
    }

    protected List<?> list(final Query<?> query) {
        this.getSession().disableFilter("notDeleted");

        return query.list();
    }

    protected Object getUniqueResultWithDeleted(final Query<?> query) {
        this.getSession().disableFilter("notDeleted");

        return query.uniqueResult();
    }

    protected Object getUniqueResult(final Query<?> query) {
        return query.uniqueResult();
    }

    protected <T extends BaseModel> List<T> listAll(final Class<T> clazz) {
        final CriteriaBuilder builder = this.getSessionWithDeleted().getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(clazz);
        final Root<T> variableRoot = criteria.from(clazz);
        criteria.select(variableRoot);
        return this.getSession().createQuery(criteria).list();
    }
}