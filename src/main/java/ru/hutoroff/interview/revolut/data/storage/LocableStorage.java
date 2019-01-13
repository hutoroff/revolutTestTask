package ru.hutoroff.interview.revolut.data.storage;

import ru.hutoroff.interview.revolut.data.entity.AbstractEntity;
import ru.hutoroff.interview.revolut.data.exception.StorageException;
import ru.hutoroff.interview.revolut.data.generator.KeyGenerator;

import java.util.concurrent.ConcurrentHashMap;

public abstract class LocableStorage<K, V extends AbstractEntity<K>> extends Storage<K, V> {
    private final ConcurrentHashMap<K, Integer> locks = new ConcurrentHashMap<>();
    private final Object lockObject = new Object();

    public LocableStorage(KeyGenerator<K> keyGenerator) {
        super(keyGenerator);
    }

    public V readForUpdate(K key) throws StorageException {
        lock(key);
        return super.read(key);
    }

    private void addLock(K key) throws InterruptedException {
        synchronized (lockObject) {
            while (locks.containsKey(key)) {
                Integer hash = locks.get(key);
                if (hash != Thread.currentThread().hashCode()) {
                    lockObject.wait();
                } else {
                    return;
                }
            }
            locks.put(key, Thread.currentThread().hashCode());
            lockObject.notifyAll();
        }
    }

    @Override
    public boolean update(V entry) throws StorageException {
        lock(entry.getKey());
        try {
            return super.update(entry);
        } finally {
            unlock(entry.getKey());
        }
    }

    private void unlock(K key) {
        final int currentHash = Thread.currentThread().hashCode();

        synchronized (lockObject) {
            Integer hash = locks.get(key);
            if (!hash.equals(currentHash)) {
                throw new IllegalStateException("No lock for current thread");
            }
            locks.remove(key);
            lockObject.notifyAll();
        }
    }

    private void lock(K key) throws StorageException {
        try {
            addLock(key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException(String.format("Can not lock entry with key '%s' in storage", key), e);
        }
    }

    public void releaseLock(K key) {
        synchronized (lockObject) {
            locks.remove(key);
            lockObject.notifyAll();
        }
    }
}
