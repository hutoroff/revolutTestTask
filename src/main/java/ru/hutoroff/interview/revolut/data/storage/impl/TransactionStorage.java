package ru.hutoroff.interview.revolut.data.storage.impl;

import ru.hutoroff.interview.revolut.data.entity.impl.Transaction;
import ru.hutoroff.interview.revolut.data.generator.impl.LongKeyGenerator;
import ru.hutoroff.interview.revolut.data.storage.LocableStorage;
import ru.hutoroff.interview.revolut.data.storage.Storage;

import javax.inject.Singleton;

@Singleton
public class TransactionStorage extends Storage<Long, Transaction> {
    public TransactionStorage() {
        super(new LongKeyGenerator());
    }
}
