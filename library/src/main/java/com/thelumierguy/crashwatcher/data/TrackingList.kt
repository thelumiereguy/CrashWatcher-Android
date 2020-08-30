package com.thelumierguy.crashwatcher.data

class TrackingList<T>(private val maxSize: Int) : MutableList<T> {

    private val list = mutableListOf<T>()

    override fun add(index: Int, element: T) {
        list.add(index, element)
    }

    override fun get(index: Int): T {
        return list[index]
    }

    override fun removeAt(index: Int): T {
        return list.removeAt(index)
    }

    override fun set(index: Int, element: T): T {
        return list.set(index, element)
    }

    override fun add(element: T): Boolean {
        if (list.size >= maxSize) {
            val newIndex = list.size % size
            list.removeAt(newIndex)
            add(list.size, element)
            return true
        }
        return list.add(element)
    }

    override fun contains(element: T): Boolean {
        return list.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return list.containsAll(elements)
    }

    override fun indexOf(element: T): Int {
        return list.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun iterator(): MutableIterator<T> {
        return list.iterator()
    }

    override fun lastIndexOf(element: T): Int {
        return list.lastIndexOf(element)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return list.addAll(index, elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return list.addAll(elements)
    }

    override fun clear() {
        list.clear()
    }

    override fun listIterator(): MutableListIterator<T> {
        return list.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return list.listIterator(index)
    }

    override fun remove(element: T): Boolean {
        return list.remove(element)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return list.removeAll(elements)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return list.retainAll(elements)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return list.subList(fromIndex, toIndex)
    }

    override val size
        get() = list.size
}