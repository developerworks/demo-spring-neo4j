package com.totorotec.rbac.hateoas;

import org.springframework.hateoas.ResourceSupport;

/**
 * http://blog.csdn.net/joymod/article/details/65438748
 * @param <T>
 */
public class EntityResource<T> extends ResourceSupport {

    public EntityResource(T entity) {
        super();
        this.entity = entity;
    }

    private T entity;

    public T getEntity() {
        return entity;
    }

}
