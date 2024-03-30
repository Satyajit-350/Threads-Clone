package com.satyajit.threads.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

object SharedPref {

    fun storeData(
        userId: String,
        name:String,
        email:String,
        bio: String,
        userName: String,
        imageUrl: String?,
        context: Context
    ){

        val sharedPref = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("userId", userId)
        editor.putString("name",name)
        editor.putString("email",email)
        editor.putString("userName", userName)
        editor.putString("bio", bio)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getUserId(context: Context): String{
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("userId", "")!!
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("userName", "")!!
    }

    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!
    }

    fun getBio(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("bio", "")!!
    }

    fun setBio(context: Context, bio:String){
        val sharedPref = context.getSharedPreferences("users", MODE_PRIVATE)
        sharedPref.edit().putString("bio", bio).apply()
    }


    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("email", "")!!
    }

    fun getImageUrl(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl", "")!!
    }
}