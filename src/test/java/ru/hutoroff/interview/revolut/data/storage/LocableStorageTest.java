package ru.hutoroff.interview.revolut.data.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.data.exception.StorageException;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocableStorageTest extends StorageTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void readForUpdate() throws Exception {
        Long accountId = storage.create(defaultAccount);
        Account account = storage.readForUpdate(accountId);
        Assert.assertNotNull("Read success", account);
    }

    @Test
    public void update() throws Exception {
        Long accountId = storage.create(defaultAccount);

        BigDecimal slowValue = new BigDecimal(111);
        BigDecimal fastValue = new BigDecimal(222);
        EntryUpdater slowUpdater = new EntryUpdater(storage, accountId, slowValue, 100L);
        EntryUpdater fastUpdater = new EntryUpdater(storage, accountId, fastValue, 0L);

        Future<Boolean> slowFuiture = executorService.submit(slowUpdater);
        Future<Boolean> fastFuture = executorService.submit(fastUpdater);

        Assert.assertTrue("Fast update completed successfully", fastFuture.get());
        Assert.assertTrue("Slow update completed successfully", slowFuiture.get());

        Account currentAccountState = storage.read(accountId);
        Assert.assertEquals(fastValue, currentAccountState.getBalance());
    }

    private class EntryUpdater implements Callable<Boolean> {
        private final LocableStorage<Long, Account> storage;
        private final Long id;
        private final BigDecimal newValue;
        private final long timeout;

        public EntryUpdater(LocableStorage<Long, Account> storage, Long id, BigDecimal newValue, long timeout) {
            this.storage = storage;
            this.id = id;
            this.newValue = newValue;
            this.timeout = timeout;
        }

        @Override
        public Boolean call() throws InterruptedException, StorageException {
            storage.readForUpdate(id);
            Thread.sleep(timeout);
            Account accountToStore = new Account();
            accountToStore.setKey(id);
            accountToStore.setBalance(newValue);
            storage.update(accountToStore);
            return true;
        }
    }
}