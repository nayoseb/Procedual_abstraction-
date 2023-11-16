package com.tip;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import com.tip.functional.Fibonacci;
import com.tip.functional.InfiniteIterator;
import com.tip.functional.Range;
import static com.tip.functional.Iterators.*;

public class Mathx {
    private Mathx() {}

    public static int randInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int randInt(int origin, int boundExclusive) {
        return ThreadLocalRandom.current().nextInt(origin, boundExclusive);
    }

    public static double randDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static InfiniteIterator<Double> randDoubles() {
        return generate(Mathx::randDouble);
    }

    public static InfiniteIterator<Integer> randInts() {
        return generate(Mathx::randInt);
    }

    public static <T extends Number> double sum(Iterable<T> numbers) {
        return reduce(numbers, (x, y) -> x.doubleValue() + y.doubleValue(), 0D);
    }

    public static <T extends Number> double sum(Iterator<T> numbers) {
        return sum(() -> numbers);
    }

    public static long sum(Range range) {
        final long max = range.max();
        final long min = range.min();
        return Math.multiplyExact(range.size(), Math.addExact(max, min)) / 2L;
    }


    public static <T extends Number> double product(Iterator<T> numbers) {
        return product(() -> numbers);
    }

    public static <T extends Number> double product(Iterable<T> numbers) {
        return reduce(numbers, (x, y) -> x.doubleValue() * y.doubleValue(), 1D);
    }

    public static long product(Range range) {
        return reduce(range, Math::multiplyExact, 1L);
    }

    public static long gcd(long x, long y) {
        return BigInteger.valueOf(x).gcd(BigInteger.valueOf(y)).longValueExact();
    }

    public static boolean dirichletTest() {
        return gcd(randInt(), randInt()) == 1;
    }

    public static InfiniteIterator<Integer> discreteUniformDistribution(int origin,
            int boundInclusive) {
        return generate(() -> randInt(origin, boundInclusive + 1));
    }

    public static InfiniteIterator<Integer> discreteUniformDistribution(int boundInclusive) {
        return generate(() -> randInt(0, boundInclusive + 1));
    }

    public static <T> int randEnumUniformlyDistributed(Class<T> enumType) {
        T[] constants = enumType.getEnumConstants();
        return randInt(0, constants.length);
    }

    public static <T extends Enum<T>> Iterator<Integer> discreteUniformDistribution(
            Class<T> enumType) {
        return generate(() -> randEnumUniformlyDistributed(enumType));
    }

    public static double randDoubleNormallyDistributed(double mean, double standardDeviation) {
        return ThreadLocalRandom.current().nextGaussian() * standardDeviation + mean;
    }

    public static InfiniteIterator<Double> normalDistribution(double mean,
            double standardDeviation) {
        return generate(() -> randDoubleNormallyDistributed(mean, standardDeviation));
    }

    // Bernoulli distribition
    public static InfiniteIterator<Integer> binaryDistribution(double probability) {
        if (probability < 0 || probability > 1)
            throw new IllegalArgumentException("Out of range with " + probability);
        return generate(() -> randDouble() <= probability ? 1 : 0);
    }

    public static InfiniteIterator<Integer> fibonacci() {
        return new Fibonacci();
    }
}
