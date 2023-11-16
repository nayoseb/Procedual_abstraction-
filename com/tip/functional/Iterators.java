package com.tip.functional;

import com.tip.functional.customexception.IllegalNullArgumentException;
import com.tip.functional.customexception.IteratorMaxSizeNegativeException;
import com.tip.functional.customexception.UnsupportedInfiniteIteratorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Iterators {

    /**
     * 주어진 Iterable의 각 요소에 대해 BiFunction을 적용하여 결과를 축소(reduce)합니다.
     * 이 메서드는 초기 값과 함께 시작하여, Iterable의 각 요소에 대해 BiFunction을 순차적으로 적용합니다.
     *
     * @param <E>        Iterable의 요소 타입
     * @param <R>        reduce 결과의 타입
     * @param es         Iterable의 요소들
     * @param biFunction 두 인자 (축소된 결과와 Iterable의 현재 요소)를 받아 새로운 결과를 생성하는 함수
     * @param init       초기 값
     * @return Iterable의 모든 요소를 처리한 최종 결과값
     * @throws IllegalNullArgumentException es, biFunction 또는 init가 null인 경우 발생
     */
    public static <E, R> R reduce(Iterable<E> es, BiFunction<R, E, R> biFunction, R init) {
        nullCheckValidation("reduce", es, "Iterable<E> es)", biFunction, "BiFunction<R, E, R> biFunction", init,
                "R init");
        R result = init;
        for (E e : es) {
            result = biFunction.apply(result, e);
        }
        return result;
    }


    /**
     * 주어진 Iterator의 각 요소에 대해 BiFunction을 적용하여 결과를 축소(reduce)합니다.
     * 이 메서드는 초기 값과 함께 시작하여, Iterator의 각 요소에 대해 BiFunction을 순차적으로 적용합니다.
     * 무한 반복자(InfiniteIterator)는 이 메서드에서 지원되지 않습니다.
     *
     * @param <E>        Iterator의 요소 타입
     * @param <R>        축소 결과의 타입
     * @param es         Iterator의 요소들
     * @param biFunction 두 인자 (축소된 결과와 Iterator의 현재 요소)를 받아 새로운 결과를 생성하는 함수
     * @param init       축소 작업의 초기 값
     * @return Iterator의 모든 요소를 처리한 최종 결과값
     * @throws IllegalNullArgumentException         es, biFunction 또는 init가 null인 경우 발생
     * @throws UnsupportedInfiniteIteratorException 무한 반복자(InfiniteIterator)가 입력으로 제공되는 경우 발생
     */
    public static <E, R> R reduce(Iterator<E> es, BiFunction<R, E, R> biFunction, R init) {
        //여기서 nullCheckValidation를 하지 않는 이유는 Iterable객체를 입력받는 reduce를 호출하여 IllegalNullArgumentException을 검사하기 때문입니다.
//        nullCheckValidation("reduce", es, "Iterator<E> es", biFunction, "BiFunction<R, E, R> biFunction", init,
//                "R init");
        //Todo: InfiniteIterator가 아닌 Iterator hasnext메서드 값이 항상 true인 객체가 들어온다면 어떻게 막을까?
        if (es instanceof InfiniteIterator) {
            throw new UnsupportedInfiniteIteratorException(
                    "reduce: 무한 반복자는 이 연산에서 지원되지 않습니다.parameter에 Predicate를 추가하세요.");
        }
        return reduce(() -> es, biFunction, init);
    }

    /**
     * 무한 Iterator에 대해 BiFunction을 적용하여 결과를 축소(reduce)합니다.
     * 이 메서드는 초기 값과 함께 시작하여, InfiniteIterator의 각 요소에 대해 BiFunction을 적용합니다.
     * stopCondition에 따라 축소 작업이 중단될 때까지 계속됩니다.
     * InfiniteIterator가 들어오는 경우 무한루프를 돌기 때문에,
     * InfiniteIterator가 인자값으로 들어오는 경우 reduce 오버로딩, 파라미터로 Predicate받아서 조건 추가해서 무한루프 탈출합니다.
     *
     * @param <E>           InfiniteIterator의 요소 타입
     * @param <R>           축소 결과의 타입
     * @param es            InfiniteIterator의 요소들
     * @param biFunction    두 인자 (축소된 결과와 Iterator의 현재 요소)를 받아 새로운 결과를 생성하는 함수
     * @param init          초기 값
     * @param stopCondition reduce 작업을 중단할 조건을 정의하는 Predicate
     * @return InfiniteIterator의 요소를 처리한 후의 최종 결과값
     * @throws IllegalNullArgumentException es, biFunction, init, 또는 stopCondition 중 어느 하나라도 null인 경우 발생
     */
    public static <E, R> R reduce(InfiniteIterator<E> es, BiFunction<R, E, R> biFunction, R init,
                                  Predicate<R> stopCondition) {
        nullCheckValidation("reduce", es, "InfiniteIterator<E> es", biFunction, "BiFunction<R, E, R> biFunction", init,
                "R init");
        R result = init;
        while (es.hasNext()) {
            result = biFunction.apply(result, es.next());
            if (stopCondition.test(result)) {

                break;
            }
        }
        return result;
    }

    /**
     * 두 Iterator 객체를 비교하여 동일한 요소와 순서를 가지고 있는지 확인합니다.
     *
     * @param <T> 요소의 타입
     * @param xs  첫 번째 Iterator 객체
     * @param ys  두 번째 Iterator 객체
     * @return 두 Iterator가 동일한 순서와 요소를 가질 경우 true, 그렇지 않으면 false 반환
     * @throws IllegalNullArgumentException xs 또는 ys가 null인 경우 발생
     */
    public static <T> boolean equals(Iterator<T> xs, Iterator<T> ys) { // TODO: reduce, zip을 써서
        // xs나 ys가 null인 경우 Exception 던짐
        nullCheckValidation("equals", xs, "Iterator<E> xs", ys, "Iterator<T> ys");


        // 먼저 두 Iterator가 동일한 객체를 참조하는지 확인
        if (xs == ys) {
            return true;
        }

        // 두 Iterator의 요소들을 비교
        Boolean areAllPairsEqual = reduce(
                zip(Objects::equals, xs, ys),
                (acc, isEqual) -> acc && isEqual,
                true
        ) && !xs.hasNext() && !ys.hasNext();

        return areAllPairsEqual;
    }


    /**
     * Iterator의 모든 요소를 separator로 구분하여 하나의 문자열로 결합합니다.
     *
     * @param es        Iterator 객체
     * @param separator 요소들 사이에 삽입할 문자열
     * @param <T>       Iterator에 포함된 요소의 타입
     * @return Iterator의 모든 요소가 separator로 구분되어 결합된 문자열
     * @throws IllegalArgumentException es 또는 separator가 null인 경우 발생
     */
    public static <T> String toString(Iterator<T> es, String separator) { // TODO: reduce를 써서
        // es나 separator가 null인 경우 IllegalArgumentException을 던짐
        nullCheckValidation("toString", es, "Iterator<E> es", separator, "String separator");

        if (es == null || separator == null) {
            throw new IllegalArgumentException("toString: Iterator 또는 구분자로 null 값이 들어올 수 없습니다.");
        }

        //Iterator가 비어 있다면 공백 반환
        if (!es.hasNext()) {
            return "";
        }

        //비어 있지 않다면 첫 번째 요소를 처리하여 초기 문자열로 사용
        T firstElement = es.next();
        // reduce를 사용하여 나머지 요소를 처리하고, 각 요소를 문자열에 separator와 함게 추가함
        return reduce(es,
                (acc, element) -> acc + separator + element.toString(),
                firstElement.toString());
    }

    /**
     * Iterator의 각 요소에 Function을 적용하여 매핑된 결과를 포함하는 새 Iterator를 반환합니다.
     * 이 메서드는 원본 Iterator의 각 요소에 주어진 함수를 적용하여, 변환된 결과를 포함하는 새로운 Iterator를 생성합니다.
     *
     * @param <E>      원본 Iterator의 요소 타입
     * @param <R>      매핑된 결과의 요소 타입
     * @param es       원본 Iterator
     * @param function 각 요소에 적용할 변환 함수
     * @return 각 요소가 주어진 함수에 의해 변환된 새로운 Iterator
     * @throws IllegalNullArgumentException es 또는 function이 null인 경우 발생
     */
    public static <E, R> Iterator<R> map(Iterator<E> es, Function<E, R> function) {
        return new Iterator<R>() {
            public boolean hasNext() {
                return es.hasNext();
            }

            public R next() {
                return function.apply(es.next());
            }
        };
    }

    /**
     * Iterator에서 특정 조건(predicate)에 맞는 요소들만 필터링하여 새로운 Iterator로 반환합니다
     *
     * @param iterator  필터링할 요소들을 포함하고 있는 원본 Iterator
     * @param predicate 조건을 정의하는 Predicate
     * @param <E>       Iterator에 포함된 요소의 타입
     * @return 특정 조건(predicate)에 맞는 요소들만 필터링한 Iterator
     * @throws IllegalArgumentException iterator 또는 predicate가 null인 경우 발생
     * @throws NoSuchElementException   다음 요소가 존재하지 않을 때 next()를 호출하면 발생
     */
    public static <E> Iterator<E> filter(Iterator<E> iterator, Predicate<E> predicate) {
        // TODO: Bug를 찾을 수 있는 test code를 IteratorTest.filterTest에 쓰고, Bug 고치기
        // findFirst를 써서 풀기
        nullCheckValidation("filter", iterator, "Iterator<E> iterator", predicate, "Predicate<E> predicate");
        //next 메서드를 호출하지 않을 시 Iterator객체가 생기기 때문에 filter에서도 nullcheck 필요!
        return new Iterator<E>() {
            // findFirst를 호출 'next'를 초기화, predicate에 일치하는 첫 번째 요소로 설정
            private E next = findFirst(iterator, predicate);

            public boolean hasNext() {
                return next != null;
            }

            public E next() {
                if (next == null) {
                    throw new NoSuchElementException("filter");
                }
                E current = next;
                // 다음 next() 호출을 위해 predicate에 일치하는 다음 요소를 찾음
                next = findFirst(iterator, predicate);
                return current;
            }
        };

    }

/*    //함수형 프로그래밍으로 작성한 filter
    public static <E> Iterator<E> filter(Iterator<E> iterator, Predicate<E> predicate) {
        // Iterator를 Stream으로 변환
        return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(iterator, 0), false)
                // Stream에 filter 적용
                .filter(predicate)
                // 필터링된 Stream을 Iterator로 변환하여 반환
                .iterator();
    }*/

    //private으로 밖에서 쓸 수 없게 함 -> nullcheck 안해도 됌. filter에서 nullcheck를 시행하기 때문에
    private static <E> E findFirst(Iterator<E> iterator, Predicate<E> predicate) {
        while (iterator.hasNext()) {
            E first = iterator.next();
            if (predicate.test(first)) {
                return first;
            }
        }
        return null;
    }

    /**
     * 초기 요소(seed)와 UnaryOperator를 사용하여 무한 Iterator를 생성합니다.
     * 이 Iterator는 'next' 메서드를 호출할 때마다 UnaryOperator를 현재 요소에 적용하여 다음 요소를 생성합니다.
     *
     * @param <T>  Iterator에 포함될 요소의 타입
     * @param seed 무한 Iterator의 시작 요소
     * @param f    현재 요소에 적용할 UnaryOperator 함수
     * @return UnaryOperator를 적용하여 생성된 무한 요소들을 포함하는 InfiniteIterator
     * @throws IllegalNullArgumentException seed 또는 f가 null인 경우 발생
     */
    public static <T> InfiniteIterator<T> iterate(T seed, UnaryOperator<T> f) {
        return new InfiniteIterator<T>() {
            T current = seed;

            @Override
            public T next() {
                T old = current;
                current = f.apply(current);
                return old;
            }
        };
    }

    /**
     * Iterator에서 maxSize 개수만큼의 요소를 포함하는 Iterator를 반환합니다
     *
     * @param iterator 요소들을 포함하고 있는 원본 Iterator
     * @param maxSize  반환할 최대 요소의 개수. 이 값은 음수가 될 수 없습니다.
     * @param <T>      Iterator에 포함된 요소의 타입
     * @return 원본 Iterator에서 최대 maxSize 만큼의 요소를 반환하는 새로운 Iterator. maxSize가 원본 Iterator의 크기보다 크면, 원본 Iterator의 모든 요소를 반환합니다.
     * @throws IllegalArgumentException 만약 iterator가 null이거나 maxSize가 음수일 경우 발생합니다.
     */
    public static <T> Iterator<T> limit(Iterator<T> iterator, long maxSize) { // TODO
        //주어진 최대 크기만큼의 요소를 포함하는 새 Iterator를 반환
        nullCheckValidation("limit", iterator, "Iterator<E> iterator");

        //음수가 들어오는 경우
        if (maxSize < 0) {
            throw new IteratorMaxSizeNegativeException("limit: maxsize로 음수는 들어올 수 없습니다.");
        }

        return new Iterator<T>() {
            private long count = 0;

            //아직 반환해야 할 요소가 남아 있고 (즉, count가 maxSize보다 작음) 원래 Iterator에도 요소가 남아 있는 경우에만 true를 반환합니다.
            @Override
            public boolean hasNext() {
                return count < maxSize && iterator.hasNext();
            }

            // count가 maxSize 이상인 경우 요소를 반환하면 안 되므로 예외를 발생시킵니다. 그렇지 않은 경우 요소를 반환하기 전에 count를 증가시킵니다.
            @Override
            public T next() {
                if (count >= maxSize) {
                    throw new NoSuchElementException();
                }
                count++;
                return iterator.next();
            }
        };


    }

    /**
     * 주어진 Supplier 함수를 사용하여 값을 무한히 생성하는 InfiniteIterator를 반환합니다.
     * 이 Iterator는 'next' 메서드가 호출될 때마다 Supplier에 의해 제공된 새로운 값을 반환합니다.
     *
     * @param <T>      생성될 값의 타입
     * @param supplier 각 'next' 호출에 대해 새로운 값을 제공하는 Supplier 함수
     * @return Supplier에 의해 생성된 값을 무한히 반환하는 InfiniteIterator
     * @throws IllegalNullArgumentException supplier가 null인 경우 발생
     */
    //Supplier 함수를 사용하여 값을 무한히 생성하는 함수
    public static <T> InfiniteIterator<T> generate(Supplier<T> supplier) { // TODO:
        nullCheckValidation("generate", supplier, "Supplier<T> supplier");
        return new InfiniteIterator<T>() {
            //next메서드를 호출할 때마다 새로운 값을 반환
            @Override
            public T next() {
                return supplier.get();
            }
        };
    }

    /**
     * 두 Iterator의 각 요소를 결합하여 새로운 값을 생성하는 Iterator를 반환합니다.
     * 이 Iterator는 각각의 xIterator와 yIterator에서 요소를 하나씩 가져와서,
     * 주어진 biFunction을 이용하여 새로운 값 Z를 생성합니다.
     *
     * @param <X>        첫 번째 Iterator의 요소 타입
     * @param <Y>        두 번째 Iterator의 요소 타입
     * @param <Z>        결과 Iterator의 요소 타입
     * @param biFunction 두 요소 X와 Y를 결합하여 새로운 요소 Z를 생성하는 함수
     * @param xIterator  첫 번째 Iterator
     * @param yIterator  두 번째 Iterator
     * @return 두 Iterator의 요소를 결합하여 생성된 새로운 요소 Z를 포함하는 Iterator
     * @throws IllegalNullArgumentException biFunction, xIterator 또는 yIterator가 null인 경우 발생
     * @throws NoSuchElementException       xIterator 또는 yIterator 중 하나가 더 이상 요소를 가지고 있지 않을 때 발생
     */
    public static <X, Y, Z> Iterator<Z> zip(BiFunction<X, Y, Z> biFunction, Iterator<X> xIterator,
                                            Iterator<Y> yIterator) {
        nullCheckValidation("zip", biFunction, "BiFunction<X, Y, Z> biFunction", xIterator, "Iterator<E> iterator",
                yIterator, "Iterator<Y> yIterator");
        return new Iterator<Z>() {
            public boolean hasNext() {
                return xIterator.hasNext() && yIterator.hasNext();
            }

            public Z next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("zip");
                }
                return biFunction.apply(xIterator.next(), yIterator.next());
            }
        };
    }

    /**
     * 주어진 Iterator에 포함된 요소의 총 개수를 반환합니다.
     * 이 메서드는 Iterator의 모든 요소를 순회하며 각 요소에 대해 카운트를 1씩 증가시킵니다.
     *
     * @param <E>      Iterator의 요소 타입
     * @param iterator 요소의 개수를 세고자 하는 Iterator
     * @return Iterator에 포함된 요소의 총 개수
     * @throws IllegalNullArgumentException iterator가 null인 경우 발생
     */
    //iterator에 포함된 요소의 수를 반환하는 메서드
    public static <E> long count(Iterator<E> iterator) {
        nullCheckValidation("count", iterator, "Iterator<E> iterator");
        // TODO: reduce를 써서
        return reduce(iterator, (acc, e) -> acc + 1, 0L);

    }

    /**
     * 주어진 Iterator에서 특정 인덱스에 위치한 요소를 반환합니다.
     * 이 메서드는 Iterator를 순회하면서 지정된 인덱스에 도달할 때까지 각 요소를 건너뜁니다.
     *
     * @param <T>      Iterator에 포함된 요소의 타입
     * @param iterator 요소를 가져오고자 하는 Iterator
     * @param index    가져오고자 하는 요소의 인덱스 (0부터 시작)
     * @return 지정된 인덱스에 위치한 요소
     * @throws IllegalNullArgumentException iterator가 null인 경우 발생
     * @throws IndexOutOfBoundsException    인덱스가 음수이거나 Iterator의 크기를 초과하는 경우 발생
     */
    public static <T> T get(Iterator<T> iterator, long index) {
        nullCheckValidation("get", iterator, "Iterator<E> iterator");
        if (index < 0) {
            throw new IndexOutOfBoundsException("get: index < " + index);
        }
        return getLast(limit(iterator, index + 1));
    }

    //private으로 선언해서 nullcheck 불필요
    private static <T> T getLast(Iterator<T> iterator) {
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return current;
            }
        }
    }

    public static <T> List<T> toList(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <E> void print(Iterator<E> iterator, String separator,
                                 java.io.PrintStream printStream) {
        printStream.print(toString(iterator, separator));
    }

    public static <E> void print(Iterator<E> iterator, String separator) {
        print(iterator, separator, System.out);
    }

    public static <E> void println(Iterator<E> iterator, String separator,
                                   java.io.PrintStream printStream) {
        print(iterator, separator, printStream);
        printStream.println();
    }

    public static <E> void println(Iterator<E> iterator, String separator) {
        println(iterator, separator, System.out);
    }

    public static <E> void println(Iterator<E> iterator) {
        println(iterator, ", ");
    }

    private Iterators() {
    }


    /**
     * 파라미터가 null값인지 체크하고 IllegalNullArgumentException을 던져주는 메서드입니다.
     *
     * @param objectsAndNames 순서에 따라 파라미터에 들어오는 객체, 이름 순으로 받습니다.
     */
    private static void nullCheckValidation(Object... objectsAndNames) {
        String methodName = objectsAndNames[0].toString();

        for (int i = 1; i < objectsAndNames.length; i += 2) {
            Object obj = objectsAndNames[i];
            String name = (String) objectsAndNames[i + 1];

            if (obj == null) {
                /*
                이전에는 파라미터에 잘못된 값이 들어와서 생기는 예외라서 IllegalArgumentException을 던져야 한다고 생각햇지만
                https://stackoverflow.com/a/14104551 글을 보고 생각이 바뀌었습니다.
                Null값이 있을 때 발생하는 오류에는 NullPointerException가 더 구체적인 예외처리라고 하는 사람들도 있습니다.
                두 exception 모두 괜찮은 거 같아 더 명확한 IllegalNullArgumentException이라는 custom 예외처리를 만들어 던졌습니다.
                */
                throw new IllegalNullArgumentException(methodName + ": " + name + " 값이 null로 들어올 수 없습니다.");
            }
        }
    }

}


