package ru.hutoroff.interview.revolut;

import org.jooby.Jooby;
import org.jooby.json.Jackson;
import ru.hutoroff.interview.revolut.controller.AccountController;
import ru.hutoroff.interview.revolut.controller.TransferController;
import ru.hutoroff.interview.revolut.data.entity.impl.Transaction;

public class App extends Jooby {

  {
    use(new Jackson());
    use(new ApplicationModule());
    use(AccountController.class);
    use(TransferController.class);
  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
