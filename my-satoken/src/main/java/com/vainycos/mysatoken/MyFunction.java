package com.vainycos.mysatoken;
@FunctionalInterface
public interface MyFunction<T> {

    /**
     * 做些什么...
     * @param t
     */
    T doSomething(T t);

}
