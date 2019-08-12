package com.yhl.kotlin

open class Book(val title: String, val author: String) {

    private var currentPage = 1

    open fun readPage() {
        currentPage ++
    }

}

class EBook(title: String, author: String, var format: String = "text"): Book(title, author) {

    private var wordsRead = 0

    override fun readPage() {
        wordsRead += 250
    }
}