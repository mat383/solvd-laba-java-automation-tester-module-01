package com.solvd.laba.homework02.exercise01.util;

public interface IAllocationStrategy {

    /**
     * calculate initial size of the collection given requested size
     *
     * @param requestedSize
     * @return
     */
    public int initialSize(int requestedSize);

    /**
     * calculate new size when collection is about to exceed it's current capacity
     *
     * @param requestedSize
     * @param currentSize
     * @return
     */
    public int expandedSize(int requestedSize, int currentSize);
}
