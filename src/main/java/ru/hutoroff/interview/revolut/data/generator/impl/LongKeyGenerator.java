package ru.hutoroff.interview.revolut.data.generator.impl;

import ru.hutoroff.interview.revolut.data.generator.KeyGenerator;

import java.util.concurrent.atomic.AtomicLong;

public class LongKeyGenerator implements KeyGenerator<Long> {
    private final AtomicLong currentKey = new AtomicLong(0);

    @Override
    public Long getNextKey() {
        return currentKey.incrementAndGet();
    }
}
