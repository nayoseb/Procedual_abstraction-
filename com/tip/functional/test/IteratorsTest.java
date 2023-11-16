package com.tip.functional.test;

import static com.tip.functional.Iterators.reduce;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tip.functional.Fibonacci;
import com.tip.functional.InfiniteIterator;
import com.tip.functional.Iterators;
import com.tip.functional.customexception.IllegalNullArgumentException;
import com.tip.functional.customexception.IteratorMaxSizeNegativeException;
import com.tip.functional.customexception.UnsupportedInfiniteIteratorException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.tip.Mathx;
import com.tip.functional.*;
import static com.tip.functional.Iterators.*;
import static com.tip.Mathx.*;

public class IteratorsTest {


    /*reduce Test 코드*/

    @Test
    @DisplayName("null 값인 Iterator를 toString 메서드에 파라미터로 넣을 시 IllegalNullArgumentException")
    void given_nullIterable_when_reduce_then_throwsNullPointerException() {
        //given
        Iterator<Integer> iterator = null;
        //when, then
        assertThrows(IllegalNullArgumentException.class, () -> Iterators.toString(iterator, ","));
    }


    @Test
    @DisplayName("reduce - null Iterator가 들어간 경우")
    void given_nullIter_when_reduceWithIterator_then_throwsNullPointerException() {
        //given
        Iterator<Integer> iterator = null;

        assertThrows(IllegalNullArgumentException.class, () -> reduce(iterator, Integer::sum, 0));
    }


    @Test
    @DisplayName("parameterdp Predicate조건을 추가한 InfiniteIterator reduce 테스트")
    void given_fibonacciInfiniteIteratorWithPredicate_when_reduce_then_returnAccumulatedResult() {
        // Fibonacci 인스턴스 생성
        Fibonacci fibonacci = new Fibonacci();

        // reduce를 사용하여 피보나치 수열의 합계 구하기 (처음 10개 요소의 합)
        int sum = reduce(fibonacci, (acc, n) -> acc + n, 0,
                acc -> fibonacci.getCurrent() > 55); // 55 이상이 되면 종료 (피보나치 10번째 요소)

        // 결과 검증
        assertEquals(143, sum); // 처음 10개 피보나치 수의 합은 143
    }

    @Test
    void given_nonInfiniteIterator_when_reduce_then_returnAccumulatedResult() {
        //given
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3, 4).iterator();
        BiFunction<Integer, Integer, Integer> sum = Integer::sum;

        //when
        int result = Iterators.reduce(iterator, sum, 0);

        //then
        assertEquals(10, result);
    }

    @Test
    void given_nullArguments_when_reduce_then_throwIllegalArgumentException() {
        //given
        Iterator<Integer> iterator = null;
        BiFunction<Integer, Integer, Integer> sum = Integer::sum;

        //when/then
        assertThrows(IllegalArgumentException.class, () -> Iterators.reduce(iterator, sum, 0));
    }

    @Test
    @DisplayName("Predicate parameter 없이 reduce 파라미터에 InfiniteIterator 입ㄹ력 시 UnsupportedInfiniteIteratorException 반환")
    void given_infiniteIterator_when_reduce_then_throwUnsupportedInfiniteIteratorException() {
        //given
        InfiniteIterator<Integer> infiniteIterator = Iterators.iterate(1, x -> x + 1);
        BiFunction<Integer, Integer, Integer> sum = Integer::sum;

        //when/then
        assertThrows(UnsupportedInfiniteIteratorException.class, () -> Iterators.reduce(infiniteIterator, sum, 0));
    }



    /*equals Test 코드*/

    @Test
    @DisplayName("동일한 요소를 가진 두 Iterator equals비교")
    void given_EqualIterators_when_CheckedForEquality_then_ReturnTrue() {
        // Given: 두 개의 동일한 값을 가진 반복자 생성
        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> it2 = Arrays.asList(1, 2, 3).iterator();

        }
        // When: 두 반복자의 동등성을 체크
        boolean isEqual = Iterators.equals(it1, it2);

        // Then: 결과가 true임을 확인
        assertTrue(isEqual);
    }

    @Test
    @DisplayName("동일하지 않은 요소를 가진 두 Iterator equals비교")
    void given_DifferentIterators_when_CheckedForEquality_then_ReturnFalse() {
        // Given: 두 개의 다른 값을 가진 반복자 생성
        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> it2 = Arrays.asList(4, 5, 6).iterator();
        // When: 두 반복자의 동등성을 체크
        boolean isEqual = Iterators.equals(it1, it2);

        // Then: 결과가 false임을 확인
        assertFalse(isEqual);
    }


    @Test
    @DisplayName("null 값인 Iterator와 equals 메서드에 파라미터로 넣을 시 IllegalNullArgumentException")
    void given_NullIterator_when_CheckedForEquality_then_ThrowsIllegalNullArgumentException() {
        // Given: 한 개의 null 값을 가진 Iterator 생성
        Iterator<Integer> it1 = null;
        Iterator<Integer> it2 = Arrays.asList(1, 2, 3).iterator();
        //When: 두 반복자의 동등성을 체크, Then: IllegalNullArgumentException가 던져지는 지 확인
        assertThrows(IllegalNullArgumentException.class, () -> Iterators.equals(it1, it2));
    }


        @Test
        void filterTest() {
                assertTrue(fibonacci() instanceof InfiniteIterator);
                Iterable<Integer> fib = Mathx::fibonacci;
                assertTrue(Iterators.equals(limit(fibonacci(), 10), StreamSupport
                                .stream(fib.spliterator(), false).limit(10).iterator()));

        }
}
