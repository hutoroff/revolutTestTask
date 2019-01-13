package ru.hutoroff.interview.revolut.service;

import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.data.exception.StorageException;
import ru.hutoroff.interview.revolut.data.storage.Storage;
import ru.hutoroff.interview.revolut.data.storage.impl.AccountStorage;
import ru.hutoroff.interview.revolut.service.exception.BusinessException;

import javax.inject.Inject;
import java.math.BigDecimal;

public class AccountService {
    private final Storage<Long, Account> storage;

    @Inject
    public AccountService(AccountStorage storage) {
        this.storage = storage;
    }

    /**
     * Creates new account
     * @param account account information
     * @return key of just created account
     * @throws BusinessException when something went wrong on creation
     */
    public Long create(Account account) throws BusinessException {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        try {
            return storage.create(account);
        } catch (StorageException e) {
            throw new BusinessException("Can not create account", e);
        }
    }

    /**
     * Returns account actual information
     * @param accountId key of account
     * @return account information
     */
    public Account get(Long accountId) {
        return storage.read(accountId);
    }
}
