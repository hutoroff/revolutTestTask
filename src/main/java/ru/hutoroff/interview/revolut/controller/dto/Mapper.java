package ru.hutoroff.interview.revolut.controller.dto;

import org.jooby.Mutant;
import org.jooby.Request;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;

import java.math.BigDecimal;

public class Mapper {
    public Account toAccount(AccountCreationRequest request) {
        return new Account(request.balance);
    }
    public <T> T toObject(Request request, Class<T> clazz) {
        try {
            return request.body(clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse request body", e);
        }
    }
}
