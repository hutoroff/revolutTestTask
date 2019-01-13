package ru.hutoroff.interview.revolut.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountCreationRequest implements Serializable {
    public BigDecimal balance;
}
