package com.solvd.laba.homework02.exercise01.util;

public class ExponentialAllocationStrategy implements IAllocationStrategy {
    @Override
    public int initialSize(int requestedSize) {
        return requestedSize;
    }

    @Override
    public int expandedSize(int requestedSize, int currentSize) {
        return requestedSize * 2;
    }
}
