package ru.hutoroff.interview.revolut.service;

import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.data.entity.impl.Transaction;
import ru.hutoroff.interview.revolut.data.exception.StorageException;
import ru.hutoroff.interview.revolut.data.storage.impl.AccountStorage;
import ru.hutoroff.interview.revolut.data.storage.impl.TransactionStorage;
import ru.hutoroff.interview.revolut.service.exception.BusinessException;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

public class TransferService {
    private final TransactionStorage transactionStorage;
    private final AccountStorage accountStorage;

    @Inject
    public TransferService(TransactionStorage transactionStorage, AccountStorage accountStorage) {
        this.transactionStorage = transactionStorage;
        this.accountStorage = accountStorage;
    }

    /**
     * Transfer founds between two accounts
     * @param idFrom key of account to charge
     * @param idTo key of account where funds will be credited
     * @param amount of founds to transfer
     * @throws BusinessException when
     */
    public void transferFromTo(Long idFrom, Long idTo, BigDecimal amount) throws BusinessException {
        try {
            this.doTransfer(idFrom, idTo, amount);
        } catch (StorageException e) {
            throw new BusinessException(String.format("Can not store transfer result from account %d to %d of amount %s", idFrom, idTo, amount), e);
        } finally {
            accountStorage.releaseLock(idFrom);
            accountStorage.releaseLock(idTo);
        }
    }

    private void doTransfer(Long idFrom, Long idTo, BigDecimal amount) throws BusinessException, StorageException {
        Account accountFrom = accountStorage.readForUpdate(idFrom);
        if (accountFrom == null) {
            throw new BusinessException(String.format("Source account with id '%d' not found", idFrom));
        }
        if (accountFrom.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Not enough founds on source account " + idFrom);
        }

        Account accountTo = accountStorage.readForUpdate(idTo);
        if (accountTo == null) {
            throw new BusinessException(String.format("Target account with id '%d' not found", idTo));
        }
        accountFrom.setBalance(accountFrom.getBalance().add(amount.negate()));
        accountTo.setBalance(accountTo.getBalance().add(amount));
        transactionStorage.create(new Transaction(new Date(), idFrom, idTo, amount));
        accountStorage.update(accountFrom);
        accountStorage.update(accountTo);
    }
}
