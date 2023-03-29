package com.example.funnypuny.domain.entity

class Optional<out T>(val value: T? = null) {

    companion object {
        fun <T> of(value: T?): Optional<T> = Optional(value)
        fun <T> empty(): Optional<T> = Optional()
    }

    fun hasValue() = value != null
}