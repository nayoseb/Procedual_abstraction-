package com.tip.functional;

public interface InfiniteIterator<T> extends java.util.Iterator<T> {
    // TODO: 채우기
    @Override
    default boolean hasNext() {
        return true; // 항상 true를 반환
    }
}

