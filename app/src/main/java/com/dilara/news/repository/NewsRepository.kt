package com.dilara.news.repository

import com.dilara.news.api.RetrofitInstance
import com.dilara.news.db.ArticleDatabase
import com.dilara.news.models.Article


class NewsRepository(//Bu kod parçası, haberlerin veri tabanı işlemlerini gerçekleştiren NewsRepository sınıfını tanımlar.
    val db: ArticleDatabase
) {//NewsRepository sınıfı, ArticleDatabase veritabanı bağımlılığı ile birlikte tanımlanır.
// ArticleDatabase, uygulamanın yerel veri tabanına erişim sağlar.
    suspend fun getBreakingNews(countryCode: String, pageNumber:Int)=  //getBreakingNews fonksiyonu, Retrofit kütüphanesi kullanarak haberlerin alınması için bir GET isteği yapar. countryCode ve pageNumber parametreleri, belirli bir ülkeye ve sayfaya göre haberleri getirir.
        // Bu işlem suspend anahtar kelimesiyle işaretlenmiştir, çünkü bu işlemin asenkron olarak gerçekleşmesi beklenir.
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) = //searchNews fonksiyonu, Retrofit kütüphanesi kullanarak arama sorgusuna göre haberleri getirir.
        // searchQuery ve pageNumber parametreleri, aranacak kelimeyi ve sayfayı belirler. Bu işlem de suspend anahtar kelimesiyle işaretlenmiştir.
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article) //upsert fonksiyonu, bir Article öğesi verildiğinde, bu öğenin veritabanına ekleneceğini veya güncelleneceğini belirler. Bu işlem de suspend anahtar kelimesiyle işaretlenmiştir.

    fun getSavedNews() = db.getArticleDao().getAllArticles() //getSavedNews fonksiyonu, veritabanında kaydedilmiş tüm haberleri getirir.

    suspend fun deleteArticle(article: Article) = db.getArticleDao().delete(article) //deleteArticle fonksiyonu, veritabanından bir haber öğesi siler. Bu işlem de suspend anahtar kelimesiyle işaretlenmiştir.
}//Bu şekilde, NewsRepository sınıfı, Retrofit kütüphanesi ve yerel veritabanı işlemleri aracılığıyla haberlerin getirilmesi ve veri tabanı işlemlerinin yapılması için kullanılır.