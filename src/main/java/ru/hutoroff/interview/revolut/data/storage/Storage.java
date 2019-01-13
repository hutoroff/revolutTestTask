package ru.hutoroff.interview.revolut.data.storage;

import ru.hutoroff.interview.revolut.data.entity.AbstractEntity;
import ru.hutoroff.interview.revolut.data.exception.StorageException;
import ru.hutoroff.interview.revolut.data.generator.KeyGenerator;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class Storage<K, V extends AbstractEntity<K>> {
    private final ConcurrentHashMap<K, V> storage = new ConcurrentHashMap<>();
    private final KeyGenerator<K> keyGenerator;

    public Storage(KeyGenerator<K> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public K create(V entry) throws StorageException {
        K key = entry.getKey();
        if (key == null) {
            K newKey = keyGenerator.getNextKey();
            entry.setKey(newKey);
        }
        V existingValue = storage.putIfAbsent(entry.getKey(), entry);
        if (existingValue != null) {
            throw new StorageException(String.format("Can not insert entry. Key '%s' already exists", entry.getKey()));
        }
        return entry.getKey();
    }

    public V read(K key) {
        V found = storage.get(key);
        return found != null ? (V) found.copy() : null;
    }

    public boolean update(V entry) throws StorageException {
        K key = entry.getKey();
        V oldValue = storage.replace(key, entry);
        if (oldValue == null) {
            throw new StorageException(String.format("Entry was not updated. Nothing found for key '%s'", key));
        }
        return !entry.equals(oldValue);
    }

    public V delete(K key) {
        return storage.remove(key);
    }
}
