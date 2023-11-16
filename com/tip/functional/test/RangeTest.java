package com.tip.functional.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tip.functional.Range;
import com.tip.functional.customexception.InvalidRangeException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RangeTest {


    @Test
    @DisplayName("Range 기능 검사 시 올바른 값 반환")
    void given_range_when_checkedFunctions_then_returnCorrectValues() {
        // TODO: 모든 기능을 고르게 테스트 할 수 있는 코드 적어보기
        //given
        Range range = new Range(1, 5);

        //when
        long max = range.max();
        long min = range.min();
        long end = range.end();
        long size = range.size();

        //then
        assertEquals(4, max);
        assertEquals(1, min);
        assertEquals(5, end);
        assertEquals(4, size);


    }

    @Test
    @DisplayName("범위 순환 시 연속된 요소 반환")
    void given_range_when_iterated_then_returnSequenceElements() {
        // TODO: 모든 기능을 고르게 테스트 할 수 있는 코드 적어보기
        //given
        Range range = new Range(1, 5);

        //when
        Iterator<Long> iterator = range.iterator();
        long next1 = iterator.next();
        long next2 = iterator.next();
        long next3 = iterator.next();
        long next4 = iterator.next();

        //then
        assertEquals(Long.valueOf(1), next1);
        assertEquals(Long.valueOf(2), next2);
        assertEquals(Long.valueOf(3), next3);
        assertEquals(Long.valueOf(4), next4);
//        assertFalse(iterator.hasNext());


    }


    @Test
    @DisplayName("잘못된 범위 설정")
    void given_wrongStartGreaterThanEnd_when_creatingRange_then_throwIllegalArgumentException() {
        //given
        long wrongStart = 10;
        long end = 5;
        //when
        InvalidRangeException exception = assertThrows(InvalidRangeException.class, () -> {
            new Range(wrongStart, end);
        });
        //then
        assertTrue(exception.getMessage().contains("Range"));
    }

    @DisplayName("빈 iterator에서 요소 빼기")
    @Test
    void given_exhaustedIterator_when_nextCalled_then_throwNoSuchElementException() {
        //given
        Range range = new Range(1, 5);
        //when
        Iterator<Long> iterator = range.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        //then
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @DisplayName("닫힌구간 테스트")
    void given_closedRange_when_checkMinAndCheckMax_then_returnTrue() {
        //given
        Range range = Range.closed(3, 7);
        //when
        long Min = range.min();
        long Max = range.max();
        //then
        assertEquals(3, Min);
        assertEquals(7, Max);

    }
}
