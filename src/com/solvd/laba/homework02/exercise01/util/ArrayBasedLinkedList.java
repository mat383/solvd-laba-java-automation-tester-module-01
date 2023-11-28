package com.solvd.laba.homework02.exercise01.util;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayBasedLinkedList<E> implements List<E> {


    private static IAllocationStrategy defaultAllocationStrategy = new ExponentialAllocationStrategy();

    /**
     * special index used to indicate empty node
     * equivalent of null reference used for first and last next/prev node index
     */
    public static final int EMPTY_NODE = -1;

    /**
     * indicates whether it can be guaranteed that
     * internal nodes array is ordered in a way
     * that each node is at index corresponding
     * to its position in list
     */
    private boolean isNodesArraySorted;
    /**
     * nodes of linked
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
        if (initialCapacity < requestedInitialCapacity) {
            throw new AllocationStrategyProvidedSizeNotSufficientException(
                    "Requested initial capacity: %d, AllocationStrategy suggested capacity: %d"
                            .formatted(requestedInitialCapacity, initialCapacity));
        }

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
        this.isNodesArraySorted = true;
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
            if (Objects.equals(o, e)) {
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

        // if you didn't add the element at the end of internal array
        // then it becomes unordered (size was increased in previous line)
        if (newIndex != (this.size - 1)) {
            this.isNodesArraySorted = false;
        }

        return true;
    }


    @Override
    public void add(int index, E element) {
        // if adding at the end then just use add
        if (index == this.size) {
            add(element);
            return;
        }

        // check if internal array needs expanding
        if (this.nodes.length - this.size < 1) {
            expand(1);
        }

        int newIndex = findEmptyNode();
        int nextIndex = findInternalPosition(index);
        int prevIndex = this.nodesPrevIndex[nextIndex];

        this.nodesIsEmpty[newIndex] = false;
        this.nodesPrevIndex[newIndex] = prevIndex;
        this.nodesNextIndex[newIndex] = nextIndex;

        if (prevIndex != EMPTY_NODE) {
            this.nodesNextIndex[prevIndex] = newIndex;
        } else {
            this.firstNode = newIndex;
        }

        // next element cannot be empty, see first if
        this.nodesPrevIndex[nextIndex] = newIndex;

        this.size++;
        // there is no way to guarantee that internal array is ordered without calling checkIsNodesArraySorted
        this.isNodesArraySorted = false;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || this.size <= index) {
            throw new ArrayIndexOutOfBoundsException("Index " + Integer.toString(index) + " out of bounds");
        }

        int nodesIndex = findInternalPosition(index);
        if (nodesIndex == EMPTY_NODE) {
            throw new RuntimeException("Unable to find specified index: " + Integer.toString(index));
        }

        return removeAtIntervalPosition(nodesIndex);
    }

    @Override
    public boolean remove(Object o) {
        int nodesIndex = findInternalPosition(o);

        if (nodesIndex == EMPTY_NODE) {
            return false;
        } else {
            removeAtIntervalPosition(nodesIndex);
            return true;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO improve this implementation
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E obj : c) {
            add(obj);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // TODO improve this implementation
        int addAt = index;
        for (E obj : c) {
            add(addAt, obj);
            addAt++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO improve implementation
        for (Object obj : c) {
            remove(obj);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (int i = 0; i < this.nodes.length; ++i) {
            if (this.nodesIsEmpty[i]) {
                continue;
            }
            if (!c.contains(this.nodes[i])) {
                removeAtIntervalPosition(i);
            }
        }
        return true;
    }

    @Override
    public void clear() {
        Arrays.fill(this.nodes, null);
        Arrays.fill(this.nodesNextIndex, EMPTY_NODE);
        Arrays.fill(this.nodesPrevIndex, EMPTY_NODE);
        Arrays.fill(this.nodesIsEmpty, true);
        this.size = 0;
        this.firstNode = EMPTY_NODE;
        this.lastNode = EMPTY_NODE;
        this.isNodesArraySorted = true;
    }

    @Override
    public E get(int index) {
        return (E) this.nodes[findInternalPosition(index)];
    }

    @Override
    public E set(int index, E element) {
        int internalPosition = findInternalPosition(index);
        E prevValue = (E) this.nodes[internalPosition];
        this.nodes[internalPosition] = element;
        return prevValue;
    }


    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Object e : this) {
            if (Objects.equals(e, o)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = 0;
        int lastIndex = -1;
        for (Object e : this) {
            if (Objects.equals(e, o)) {
                lastIndex = index;
            }
            index++;
        }
        return lastIndex;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ArrayBasedLinkedListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayBasedLinkedListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // TODO implement
        /*
        when iterating maybe use this.last/firstNode to check boundaries
        then it would be easy to create sublist with private constructor that
        passes gets internal nodes array with modified last/firstNode
         */
        return null;
    }


    // linked list specific methods

    // arraylist specific methods


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
        int requestedSize = this.nodes.length + expansionSize;
        int newSize = this.allocationStrategy.expandedSize(requestedSize, this.nodes.length);
        if (newSize < requestedSize) {
            throw new AllocationStrategyProvidedSizeNotSufficientException(
                    "New size (%d) is smaller than requested size(%d)"
                            .formatted(newSize, requestedSize));
        }

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


    /**
     * find position of element with given index in internal array of nodes
     * return EMPTY_NODE if not found
     *
     * @param index position of element in list
     * @return position if found, EMPTY_NODE otherwise
     */
    private int findInternalPosition(int index) {
        if (index < 0 || this.size <= index) {
            throw new IndexOutOfBoundsException(
                    "Index %d if of out bound (array size is %d)".formatted(index, this.size));

        }
        if (this.isNodesArraySorted) {
            return index;
        }

        int currentPos = this.firstNode;
        int nodeIndex = 0;
        while (currentPos != EMPTY_NODE) {
            if (nodeIndex == index) {
                return currentPos;
            }
            currentPos = this.nodesNextIndex[currentPos];
            nodeIndex++;
        }
        return EMPTY_NODE;
    }

    /**
     * find position of first occurrence of Object o in internal array of nodes
     * return EMPTY_NODE if not found
     *
     * @param o object to be fount
     * @return position if found, EMPTY_NODE otherwise
     */
    private int findInternalPosition(Object o) {
        // search directly in nodes array
        int currentPos = this.firstNode;
        int nodeIndex = 0;
        while (currentPos != EMPTY_NODE) {
            if (Objects.equals(o, this.nodes[currentPos])) {
                return currentPos;
            }
            currentPos = this.nodesNextIndex[currentPos];
            nodeIndex++;
        }

        // TODO consider throwing NoSuchElementException
        return EMPTY_NODE;
    }

    /**
     * remove element identified by its index in nodes array
     *
     * @param internalIndex index of element in nodes array (different from index of this list)
     * @return reference to removed element
     */
    private E removeAtIntervalPosition(int internalIndex) {
        // TODO add bound checking
        if (!this.nodesIsEmpty[internalIndex]) {
            throw new IllegalArgumentException("Trying to remove empty element");
        }

        // check if you can guarantee that removal will not disturb order of nodes
        // (can only be guaranteed if removing last element)
        if (internalIndex != this.lastNode) {
            this.isNodesArraySorted = false;
        }

        int prevNodeIndex = this.nodesPrevIndex[internalIndex];
        int nextNodeIndex = this.nodesNextIndex[internalIndex];

        // set previous
        if (prevNodeIndex != EMPTY_NODE) {
            this.nodesNextIndex[prevNodeIndex] = nextNodeIndex;
        } else {
            // removing first node
            this.firstNode = nextNodeIndex;
        }
        // set next
        if (nextNodeIndex != EMPTY_NODE) {
            this.nodesPrevIndex[nextNodeIndex] = prevNodeIndex;
        } else {
            // removing last node
            this.lastNode = prevNodeIndex;
        }

        // remove
        E removedObject = (E) this.nodes[internalIndex];
        this.nodes[internalIndex] = null;
        this.nodesPrevIndex[internalIndex] = EMPTY_NODE;
        this.nodesNextIndex[internalIndex] = EMPTY_NODE;
        this.nodesIsEmpty[internalIndex] = true;
        this.size--;

        return removedObject;
    }

    private boolean checkIsNodesArraySorted() {
        // TODO implement
        return false;
    }

    public void sortNodesArray() {
        // TODO implement
        /*
        this will be O(n)
        just use firstNode and nodes next and
        put each element at proper place
         */
    }

    public boolean isNodesArraySorted() {
        return this.isNodesArraySorted;
    }


    public class ArrayBasedLinkedListIterator implements ListIterator<E> {
        /**
         * describes state of iterator with regard to set and remove operations
         */
        public enum State {
            CANNOT_SET_OR_REMOVE,
            AFTER_NEXT,
            AFTER_PREV
        }

        /**
         * internal position of next element
         */
        private int nextElementIndex;
        /**
         * position of the next element on the list (it's index)
         */
        private int nextListIndex;
        private State state;

        public ArrayBasedLinkedListIterator() {
            this.nextElementIndex = ArrayBasedLinkedList.this.firstNode;
            this.nextListIndex = 0;
            this.state = State.CANNOT_SET_OR_REMOVE;
        }

        public ArrayBasedLinkedListIterator(int index) {
            // TODO what about edge case with empty list?
            // FIXME inconsistent behaviour: default ctro works with empty list, while this won't
            this.nextElementIndex = findInternalPosition(index);
            this.nextListIndex = index;
            this.state = State.CANNOT_SET_OR_REMOVE;
        }

        @Override
        public boolean hasNext() {
            return this.nextListIndex < ArrayBasedLinkedList.this.size();
        }

        @Override
        public int nextIndex() {
            return this.nextListIndex;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Collection have no next element");
            }
            // get next element
            E nextElement = (E) ArrayBasedLinkedList.this.nodes[this.nextElementIndex];
            // move pointers to next element
            this.nextElementIndex = ArrayBasedLinkedList.this.nodesNextIndex[this.nextElementIndex];
            this.nextListIndex++;

            this.state = State.AFTER_NEXT;

            return nextElement;
        }

        @Override
        public boolean hasPrevious() {
            return this.nextListIndex > 0;
        }
        
        @Override
        public int previousIndex() {
            return this.nextListIndex - 1;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException("Collection have no previous element");
            }
            this.nextElementIndex = ArrayBasedLinkedList.this.nodesPrevIndex[this.nextElementIndex];
            this.nextListIndex--;

            this.state = State.AFTER_PREV;

            return (E) ArrayBasedLinkedList.this.nodes[this.nextElementIndex];
        }

        @Override
        public void add(E e) {
            if (this.nextElementIndex == EMPTY_NODE) {
                // adding to the end of the list
                ArrayBasedLinkedList.this.add(e);
            } else {
                // adding in the middle or in the beginning of the list
                ArrayBasedLinkedList.this.add(this.nextListIndex - 1, e);
            }
            this.nextListIndex++;
            this.state = State.CANNOT_SET_OR_REMOVE;
        }

        @Override
        public void remove() {
            // TODO implement
            throw new UnsupportedOperationException("not implemented yet");

            this.state = State.CANNOT_SET_OR_REMOVE;
        }

        @Override
        public void set(E e) {
            // TODO implement
            throw new UnsupportedOperationException("not implemented yet");

            this.state = State.CANNOT_SET_OR_REMOVE;
        }
    }


    private class ArrayBasedLinkedListView extends AbstractSequentialList<E> {
        // TODO finish implementing
        // look at:
        // - https://docs.oracle.com/javase/8/docs/api/java/util/AbstractList.html
        // - https://hg.openjdk.org/jdk/jdk/file/default/src/java.base/share/classes/java/util/AbstractList.java

        /**
         * internal position (in nodes array) of node before first node of sublist
         * if sublist starts with index 0, then it's equal to EMPTY_NODE
         */
        private final int nodeBeforeFirst;

        /**
         * internal position (in nodes array) of node after last node of sublist
         * if sublist enda with last element of list, then it's equal to EMPTY_NODE
         */
        private final int nodeAfterLast;


        /**
         * @param fromIndex index of the first element of sublist
         * @param toIndex   index after last element of the sublist, can be equal to list.size
         */
        public ArrayBasedLinkedListView(int fromIndex, int toIndex) {
            if (fromIndex == 0) {
                this.nodeBeforeFirst = EMPTY_NODE;
            } else {
                int firstNodeIndex = ArrayBasedLinkedList.this.findInternalPosition(fromIndex);
                this.nodeBeforeFirst = ArrayBasedLinkedList.this.nodesPrevIndex[firstNodeIndex];
            }
            if (toIndex == ArrayBasedLinkedList.this.size) {
                this.nodeAfterLast = EMPTY_NODE;
            } else {
                this.nodeAfterLast = ArrayBasedLinkedList.this.findInternalPosition(toIndex);
            }
        }
        /*
        how to make sublist of empty list
        from and to index is 0
         */

        @Override
        public ListIterator<E> listIterator(int index) {
            return null;
        }

        @Override
        public int size() {
            int firstElementIndex;
            // TODO move this logic to other method (sth like get index)
            // find index of the first node of sublist
            if (this.nodeBeforeFirst == EMPTY_NODE) {
                firstElementIndex = 0;
            } else if (ArrayBasedLinkedList.this.isNodesArraySorted) {
                firstElementIndex = this.nodeBeforeFirst + 1;
            } else {
                int currentNode = ArrayBasedLinkedList.this.firstNode;
                firstElementIndex = 1;
                // TODO use iterator for this
                while (currentNode != this.nodeBeforeFirst) {
                    currentNode = ArrayBasedLinkedList.this.nodesNextIndex[currentNode];
                    firstElementIndex++;
                }
            }

            // TODO make it more clear
            // last element in set to -1 if list is empty
            int lastElementIndex;
            // find index of the last node of sublist
            if (this.nodeAfterLast == EMPTY_NODE) {
                lastElementIndex = ArrayBasedLinkedList.this.size - 1;
            } else if (ArrayBasedLinkedList.this.isNodesArraySorted) {
                lastElementIndex = this.nodeAfterLast - 1;
            } else {
                int currentNode = ArrayBasedLinkedList.this.lastNode;
                // size - 2 is element before last
                lastElementIndex = ArrayBasedLinkedList.this.size - 2;
                while (currentNode != this.nodeAfterLast) {
                    currentNode = ArrayBasedLinkedList.this.nodesPrevIndex[currentNode];
                    lastElementIndex++;
                }
            }

            return lastElementIndex - firstElementIndex + 1;
        }

        private class ViewIterator implements ListIterator<E> {

        }
    }
}
