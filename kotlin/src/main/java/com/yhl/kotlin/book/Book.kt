package com.yhl.kotlin.book

import java.util.*

const val MAX_BOOK_NUM = 10

class Book(val title: String, val author: String, val year: Int, var pages: Int = 300) {
    fun getTitleAuthor(): Pair<String, String> {
        return (title to author)
    }

    fun getTitleAuthorYear(): Triple<String, String, Int> {
        return Triple(title, author, year)
    }

    fun canBorrow(num: Int): Boolean {
        return num < MAX_BOOK_NUM
    }

    fun printUrl(): String{
        return "$BASE_URL$title.html"
    }

    companion object {
        const val BASE_URL = "www.homlee.com/"
    }
}

fun Book.weight(): Double = pages * 1.5

fun Book.tornPages(torn: Int) {
    if (pages > torn) {
        pages -= torn
    } else {
        pages = 0
    }
}

class Puppy() {
    fun playWithBook(book: Book) {
        book.tornPages(Random().nextInt(12))
    }
}

object Constants {
    const val BASE_URL = "www.homlee.com/"
}

fun main(args: Array<String>) {
    /*
    val book = Book("Romeon and Juliet", "william Shakespeare", 1579)
    val bookTitleAuthor = book.getTitleAuthor();
    val bookTitleAuthorYear = book.getTitleAuthorYear();
    println("Here is your book ${bookTitleAuthor.first} by ${bookTitleAuthor.second}")
    println("Here is your book ${bookTitleAuthorYear.first} by ${bookTitleAuthorYear.second} written in ${bookTitleAuthorYear.third}")
    */

    /*var allBooks = setOf("Macbeth", "Romeo and Juliet", "Hamlet", "A Midsummer Night's Dream")
    var library = mapOf("Shakespeare" to allBooks)
    println(library.any {
        it.value.contains("Hamlet")
    })

    val moreBooks = mutableMapOf<String, String>("Wilhelm Tell" to "Schiller")
    moreBooks.getOrPut("Jungle Book") { "Kipling" }
    moreBooks.getOrPut("Hamlet") { "Shakespeare" }
    println(moreBooks)*/

    val puppy = Puppy()
    val book = Book("Oliver Twist", "Charles Dickens", 1837, 540)

    while (book.pages > 0) {
        puppy.playWithBook(book)
        println("${book.pages} left in ${book.title}")
    }
    println("Sad puppy, no more pages in ${book.title}. ")
}