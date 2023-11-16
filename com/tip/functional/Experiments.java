package com.tip.functional;

import java.util.Iterator;

public class Experiments<T extends Number> implements Iterator<T> {
    private final Iterator<T> internalIterator; // 내부 반복자
    private final String herbAvailabilities; // 이름
    private final String distributionDescription; // 분포 설명
    private int count = 0; // 계수
    private double sum = 0; // 합계

    public Experiments(Iterator<T> internalIterator, String herbAvailabilities, String distributionDescription) {
        this.internalIterator = internalIterator;
        this.herbAvailabilities = herbAvailabilities;
        this.distributionDescription = distributionDescription;
    }

    @Override
    public boolean hasNext() {
        // InfiniteIterator처럼 동작
        return true;
    }

    @Override
    public T next() {
        // 반복자가 제공하는 값의 계수와 합계를 추적하려고 합니다.
        T value = internalIterator.next();
        count++;
        sum += value.doubleValue();
        return value;
    }

    public void report() {
        // 분포 설명과 처리된 요소의 계수, 평균값을 출력하는 간단한 보고 메서드입니다.
        System.out.println(herbAvailabilities + " - " + distributionDescription);
        System.out.println("계수: " + count);
        System.out.println("합계: " + sum);
        if (count > 0) {
            System.out.println("평균: " + (sum / count));
        }
    }

}
