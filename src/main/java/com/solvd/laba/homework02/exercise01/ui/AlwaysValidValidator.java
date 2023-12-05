package com.solvd.laba.homework02.exercise01.ui;

public class AlwaysValidValidator<T> implements IValidator<T> {
    @Override
    public boolean isValid(T obj) {
        return true;
    }
}
