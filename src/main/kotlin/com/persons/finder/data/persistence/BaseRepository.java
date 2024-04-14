package com.persons.finder.data.persistence;

import com.persons.finder.data.model.BaseModel;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class BaseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected void persist(final Object entity) {
        this.entityManager.persist(entity);
    }

    protected void delete(final Object entity) {
        this.getSessionWithDeleted().delete(entity);
    }

    protected <T extends BaseModel> T getById(final Class<T> clazz, final long id) {
        return this.getSessionWithDeleted().get(clazz, id);
    }

    protected Query<?> getNamedQuery(final String queryName) {
        return this.getSession().getNamedQuery(queryName);
    }

    protected List<?> list(final Query<?> query) {
        this.getSession().disableFilter("notDeleted");

        return query.list();
    }

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
}