package com.example.bmi.logic

import java.util.*

class HistoryQueue {

    private val maxSize = 10
    private val entriesQueue: LinkedList<HistoryEntry> = LinkedList()

    fun add(entry: HistoryEntry) {
        if (entriesQueue.size >= maxSize)
            entriesQueue.removeLast()
        entriesQueue.addFirst(entry)
    }

    fun toArrayList(): ArrayList<HistoryEntry> {
        return ArrayList(entriesQueue)
    }
}