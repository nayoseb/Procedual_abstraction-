package com.tip.functional;

public class Fibonacci implements InfiniteIterator<Integer> {
    // TODO: 채우기
    private int current = 1;
    private int next = 1;

    public int getCurrent() {
        return current;
    }

    @Override
    public boolean hasNext() {
        // 피보나치 수열은 무한하므로 항상 true를 반환합니다.
        return true;
    }

    @Override
    public Integer next() {
        int result = current;
        int newNext = current + next;
        current = next;
        next = newNext;
        return result;
    }
}
