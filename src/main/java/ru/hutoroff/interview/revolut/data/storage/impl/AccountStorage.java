package ru.hutoroff.interview.revolut.data.storage.impl;

import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.data.generator.impl.LongKeyGenerator;
import ru.hutoroff.interview.revolut.data.storage.LocableStorage;
import ru.hutoroff.interview.revolut.data.storage.Storage;

import javax.inject.Singleton;

@Singleton
public class AccountStorage extends LocableStorage<Long, Account> {

    public AccountStorage() {
        super(new LongKeyGenerator());
    }
}
