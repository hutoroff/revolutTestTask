package ru.hutoroff.interview.revolut.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.data.storage.impl.AccountStorage;
import ru.hutoroff.interview.revolut.data.storage.impl.TransactionStorage;
import ru.hutoroff.interview.revolut.service.exception.BusinessException;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransferServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TransferService transferService;
    private AccountStorage accountStorage;
    private TransactionStorage transactionStorage;

    @Before
    public void setUp() {
        transactionStorage = new TransactionStorage();
        accountStorage = new AccountStorage();
        transferService = new TransferService(transactionStorage, accountStorage);
    }

    @Test
    public void transferFromTo() throws Exception {
        BigDecimal srcInitialBalance = new BigDecimal(5);
        Long srcAccounId = accountStorage.create(new Account(srcInitialBalance));
        Long trgtAccountId = accountStorage.create(new Account(BigDecimal.ZERO));
        BigDecimal amount = new BigDecimal(2.55);

        transferService.transferFromTo(srcAccounId, trgtAccountId, amount);
        Account srcAccount = accountStorage.read(srcAccounId);
        Assert.assertEquals("Less balance on source", srcInitialBalance.add(amount.negate()), srcAccount.getBalance());

        Account trgtAccount = accountStorage.read(trgtAccountId);
        Assert.assertEquals("Greater balance on target", BigDecimal.ZERO.add(amount), trgtAccount.getBalance());
    }

    @Test
    public void transferFromToTooBigAmount() throws Exception {
        Long srcAccounId = accountStorage.create(new Account(BigDecimal.ZERO));
        Long trgtAccountId = accountStorage.create(new Account(BigDecimal.ZERO));
        BigDecimal amount = BigDecimal.TEN;

        thrown.expect(BusinessException.class);
        thrown.expectMessage("Not enough founds on source account " + srcAccounId);
        transferService.transferFromTo(srcAccounId, trgtAccountId, amount);
    }

    @Test
    public void transferFromToSourceAccountNotFound() throws Exception {
        long srcAccounId = 0L;
        long trgtAccountId = 1L;
        thrown.expect(BusinessException.class);
        thrown.expectMessage(String.format("Source account with id '%d' not found", srcAccounId));
        transferService.transferFromTo(srcAccounId, trgtAccountId, BigDecimal.ONE);
    }

    @Test
    public void transferFromToTargetAccountNotFound() throws Exception {
        Long srcAccounId = accountStorage.create(new Account(BigDecimal.TEN));
        long trgtAccountId = -1L;

        thrown.expect(BusinessException.class);
        thrown.expectMessage(String.format("Target account with id '%d' not found", trgtAccountId));
        transferService.transferFromTo(srcAccounId, trgtAccountId, BigDecimal.ONE);
    }
}