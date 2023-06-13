package com.dilara.news.util

sealed class Resource <T>(//Bu, özel bir kotlin sınıfıdır. Resource sınıfı, bir API veya bir veritabanından veri getirme işleminin sonucunu belirtmek için kullanılır
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T) : Resource<T>(data) //Success<T>: Veri başarıyla getirilirse, bu alt sınıf kullanılır ve veriyi içerir.
    class Error<T>(message: String,data: T?= null) : Resource<T>(data,message) //Error<T>: Veri getirilirken hata oluşursa, bu alt sınıf kullanılır ve hata mesajını ve varsa veriyi içerir.
    class Loading<T> : Resource<T>() //Loading<T>: Veri getirme işlemi sürerken, bu alt sınıf kullanılır ve veri veya hata mesajı içermez.
}