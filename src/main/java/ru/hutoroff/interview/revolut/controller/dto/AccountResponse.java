package ru.hutoroff.interview.revolut.controller.dto;

import ru.hutoroff.interview.revolut.data.entity.impl.Account;

import java.math.BigDecimal;

public class AccountResponse extends AccountCreationResponse {
    public BigDecimal balance;

    public AccountResponse(Account account) {
        super(account.getKey());
        this.balance = account.getBalance();
    }
}
