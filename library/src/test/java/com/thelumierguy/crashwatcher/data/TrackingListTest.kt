package com.thelumierguy.crashwatcher.data

import org.junit.Test


internal class TrackingListTest {

    private val trackingList = TrackingList<Int>(5)

    @Test
    fun `for a single item, it should be added to the list item normally`() {
        trackingList.add(1)
        assert(trackingList[0] == 1)
    }

    @Test
    fun `when given n items equal to list size, they should be added to the list item normally`() {
        trackingList.add(1)
        trackingList.add(2)
        trackingList.add(3)
        trackingList.add(4)
        trackingList.add(5)
        trackingList.indices.forEach {
            assert(trackingList[it] == it + 1)
        }
    }

    @Test
    fun `when item is added at an index greater than size, difference in positions should be removed from top`() {
        trackingList.add(1)
        trackingList.add(2)
        trackingList.add(3)
        trackingList.add(4)
        trackingList.add(5)
        trackingList.add(6)
        assert(trackingList[0] == 2)
        assert(trackingList[1] == 3)
        assert(trackingList[2] == 4)
        assert(trackingList[3] == 5)
        assert(trackingList[4] == 6)
    }
}