package ru.hutoroff.interview.revolut.controller.dto;

import java.io.Serializable;

public class AccountCreationResponse implements Serializable {

    public Long accountId;

    public AccountCreationResponse(Long accountId) {
        this.accountId = accountId;
    }
}
