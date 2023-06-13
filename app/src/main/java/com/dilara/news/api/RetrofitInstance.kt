package com.dilara.news.api


import com.dilara.news.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance { //nesnesi sağlar. RetrofitInstance adlı sınıf, companion object içinde tanımlanmıştır.
    companion object {//companion object, Kotlin'de, sınıfın kendisine özgü bir singleton nesnesi oluşturur. Bu nesne, sınıfın metotlarına erişim sağlar.
        //RetrofitInstance sınıfı, Retrofit kütüphanesi ile iletişim kurmak için bir OkHttpClient nesnesi ve bir Retrofit nesnesi oluşturur.
    // Bu sınıf, Singleton tasarım desenine uygun olarak tasarlanmıştır. Bu, uygulamanın herhangi bir yerinde bu nesneyi oluşturmak yerine, önceden oluşturulmuş bir nesneyi kullanarak verimli bir şekilde çalışmasına yardımcı olur.
        private val retrofit by lazy {//private val retrofit adlı değişken, by lazy özelliğiyle oluşturulur. Bu, nesnenin yalnızca kullanıldığında oluşturulacağı anlamına gelir. by lazy özelliği, değişkenin yalnızca ilk çağrıldığında oluşturulmasını sağlar.
            val logging = HttpLoggingInterceptor() //HttpLoggingInterceptor ve OkHttpClient.Builder sınıfları, HTTP isteklerinin ve yanıtlarının protokol düzeyinde nasıl işlendiğini kontrol etmek için kullanılır
            logging.setLevel(HttpLoggingInterceptor.Level.BODY) //HttpLoggingInterceptor, HTTP isteklerinin ve yanıtlarının günlüğüne kaydedilmesini sağlar. Burada, BODY seviyesi belirtilerek, hem isteklerin hem de yanıtların gövdesi yazdırılır.
            val client = OkHttpClient.Builder()//OkHttpClient.Builder sınıfı, Retrofit kütüphanesinin konfigürasyonunu yapmak için kullanılır. Burada, logging adlı HttpLoggingInterceptor nesnesi eklenerek, HTTP istekleri ve yanıtları günlüğe kaydedilir.
                .addInterceptor(logging)
                .build()
            Retrofit.Builder() //Retrofit.Builder() metodu, Retrofit isteklerinin oluşturulması için gereken temel yapıyı sağlar. Burada, BASE_URL adlı bir sabit, API'ye yapılan isteklerin temel URL'sini belirtir. GsonConverterFactory sınıfı, Retrofit'in JSON verilerini otomatik olarak ayrıştırması için kullanılır. client değişkeni, OkHttpClient.Builder() ile oluşturulan istemciyi Retrofit'e ekler.
                .baseUrl(BASE_URL) //baseUrl() yöntemi kullanılarak API'nin temel URL'si ayarlanır. BASE_URL, API'nin temel URL'sini içeren bir sabittir.
                .addConverterFactory(GsonConverterFactory.create())//erFactory sınıfından bir örnek oluşturulur ve Retrofit.Builder örneğine eklenir. Bu özellik, API'dan alınan JSON verilerini otomatik olarak Kotlin sınıflarına dönüştürmek için kullanılır.
                .client(client)//client() yöntemi kullanılarak, önceden oluşturulmuş olan OkHttpClient örneği, Retrofit.Builder örneğine eklenir. Bu, isteklerin ve yanıtların loglanmasını sağlayan OkHttpClient örneğini kullanarak HTTP isteklerinin gönderilmesini ve yanıtların alınmasını sağlar.
                .build()   //build() yöntemi kullanılarak Retrofit örneği oluşturulur ve bu örnek döndürülür. Artık oluşturulan Retrofit örneği, API isteklerini yapmak için kullanılabilir.
        }

        val api by lazy { //Bu kod bloğu, RetrofitInstance sınıfından bir örnek oluşturur ve bu örneğin NewsAPI arayüzüne dayalı bir API servisi oluşturmasını sağlar.

            //api adında bir değişken tanımlanır ve lazy anahtar kelimesi kullanılarak ona tembel yüklemesi özelliği eklenir. lazy anahtar kelimesi sayesinde, api değişkeni ilk kullanıldığında yalnızca bir kez RetrofitInstance sınıfından bir örnek oluşturulur.
            retrofit.create(NewsAPI::class.java) //retrofit.create() yöntemi kullanılarak, RetrofitInstance sınıfından oluşturulan Retrofit örneği üzerinde NewsAPI arayüzüne dayalı bir API servisi oluşturulur. Bu, Retrofit'in API isteklerini oluşturmak için kullanacağı bir arayüz sağlar.
        } //Sonuç olarak, api değişkeni, RetrofitInstance sınıfından oluşturulan örnek üzerinde NewsAPI arayüzüne dayalı bir API servisi oluşturmak için kullanılabilir. Bu sayede, NewsAPI arayüzündeki API istekleri yapılabilir.
    }
}