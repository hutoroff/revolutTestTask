package ru.hutoroff.interview.revolut.data.entity;

import java.io.Serializable;

public abstract class AbstractEntity<K> implements Serializable {

    public AbstractEntity() {
    }

    public AbstractEntity(AbstractEntity<K> that) {
    }

    abstract public AbstractEntity copy();
    abstract public K getKey();
    abstract public void setKey(K key);
}
