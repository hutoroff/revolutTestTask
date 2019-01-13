package ru.hutoroff.interview.revolut.data.impl;

import org.junit.Assert;
import org.junit.Test;
import ru.hutoroff.interview.revolut.data.generator.impl.LongKeyGenerator;

public class LongKeyGeneratorTest {

    @Test
    public void getNextKey() {
        LongKeyGenerator keyGenerator = new LongKeyGenerator();
        Long firstKey = keyGenerator.getNextKey();
        Long secondKey = keyGenerator.getNextKey();
        Assert.assertEquals(-1, firstKey.compareTo(secondKey));
    }
}