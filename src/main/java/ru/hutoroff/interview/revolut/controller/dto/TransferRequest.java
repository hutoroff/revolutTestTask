package ru.hutoroff.interview.revolut.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferRequest implements Serializable {
    public TransferRequest() {
    }

    public TransferRequest(Long from, Long to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Long from;
    public Long to;
    public BigDecimal amount;
}
