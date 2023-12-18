package com.solvd.laba.homework02.exercise01.tui;

import java.util.List;


@FunctionalInterface
public interface IListFilter<T> {
    public List<T> filter(List<T> list);
}
