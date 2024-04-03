package com.satyajit.threads.utils

import com.google.gson.Gson

fun <A> String.fromJson(type: Class<A>): A {
    return Gson().fromJson(this, type)
}
fun <A> A.toJson(): String? {
    return Gson().toJson(this)
}