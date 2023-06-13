package com.dilara.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dilara.news.models.Article

@Dao //@Dao: Bu işaretçi, veritabanı işlemlerinin yapıldığı veri erişim nesnesi (DAO) arabirimini tanımlayan sınıfı belirtir.
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //@Insert(onConflict = OnConflictStrategy.REPLACE): Bu, veritabanına veri ekleme işlemini gerçekleştiren bir fonksiyon tanımlar. onConflict = OnConflictStrategy.REPLACE belirteci, veritabanına ekleme işlemi sırasında bir çakışma olması durumunda, yeni verilerle mevcut verilerin yerini değiştirir.
    suspend fun upsert(article: Article):Long //suspend fun upsert(article: Article):Long: Bu, asenkron olarak çalışan ve bir parametre alan bir işlevdir. Parametre, eklenecek veya güncellenecek makale nesnesidir. Fonksiyon, işlem sonucunda etkilenen satır sayısını döndürür.

    @Query("SELECT * FROM articles") //@Query("SELECT * FROM articles"): Bu, veritabanında bulunan tüm makaleleri sorgulamak için kullanılır. SELECT * FROM articles sorgusu, articles adlı tablodaki tüm sütunları seçer
    fun getAllArticles() : LiveData<List<Article>> //fun getAllArticles() : LiveData<List<Article>>: Bu, tüm makaleleri almak için bir işlevdir. Bu işlev, tüm makaleleri canlı veri olarak döndürür.

    @Delete //@Delete: Bu, veritabanından veri silme işlemini gerçekleştiren bir işaretçidir.
    suspend fun delete(article: Article) //suspend fun delete(article: Article): Bu, silinecek makale nesnesini parametre olarak alan ve işlem sonucunda etkilenen satır sayısını döndüren asenkron bir işlevdir.
}