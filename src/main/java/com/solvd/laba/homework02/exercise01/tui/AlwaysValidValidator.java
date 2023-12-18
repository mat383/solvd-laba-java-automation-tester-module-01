package com.solvd.laba.homework02.exercise01.tui;

public class AlwaysValidValidator<T> implements IValidator<T> {
    @Override
    public boolean isValid(T obj) {
        return true;
    }
}
