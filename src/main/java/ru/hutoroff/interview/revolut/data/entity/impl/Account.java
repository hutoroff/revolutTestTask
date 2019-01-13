package ru.hutoroff.interview.revolut.data.entity.impl;

import ru.hutoroff.interview.revolut.data.entity.AbstractEntity;

import java.math.BigDecimal;
import java.util.Objects;

public class Account extends AbstractEntity<Long> {
    private Long id;
    private BigDecimal balance;

    public Account() {
        super();
    }

    public Account(BigDecimal balance) {
        this.balance = balance;
    }

    public Account(Account that) {
        super(that);
        this.id = that.id;
        this.balance = that.balance;
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
    public Account copy() {
        return new Account(this);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
