package com.dilara.news.api

import com.dilara.news.models.NewsResponse
import com.dilara.news.util.Constants.API_KEY

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI { //Bu kod parçası, NewsAPI adlı bir arayüz tanımlar. Bu arayüz, Retrofit kütüphanesi aracılığıyla haberlerin alınması için kullanılır.

    @GET("v2/top-headlines")//@GET anotasyonu, HTTP GET isteğinin kullanılacağını belirtir.
    //"v2/top-headlines" URL'si, bu API isteğinin hedefini belirtir.
    suspend fun getBreakingNews( //getBreakingNews fonksiyonu, @GET anotasyonuyla belirtilen v2/top-headlines URL'sine bir GET isteği yapar.
        // Bu istek, belirtilen countryCode ve pageNumber parametreleriyle yapılmaktadır. Ayrıca, apiKey parametresi, API anahtarını belirler. Bu işlem, suspend anahtar kelimesiyle işaretlenmiştir, çünkü bu işlemin asenkron olarak gerçekleşmesi beklenir.
        // Bu işlem, NewsResponse türünde bir yanıt döndürür.
        //country" parametresi, API'den istenen haberlerin ülkesini belirtir.
        @Query("country") //@Query anotasyonları, her bir parametrenin URL'de nasıl gönderileceğini belirtir.
        countryCode:String="us",
        @Query("page")//"page" parametresi, API'den istenen sayfanın numarasını belirtir.
        pageNumber:Int=1,
        @Query("apiKey")//apiKey" parametresi, bu API'yi kullanmak için gerekli olan API anahtarını belirtir. Varsayılan olarak API_KEY değişkenine atanmış bir sabit kullanılır.
        apiKey:String=API_KEY
    ):Response<NewsResponse> //NewsResponse veri sınıfını belirtir. Bu sınıf, API'nin yanıt verdiği JSON verilerinin ayrıştırılması için kullanılır. Response sınıfı, Retrofit kütüphanesinde tanımlı bir sınıftır ve hem yanıtın başarılı olduğunu hem de hataların nasıl ele alınacağını belirtir.

    @GET("v2/everything")
    suspend fun searchForNews(//searchForNews fonksiyonu, @GET anotasyonuyla belirtilen v2/everything URL'sine bir GET isteği yapar. Bu istek, belirtilen searchQuery ve pageNumber parametreleriyle yapılır.
        // Ayrıca, apiKey parametresi, API anahtarını belirler. Bu işlem de suspend anahtar kelimesiyle işaretlenmiştir, çünkü bu işlemin asenkron olarak gerçekleşmesi beklenir. Bu işlem, NewsResponse türünde bir yanıt döndürür.
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsResponse>
}//Bu şekilde, NewsAPI arayüzü, Retrofit kütüphanesi aracılığıyla belirtilen URL'lerin GET istekleri yapılmasını sağlar.