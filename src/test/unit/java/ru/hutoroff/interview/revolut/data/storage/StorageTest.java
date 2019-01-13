package ru.hutoroff.interview.revolut.data.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.data.exception.StorageException;
import ru.hutoroff.interview.revolut.data.storage.impl.AccountStorage;

import java.math.BigDecimal;
import java.util.Collection;

public class StorageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected AccountStorage storage;
    protected Account defaultAccount;

    @Before
    public void setUp() {
        storage = new AccountStorage();
        defaultAccount = new Account();
        defaultAccount.setBalance(new BigDecimal(123));
    }

    @Test
    public void create() throws Exception {
        Long accountId = storage.create(defaultAccount);
        Assert.assertNotNull("Key returned", accountId);

        Account accountFromStorage = storage.read(accountId);
        Assert.assertNotNull("Entry found", accountFromStorage);
        Assert.assertEquals("Value same to saved", defaultAccount.getBalance(), accountFromStorage.getBalance());
    }

    @Test
    public void createFailure() throws Exception {
        Long accountId = storage.create(defaultAccount);

        Account sameIdAccount = new Account();
        sameIdAccount.setBalance(new BigDecimal(321));
        sameIdAccount.setKey(accountId);

        thrown.expect(StorageException.class);
        thrown.expectMessage(String.format("Can not insert entry. Key '%s' already exists", accountId));
        storage.create(sameIdAccount);
    }

    @Test
    public void read() throws Exception {
        Long accountId = storage.create(defaultAccount);

        Account account = storage.read(accountId);
        Assert.assertNotNull("Found", account);

        account = storage.read(-1L);
        Assert.assertNull("Not found", account);
    }

    /*@Test
    public void readAll() throws Exception {
        final int accountsToCreate = 5;
        for (int i = 0; i < accountsToCreate; i++) {
            defaultAccount.setKey(null);
            storage.create(defaultAccount);
        }

        Collection<Account> accounts = storage.readAll();
        Assert.assertEquals("All created loaded", accountsToCreate, accounts.size());
    }*/

    @Test
    public void update() throws Exception {
        Long accountId = storage.create(defaultAccount);
        Account entryToUpdate = new Account();
        entryToUpdate.setKey(accountId);
        entryToUpdate.setBalance(defaultAccount.getBalance().add(new BigDecimal(5)));
        boolean updateResult = storage.update(entryToUpdate);
        Assert.assertTrue("Value updated", updateResult);

        updateResult = storage.update(entryToUpdate);
        Assert.assertFalse("Value is same, not updated", updateResult);
    }

    @Test
    public void updateFailure() throws Exception {
        final Long key = 1L;
        defaultAccount.setKey(key);

        thrown.expect(StorageException.class);
        thrown.expectMessage(String.format("Entry was not updated. Nothing found for key '%s'", key));

        storage.update(defaultAccount);
    }

    @Test
    public void delete() throws Exception {
        Long accountId = storage.create(defaultAccount);
        Account account = storage.read(accountId);account.setBalance(null);
        Assert.assertNotNull("Found", account);

        Account deleteResult = storage.delete(accountId);
        Assert.assertNotNull("Deleted account returned", deleteResult);
        Assert.assertEquals("Deleted account equal to created", defaultAccount, deleteResult);

        deleteResult = storage.delete(accountId);
        Assert.assertNull("Nothing to delete", deleteResult);
    }
}