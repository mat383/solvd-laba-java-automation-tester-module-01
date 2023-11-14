package com.solvd.laba.homework02.exercise01.util;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayBasedLinkedList<E> implements List<E> {

    public class ArrayBasedLinkedListIterator implements Iterator<E> {
        private int currentElementIndex = ArrayBasedLinkedList.this.firstNode;

        @Override
        public boolean hasNext() {
            return currentElementIndex != EMPTY_NODE
                    && ArrayBasedLinkedList.this.nodesNextIndex[currentElementIndex] != ArrayBasedLinkedList.EMPTY_NODE;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Collection have no next element");
            }
            this.currentElementIndex = ArrayBasedLinkedList.this.nodesNextIndex[currentElementIndex];
            return (E) ArrayBasedLinkedList.this.nodes[currentElementIndex];
        }
    }


    private static IAllocationStrategy defaultAllocationStrategy = new ExponentailAllocationStrategy();

    /**
     * special index used to indicate empty node
     * equivalent of null reference used for first and last next/prev node index
     */
    public static final int EMPTY_NODE = -1;

    private boolean isNodesArraySorted;
    /**
     * linked list nodes
     */
    private Object[] nodes;
    /**
     * indices of next nodes
     */
    private int[] nodesNextIndex;
    /**
     * indices of previous nodes
     */
    private int[] nodesPrevIndex;
    private boolean[] nodesIsEmpty;
    private int firstNode;
    private int lastNode;
    private int size;
    private final IAllocationStrategy allocationStrategy;


    /**
     * 'main' constructor, used by the rest of the constructors
     * it allocates all arrays and sets proper values
     *
     * @param requestedInitialCapacity initial requested capacity of internal array used for storing nodes of list
     *                                 real initial capacity might be different and depends on allocationStrategy
     * @param allocationStrategy       tells class how much memory to allocate
     */
    public ArrayBasedLinkedList(int requestedInitialCapacity, IAllocationStrategy allocationStrategy) {
        if (allocationStrategy == null) {
            throw new IllegalArgumentException("allocationStrategy cannot be null");
        }
        this.allocationStrategy = allocationStrategy;
        int initialCapacity = allocationStrategy.initialSize(requestedInitialCapacity);

        this.nodes = new Object[initialCapacity];
        this.nodesNextIndex = new int[initialCapacity];
        Arrays.fill(this.nodesNextIndex, EMPTY_NODE);
        this.nodesPrevIndex = new int[initialCapacity];
        Arrays.fill(this.nodesPrevIndex, EMPTY_NODE);
        this.nodesIsEmpty = new boolean[initialCapacity];
        Arrays.fill(this.nodesIsEmpty, true);
        this.size = 0;
        this.firstNode = EMPTY_NODE;
        this.lastNode = EMPTY_NODE;
        this.isNodesArraySorted = false;
    }

    public ArrayBasedLinkedList(int requestedInitialCapacity) {
        this(requestedInitialCapacity, ArrayBasedLinkedList.defaultAllocationStrategy);
    }

    public ArrayBasedLinkedList() {
        this(0);
    }

    public ArrayBasedLinkedList(Collection<? extends E> c, IAllocationStrategy allocationStrategy) {
        this(c.size(), allocationStrategy);
        this.addAll(c);
        this.isNodesArraySorted = checkIsNodesArraySorted();
    }

    public ArrayBasedLinkedList(Collection<? extends E> c) {
        this(c, ArrayBasedLinkedList.defaultAllocationStrategy);
    }


    public static IAllocationStrategy getDefaultAllocationStrategy() {
        return ArrayBasedLinkedList.defaultAllocationStrategy;
    }

    public static void setDefaultAllocationStrategy(IAllocationStrategy allocationStrategy) {
        if (allocationStrategy == null) {
            throw new IllegalArgumentException("allocationStrategy cannot be null");
        }
        ArrayBasedLinkedList.defaultAllocationStrategy = allocationStrategy;
    }

    // methods overloading List interface
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size < 1;
    }

    @Override
    public boolean contains(Object o) {
        for (Object e : this) {
            if (o == null && e == null) {
                return true;
            } else if (o != null && o.equals(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayBasedLinkedListIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        int i = 0;
        for (Object e : this) {
            array[i] = e;
            ++i;
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < this.size) {
            a = (T1[]) Array.newInstance(a.getClass().getComponentType(), this.size);
        } else if (a.length > this.size) {
            Arrays.fill(a, null);
        }
        int i = 0;
        for (Object e : this) {
            a[i] = (T1) e;
            ++i;
        }
        return a;
    }

    /**
     * add element to the end of the list
     *
     * @param e element whose presence in this collection is to be ensured
     * @return
     */
    @Override
    public boolean add(E e) {
        // check if internal array needs expanding
        if (this.nodes.length - this.size < 1) {
            expand(1);
        }

        int newIndex = findEmptyNode();
        this.nodesIsEmpty[newIndex] = false;
        this.nodesNextIndex[this.lastNode] = newIndex;
        this.nodesPrevIndex[newIndex] = this.lastNode;
        this.nodesNextIndex[newIndex] = EMPTY_NODE;
        this.lastNode = newIndex;
        this.firstNode = this.firstNode == EMPTY_NODE ? newIndex : this.lastNode;
        this.size++;
        
        return true;
    }

    @Override
    public boolean remove(Object o) {
        // TODO implement
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO implement
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO implement
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // TODO implement
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO implement
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO implement
        return false;
    }

    @Override
    public void clear() {
        // TODO implement

    }

    @Override
    public E get(int index) {
        // TODO implement
        return null;
    }

    @Override
    public E set(int index, E element) {
        // TODO implement
        return null;
    }

    @Override
    public void add(int index, E element) {
        // TODO implement

    }

    @Override
    public E remove(int index) {
        // TODO implement
        return null;
    }

    @Override
    public int indexOf(Object o) {
        // TODO implement
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO implement
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO implement
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // TODO implement
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // TODO implement
        return null;
    }


    // linked list specific methods

    // arraylist spacific methods


    // helper methods
    private int findEmptyNode() {
        if (this.nodes.length >= this.size) {
            // TODO add custom exception type
            throw new NoSuchElementException("trying to find free node when there is none");
        }
        // try to find empty space after last item
        // use max to make sure that starting position is valid index
        for (int i = Math.max(this.lastNode, 0); i < this.nodes.length; ++i) {
            if (this.nodesIsEmpty[i]) {
                return i;
            }
        }
        // try to find free space before last node
        for (int i = 0; i < Math.max(this.lastNode, 0); ++i) {
            if (this.nodesIsEmpty[i]) {
                return i;
            }
        }

        throw new NoSuchElementException("trying to find free node when there is none");
    }

    private void expand(int expansionSize) {
        if (expansionSize < 0) {
            throw new IllegalArgumentException("expansionSize cannot be negative");
        }
        int newSize = this.allocationStrategy.expandedSize(this.nodes.length + expansionSize, this.nodes.length);

        // allocate
        Object[] newNodes = new Object[newSize];
        int[] newNodesNextIndex = new int[newSize];
        Arrays.fill(newNodesNextIndex, EMPTY_NODE);
        int[] newNodesPrevIndex = new int[newSize];
        Arrays.fill(newNodesPrevIndex, EMPTY_NODE);
        boolean[] newNodesIsEmpty = new boolean[newSize];
        Arrays.fill(newNodesIsEmpty, true);

        // transfer
        System.arraycopy(this.nodes, 0, newNodes, 0, this.nodes.length);
        System.arraycopy(this.nodesNextIndex, 0, newNodesNextIndex, 0, this.nodesNextIndex.length);
        System.arraycopy(this.nodesPrevIndex, 0, newNodesPrevIndex, 0, this.nodesPrevIndex.length);
        System.arraycopy(this.nodesIsEmpty, 0, newNodesIsEmpty, 0, this.nodesIsEmpty.length);

        // reassign
        this.nodes = newNodes;
        this.nodesNextIndex = newNodesNextIndex;
        this.nodesPrevIndex = newNodesPrevIndex;
        this.nodesIsEmpty = newNodesIsEmpty;
    }

    private boolean checkIsNodesArraySorted() {
        // TODO implement
        return false;
    }

    public void sortNodesArray() {
        // TODO implement
    }

    public boolean isNodesArraySorted() {
        return this.isNodesArraySorted;
    }
}
