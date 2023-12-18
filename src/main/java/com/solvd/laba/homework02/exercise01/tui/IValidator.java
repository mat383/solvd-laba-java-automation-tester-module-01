package com.solvd.laba.homework02.exercise01.tui;

@FunctionalInterface
public interface IValidator<T> {
    public boolean isValid(T obj);
}
