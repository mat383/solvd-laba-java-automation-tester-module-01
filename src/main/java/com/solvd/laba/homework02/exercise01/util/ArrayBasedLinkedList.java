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
     * @param element element whose presence in this collection is to be ensured
     * @return
     */
    @Override
    public boolean add(E element) {
        // check if internal array needs expanding
        if (this.nodes.length < this.size + 1) {
            expand(1);
        }

        int newIndex = findEmptyNode();
        this.nodes[newIndex] = element;
        this.nodesIsEmpty[newIndex] = false;
        if (this.lastNode != EMPTY_NODE) {
            this.nodesNextIndex[this.lastNode] = newIndex;
        }
        this.nodesPrevIndex[newIndex] = this.lastNode;
        this.nodesNextIndex[newIndex] = EMPTY_NODE;
        this.lastNode = newIndex;
        this.firstNode = this.firstNode == EMPTY_NODE ? newIndex : this.firstNode;
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

        this.nodes[newIndex] = element;
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
        return new ArrayBasedLinkedListView(fromIndex, toIndex);
    }


    // helper methods

    /**
     * Finds empty space in internal array for new node,
     * throws NoSuchElementException if there isn't any.
     * At first, it tries to find empty space after last element,
     * and if this fails then it will search from the beginning.
     *
     * @return index of empty node in internal array
     */
    private int findEmptyNode() {
        if (this.nodes.length < this.size + 1) {
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
     * converts internal position into index on the list
     *
     * @param internalPosition position of node in nodes array
     * @return list index of node with given internal position
     */
    private int findListPosition(int internalPosition) {
        if (this.nodesIsEmpty[internalPosition]) {
            throw new NoSuchElementException("No element with at internal position " + internalPosition);
        }
        if (this.isNodesArraySorted) {
            return internalPosition;
        } else {
            int currentNode = this.firstNode;
            int index = 0;
            while (currentNode != internalPosition && currentNode != EMPTY_NODE) {
                currentNode = this.nodesNextIndex[currentNode];
                index++;
            }
            assert currentNode != EMPTY_NODE;
            return index;
        }
    }

    /**
     * remove element identified by its index in nodes array
     *
     * @param internalIndex index of element in nodes array (different from index of this list)
     * @return reference to removed element
     */
    private E removeAtIntervalPosition(int internalIndex) {
        // TODO add bound checking
        if (this.nodesIsEmpty[internalIndex]) {
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
        int nodeListPosition = 0;
        int currentNode = this.firstNode;
        while (currentNode != EMPTY_NODE) {
            if (nodeListPosition != currentNode) {
                return false;
            }
            nodeListPosition++;
            currentNode = this.nodesNextIndex[currentNode];
        }
        return true;
    }

    /**
     * Sort internal nodes array so that internal position equals position in list,
     * which makes cost of accessing elements similar to ArrayList.
     * This will probably make structural modifications to the list, so using
     * this method while some iterator or sublist is used will lead to undefined behaviour.
     * The time cost of this operation is linear with respect to the size of the list.
     */
    public void sortNodesArray() {
        // TODO implement
        if (this.isNodesArraySorted) {
            return;
        }

        int currentNodeIndex = this.firstNode;
        // for each node check if it is on right position
        // if not, swap it to the right position
        for (int i = 0; i < this.size; i++) {
            if (currentNodeIndex != i) {
                // swap nodes at i and currentNodeIndex
                // update neighboring nodes
                if (this.nodesPrevIndex[currentNodeIndex] != EMPTY_NODE)
                    this.nodesNextIndex[this.nodesPrevIndex[currentNodeIndex]] = i;
                if (this.nodesNextIndex[currentNodeIndex] != EMPTY_NODE)
                    this.nodesPrevIndex[this.nodesNextIndex[currentNodeIndex]] = i;
                if (this.nodesPrevIndex[i] != EMPTY_NODE)
                    this.nodesNextIndex[this.nodesPrevIndex[i]] = currentNodeIndex;
                if (this.nodesNextIndex[i] != EMPTY_NODE)
                    this.nodesPrevIndex[this.nodesNextIndex[i]] = currentNodeIndex;

                // swap elements
                E swapHelperObj = (E) this.nodes[i];
                this.nodes[i] = this.nodes[currentNodeIndex];
                this.nodes[currentNodeIndex] = swapHelperObj;

                int swapHelperInt = this.nodesNextIndex[i];
                this.nodesNextIndex[i] = this.nodesNextIndex[currentNodeIndex];
                this.nodesNextIndex[currentNodeIndex] = swapHelperInt;

                swapHelperInt = this.nodesPrevIndex[i];
                this.nodesPrevIndex[i] = this.nodesPrevIndex[currentNodeIndex];
                this.nodesPrevIndex[currentNodeIndex] = swapHelperInt;

                boolean swapHelperBool = this.nodesIsEmpty[i];
                this.nodesIsEmpty[i] = this.nodesIsEmpty[currentNodeIndex];
                this.nodesIsEmpty[currentNodeIndex] = swapHelperBool;

                // updateCurrentNodeIndex
                currentNodeIndex = i;
            }
            currentNodeIndex = this.nodesNextIndex[currentNodeIndex];
        }

        // set first last node
        if (this.size > 0) {
            this.firstNode = 0;
            this.lastNode = this.size - 1;
        }
        this.isNodesArraySorted = true;
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

        // TODO rename them to element after cursor pointer or sth
        /**
         * internal position of next element, can be EMPTY_NODE if list is empty
         */
        private int nextElementIndex;
        /**
         * position of the next element on the list (it's index)
         */
        // TODO change to be sth about relative position, or split to position and offset
        private int nextListIndex;
        private State state;

        /**
         * index of the element before the first, used to narrow scope of iteration
         * set to EMPTY_NODE to start iteration from the first element
         */
        private int elementBeforeFirstIndex;
        /**
         * index of the element after the last, used to narrow scope of iteration
         * set to EMPTY_NODE to iterate to the end
         */
        private int elementAfterLastIndex;

        public ArrayBasedLinkedListIterator() {
            this(0, EMPTY_NODE, EMPTY_NODE);
        }

        public ArrayBasedLinkedListIterator(int index) {
            this(index, EMPTY_NODE, EMPTY_NODE);
        }

        /**
         * creates iterator that is restricted to nodes
         * between nodeBeforeFirst and nodeAfterLast (exclusive)
         *
         * @param relativeIndex   index of first element that would be returned by next
         *                        relative to nodeBeforeFirst, i.e. if relativeIndex
         *                        is 0 then first element returned by next will be
         *                        the element after nodeBeforeFirst
         * @param nodeBeforeFirst internal position of element before first
         *                        accessible element (or EMPTY_ELEMENT if no restrictions)
         * @param nodeAfterLast   internal position of element after last
         *                        accessible element (or EMPTY_ELEMENT if no restrictions)
         */
        private ArrayBasedLinkedListIterator(int relativeIndex, int nodeBeforeFirst, int nodeAfterLast) {
            // TODO rewrite with optional?
            int nodeBeforeFirstListPosition = nodeBeforeFirst == EMPTY_NODE
                    ? EMPTY_NODE
                    : ArrayBasedLinkedList.this.findListPosition(nodeBeforeFirst);

            int nodeAfterLastListPosition = nodeAfterLast == EMPTY_NODE
                    ? EMPTY_NODE
                    : ArrayBasedLinkedList.this.findListPosition(nodeAfterLast);

            int absoluteIndex = nodeBeforeFirst == EMPTY_NODE
                    ? relativeIndex
                    : nodeBeforeFirstListPosition + 1 + relativeIndex;

            // in case list is empty get first element from beginning pointer
            // to avoid out of bound exception
            this.nextElementIndex = absoluteIndex == 0
                    ? ArrayBasedLinkedList.this.firstNode
                    : findInternalPosition(absoluteIndex);
            this.nextListIndex = relativeIndex;
            this.state = State.CANNOT_SET_OR_REMOVE;
            this.elementBeforeFirstIndex = nodeBeforeFirst;
            this.elementAfterLastIndex = nodeAfterLast;

            // check if index is within scope
            if (relativeIndex < 0) {
                throw new IndexOutOfBoundsException("relativeIndex cannot be negative. relativeIndex == " + relativeIndex);
            }
            if (nodeAfterLastListPosition != EMPTY_NODE
                    && nodeAfterLastListPosition <= relativeIndex) {
                throw new IndexOutOfBoundsException("relativeIndex out of scope. relativeIndex == " + relativeIndex);
            }

            // check if scope is correct
            if (nodeBeforeFirstListPosition != EMPTY_NODE
                    && nodeBeforeFirstListPosition != EMPTY_NODE
                    && nodeAfterLastListPosition <= nodeBeforeFirstListPosition) {
                throw new IllegalArgumentException("Wrong scope, beginning is after end.");
            }
        }

        @Override
        public boolean hasNext() {
            return this.nextElementIndex != this.elementAfterLastIndex
                    && this.nextElementIndex != EMPTY_NODE;
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
                // FIXME quick and dirty implementation, improve
                // potential solution would be to store offset
                if (this.elementBeforeFirstIndex == EMPTY_NODE) {
                    ArrayBasedLinkedList.this.add(this.nextListIndex, e);
                } else {
                    int offset = ArrayBasedLinkedList.this.findListPosition(this.elementBeforeFirstIndex) + 1;
                    ArrayBasedLinkedList.this.add(this.nextListIndex + offset, e);
                }
            }
            this.nextListIndex++;
            this.state = State.CANNOT_SET_OR_REMOVE;
        }

        @Override
        public void remove() {
            switch (this.state) {
                case CANNOT_SET_OR_REMOVE -> {
                    throw new IllegalStateException("Wrong state, cannot remove element");
                }
                case AFTER_NEXT -> {
                    // remove element before cursor
                    int nodeToRemove = EMPTY_NODE;
                    // check if iteration reached end of the list
                    if (this.nextElementIndex == EMPTY_NODE) {
                        nodeToRemove = ArrayBasedLinkedList.this.lastNode;
                    } else {
                        nodeToRemove = ArrayBasedLinkedList.this.nodesPrevIndex[this.nextElementIndex];
                    }
                    ArrayBasedLinkedList.this.removeAtIntervalPosition(nodeToRemove);
                    this.nextListIndex--;
                }
                case AFTER_PREV -> {
                    // remove element after cursor
                    int nodeToRemove = this.nextElementIndex;
                    this.nextElementIndex = ArrayBasedLinkedList.this.nodesNextIndex[this.nextElementIndex];
                    ArrayBasedLinkedList.this.removeAtIntervalPosition(nodeToRemove);
                }
            }
            this.state = State.CANNOT_SET_OR_REMOVE;
        }

        @Override
        public void set(E e) {
            int nodeToSet = EMPTY_NODE;
            switch (this.state) {
                case CANNOT_SET_OR_REMOVE -> {
                    throw new IllegalStateException("Wrong state, cannot set element");
                }
                case AFTER_NEXT -> {
                    // check if iteration reached end of the list
                    if (this.nextElementIndex == EMPTY_NODE) {
                        nodeToSet = ArrayBasedLinkedList.this.lastNode;
                    } else {
                        nodeToSet = ArrayBasedLinkedList.this.nodesPrevIndex[this.nextElementIndex];
                    }
                }
                case AFTER_PREV -> {
                    nodeToSet = this.nextElementIndex;
                }
            }
            ArrayBasedLinkedList.this.nodes[nodeToSet] = e;
        }
    }


    private class ArrayBasedLinkedListView extends AbstractSequentialList<E> {
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
            return ArrayBasedLinkedList.this.new ArrayBasedLinkedListIterator(index,
                    this.nodeBeforeFirst,
                    this.nodeAfterLast);
        }

        @Override
        public int size() {
            // find index of the first node of sublist
            int firstElementIndex = this.nodeBeforeFirst == EMPTY_NODE
                    ? 0
                    : ArrayBasedLinkedList.this.findListPosition(this.nodeBeforeFirst) + 1;

            // find index of the last node of sublist
            // last element in set to -1 if list is empty
            int lastElementIndex = this.nodeAfterLast == EMPTY_NODE
                    ? ArrayBasedLinkedList.this.size - 1
                    : ArrayBasedLinkedList.this.findListPosition(this.nodeAfterLast) - 1;

            return lastElementIndex - firstElementIndex + 1;
        }
    }
}
