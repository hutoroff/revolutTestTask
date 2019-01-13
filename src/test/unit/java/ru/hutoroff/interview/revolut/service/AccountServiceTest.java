package ru.hutoroff.interview.revolut.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.data.storage.impl.AccountStorage;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountStorage accountStorage;

    @Before
    public void setUp() throws Exception {
        accountStorage = new AccountStorage();
        accountService = new AccountService(accountStorage);
    }

    @Test
    public void create() throws Exception {
        Long accId = accountService.create(new Account(BigDecimal.TEN));
        Account account = accountStorage.read(accId);
        Assert.assertEquals("As initiated", BigDecimal.TEN, account.getBalance());

        accId = accountService.create(new Account());
        account = accountStorage.read(accId);
        Assert.assertEquals("Initiated with default", BigDecimal.ZERO, account.getBalance());
    }

    @Test
    public void get() throws Exception {
        Long accId = accountService.create(new Account());
        Account account = accountService.get(accId);
        Assert.assertEquals(BigDecimal.ZERO, account.getBalance());
    }
}