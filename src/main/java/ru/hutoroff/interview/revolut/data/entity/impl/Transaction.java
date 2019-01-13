package ru.hutoroff.interview.revolut.data.entity.impl;

import ru.hutoroff.interview.revolut.data.entity.AbstractEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Transaction extends AbstractEntity<Long> {
    private Long id;
    private Date date;
    private Long from;
    private Long to;
    private BigDecimal amount;

    public Transaction() {
        super();
    }

    public Transaction(Date date, Long from, Long to, BigDecimal amount) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Transaction(Transaction that) {
        super(that);
        this.id = that.id;
        this.date = that.date;
        this.from = that.from;
        this.to = that.to;
        this.amount = that.amount;
    }

    @Override
    public Long getKey() {
        return id;
    }

    @Override
    public void setKey(Long key) {
        this.id = key;
    }

    @Override
    public Transaction copy() {
        return new Transaction(this);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, from, to, amount);
    }
}
