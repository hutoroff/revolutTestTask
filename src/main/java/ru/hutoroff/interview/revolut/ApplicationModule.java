package ru.hutoroff.interview.revolut;

import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.typesafe.config.Config;
import org.jooby.Env;
import org.jooby.Jooby;
import ru.hutoroff.interview.revolut.data.storage.Storage;
import ru.hutoroff.interview.revolut.data.storage.impl.AccountStorage;
import ru.hutoroff.interview.revolut.data.storage.impl.TransactionStorage;

public class ApplicationModule implements Jooby.Module {

    @Override
    public void configure(Env env, Config conf, Binder binder) throws Throwable {
        Multibinder<Storage> storageMultibinder = Multibinder.newSetBinder(binder, Storage.class);
        storageMultibinder.addBinding().to(AccountStorage.class);
        storageMultibinder.addBinding().to(TransactionStorage.class);
    }
}
