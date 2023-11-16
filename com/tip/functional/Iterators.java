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
   * @throws IllegalNullArgumentException es, biFunction 또는 init가 null인 경우 발생
   * @throws IllegalArgumentException     무한 반복자(InfiniteIterator)가 입력으로 제공되는 경우 발생
   */
  public static <E, R> R reduce(Iterator<E> es, BiFunction<R, E, R> biFunction, R init) {
    nullCheckValidation("reduce", es, "Iterator<E> es", biFunction, "BiFunction<R, E, R> biFunction", init,
            "R init");
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
      private E current;

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

  public static <E> E findFirst(Iterator<E> iterator, Predicate<E> predicate) {
    while (iterator.hasNext()) {
      E first = iterator.next();
      if (predicate.test(first))
        return first;
    }
    return null;
  }

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

  public static <T> Iterator<T> limit(Iterator<T> iterator, long maxSize) { // TODO

  }

  public static <T> InfiniteIterator<T> generate(Supplier<T> supplier) { // TODO:

  }

  public static <X, Y, Z> Iterator<Z> zip(BiFunction<X, Y, Z> biFunction, Iterator<X> xIterator,
      Iterator<Y> yIterator) {
    return new Iterator<Z>() {
      public boolean hasNext() {
        return xIterator.hasNext() && yIterator.hasNext();
      }

      public Z next() {
        if (!hasNext())
          throw new NoSuchElementException("zip");
        return biFunction.apply(xIterator.next(), yIterator.next());
      }
    };
  }

  public static <E> long count(Iterator<E> iterator) {
    // TODO: reduce를 써서
  }

  public static <T> T get(Iterator<T> iterator, long index) {
    if (index < 0)
      throw new IndexOutOfBoundsException("index < " + index);
    return getLast(limit(iterator, index + 1));
  }

  private static <T> T getLast(Iterator<T> iterator) {
    while (true) {
      T current = iterator.next();
      if (!iterator.hasNext())
        return current;
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


