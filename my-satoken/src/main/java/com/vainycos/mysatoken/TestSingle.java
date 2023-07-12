package com.vainycos.mysatoken;

import java.util.function.BiFunction;

/**
 * @author: Vainycos
 * @description
 * @date: 2022/12/16 17:08
 */
public class TestSingle {

    public static final TestSingle me = new TestSingle();

    public BiFunction<Object, String, String> test = (id, name) -> {
        return name;
    };
}
