package com.tip.functional;

import com.tip.functional.customexception.InvalidRangeException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Range implements Iterable<Long> {
    private long startInclusive;
    private long endExclusive;

    /**
     * long타입의 startInclusive과 endExclusive를 매개변수로 받는 Range 셍성자를 반환하는 메서드입니다.
     *
     * @param startInclusive 시작점 매개변수
     * @param endExclusive   끝점 매개변수
     */
    public Range(long startInclusive, long endExclusive) {
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        classInvariant();
    }

    /**
     * 1부터 끝점 사이의 Range 생성자
     *
     * @param endExclusive long 타입의 끝점입니다.
     */
    public Range(long endExclusive) {
        this(1, endExclusive);
    }

    public static Range closed(long startInclusive, long endInclusive) {
        return new Range(startInclusive, endInclusive + 1);
    }

    /**
     * Range가 적절한 startInclusive와 endExclusive를 가지고 있는 지 검사하는 메서드
     *
     * @throws IllegalArgumentException startInclusive값이 endExclusive보다 클 경우 반환합니다.
     */
    private void classInvariant() {
        if (max() < min()) {
            throw new IllegalArgumentException("Range: " + this.min() + " > " + this.max());
        }
    }

    /**
     * @return 인수의 차이를 반환합니다
     * @throws ArithmeticException 오버플로우가 발생하면 던져지는 예외처리입니다.
     */
    public long max() {
        return Math.subtractExact(endExclusive, 1);
    }

    /**
     * @return Range의 startInclusive를 반환합니다
     */
    public long min() {
        return this.startInclusive;
    }

    /**
     * @return Range의 end를 반환합니다
     */
    public long end() {
        return this.endExclusive;
    }

    /**
     * @return endExclusive와 startInclusive의 차이를 반환합니다.
     */
    public long size() {
        return Math.subtractExact(this.end(), this.min());
    }

    /**
     * 이 객체가 나타내는 범위에 속한 Long 값을 순차적으로 반환하는 Iterator를 생성합니다.
     * Iterator는 이 객체의 최소값에서 시작하여 끝 값에 도달할 때까지 순차적으로 증가합니다.
     *
     * @return 범위에 속한 Long 값을 순차적으로 반환하는 Iterator
     * @throws NoSuchElementException 다음 요소가 없을 때 'next'를 호출하면 발생
     */
    public Iterator<Long> iterator() {
        return new Iterator<Long>() {

            private long current = min();

            public boolean hasNext() {
                return current < end();
            }

            public Long next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Range.iterator()");
                }
                long value = current;
                current = Math.addExact(current, 1);
                return value;
            }
        };
    }
}
