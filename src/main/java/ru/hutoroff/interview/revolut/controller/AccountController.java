package ru.hutoroff.interview.revolut.controller;

import com.google.inject.Inject;
import org.jooby.*;
import org.jooby.mvc.GET;
import org.jooby.mvc.PUT;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hutoroff.interview.revolut.controller.dto.AccountCreationRequest;
import ru.hutoroff.interview.revolut.controller.dto.AccountCreationResponse;
import ru.hutoroff.interview.revolut.controller.dto.AccountResponse;
import ru.hutoroff.interview.revolut.controller.dto.Mapper;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;
import ru.hutoroff.interview.revolut.service.AccountService;
import ru.hutoroff.interview.revolut.service.exception.BusinessException;

@Path("/api/account")
public class AccountController {
    private final static Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final Mapper mapper;

    @Inject
    public AccountController(AccountService accountService, Mapper mapper) {
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @GET
    @Path("/{id}")
    public AccountResponse getAccount(Request request) {
        Mutant pathId = request.param("id");
        long id = pathId.longValue();
        Account account = accountService.get(id);
        if (account == null) {
            throw new Err(Status.NOT_FOUND);
        }
        return new AccountResponse(account);
    }

    @PUT
    public AccountCreationResponse createAccount(Request request) throws Exception {
        AccountCreationRequest account = mapper.toObject(request, AccountCreationRequest.class);
        if (account.balance == null) {
            throw new IllegalArgumentException("Balance state required for account creation");
        }
        try {
            Long accountId = accountService.create(mapper.toAccount(account));
            return new AccountCreationResponse(accountId);
        } catch (BusinessException e) {
            LOG.error("Account {} not created", account, e);
            throw new Err(Status.BAD_REQUEST);
        }
    }
}
