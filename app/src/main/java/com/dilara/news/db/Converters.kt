package com.dilara.news.db

import androidx.room.TypeConverter
import com.dilara.news.models.Source


class Converters {
    //Bu sınıf, Room tarafından kullanılabilen özel tür dönüştürücüler içerir.
    // Room, verileri SQLite veritabanında depolar, ancak bunları model sınıfları ile temsil eder. Bazen, Room'un varsayılan tür dönüştürücüleri, bir veritabanı tablosuna yazılan veri türü ile ilgili bir model sınıfı alanını dönüştüremez. Bu, Source sınıfının kullanımıyla ilgilidir. Bu sınıf, Source sınıfını, veritabanına yazmak için String'e dönüştürmek ve veritabanından geri dönüştürmek için kullanılır.
    // Bu işlemler TypeConverter adlı Room işaretleyicisi kullanılarak gerçekleştirilir.
    // Bu dönüştürücüler, veritabanına yazılan ve okunan verilerin uygun şekilde dönüştürülmesini sağlar.

    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }//Bu, Room kütüphanesi tarafından kullanılan bir tür dönüştürücü işlevdir. Veritabanına kaydetmek için bir Source nesnesini bir String'e dönüştürür. Veritabanından veri çekerken bu işlem tersine çevrilir (yani String, Source nesnesine dönüştürülür).

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name, name)
    }
}