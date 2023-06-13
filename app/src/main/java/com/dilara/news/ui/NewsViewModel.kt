package com.dilara.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.news.NewsApplication
import com.dilara.news.models.Article
import com.dilara.news.models.NewsResponse
import com.dilara.news.repository.NewsRepository
import com.dilara.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel( //Bu kod bloğu, AndroidViewModel sınıfından kalıtım alarak NewsViewModel sınıfını tanımlayan bir sınıf tanımıdır.
// NewsViewModel, uygulama düzeyindeki verilere erişmek için kullanılan bir ViewModel'dir ve uygulama bağımlılıklarını enjekte etmek için bir NewsRepository nesnesi alır.
// Bu sınıfın örneği, ViewModelProvider tarafından oluşturulabilir ve uygulama bileşenleri tarafından kullanılabilir.
    app:Application,
    val newsRepository: NewsRepository
) :AndroidViewModel(app){

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData() //Bu kod bloğunda, haberlerin depolanacağı MutableLiveData objeleri, sayfalama bilgileri ve dönüş objeleri tanımlanmıştır. breakingNews adında bir MutableLiveData objesi oluşturulmuş ve içerisinde bir Resource nesnesi tutulacaktır.
    var breakingNewsPage = 1
    var breakingNewsResponse:NewsResponse?=null //breakingNewsResponse  API'dan dönen yanıtların geçici olarak saklanacağı objelerdir.

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1 //breakingNewsPage ve searchNewsPage değişkenleri, hangi sayfadan başlayarak haberlerin getirileceğini belirlemek için kullanılır.
    var searchNewsResponse: NewsResponse?=null // searchNewsResponse  API'dan dönen yanıtların geçici olarak saklanacağı objelerdir.


    init { //Bu kod, NewsViewModel sınıfının örneği oluşturulduğunda çalıştırılır ve "us" ülke koduyla ilgili  haberleri alma işlemini başlatır. Yani, uygulama açıldığında önceki "us" haberleri yüklenecek ve daha sonra güncellenen son  haberler listesi gösterilecektir.
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        //Bu, NewsRepository sınıfındaki getBreakingNews işlevini çağıran viewModelScope'da bir Coroutine başlatır. Bu işlev, belirtilen countryCode'a göre son dakika haberlerini alma işlemi gerçekleştirir. Bu işlem, önce breakingNews'a yüklenirken işlem durumu da Resource.Loading() olarak ayarlanır. İşlem başarılı olursa, elde edilen cevap, handleBreakingNewsResponse işlevine gönderilir ve işlemin sonucu Resource.Success() olarak ayarlanır ve breakingNews'a gönderilir. Ayrıca, sayfa sayısı da arttırılır. İşlem başarısız olursa, hata mesajı Resource.Error() olarak ayarlanır ve breakingNews'a gönderilir.
       safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        //"searchQuery" (arama sorgusu olarak adlandırılır). Bu fonksiyon "viewModelScope.launch" kullanarak bir "searchNewsCall" işlevi çağırır ve bu işlev, verilen arama sorgusuna dayalı olarak haberleri arar. Bu fonksiyon asenkron bir şekilde çalışır, yani ana iş parçacığı üzerinde bloke etmez ve arka planda çalışır.
        searchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) :Resource <NewsResponse>{ //Bu fonksiyon, haber kaynağından alınan son dakika haberleriyle ilgili bir API çağrısının cevabını ele alır ve bir Resource tipinde yanıt döndürür.
        if (response.isSuccessful){ //response.isSuccessful: HTTP isteğinin başarılı olup olmadığını belirten bir Boolean değerdir. Eğer istek başarılıysa, let işlevi içindeki kodlar çalıştırılır.
            response.body()?.let { resultResponse -> //resultResponse: cevaptan dönen NewsResponse nesnesidir.
                breakingNewsPage++ //breakingNewsPage++: Haber sayfasının numarası arttırılır, böylece bir sonraki istek farklı bir sayfayı döndürür.
                if (breakingNewsResponse == null){ //breakingNewsResponse: uygulama kaydedilen en son haberleri içeren bir NewsResponse nesnesidir. API'den gelen veriler bu nesne ile birleştirilir.
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticles=breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse) //Resource.Success(breakingNewsResponse ?: resultResponse): Bu, Resource tipinde bir başarılı yanıt döndürür ve breakingNewsResponse nesnesi boşsa, API'den dönen sonuçResponse nesnesini döndürür.
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{ //Bu fonksiyon, arama sorgusuna göre haberleri işlemek için kullanılır. Parametre olarak bir haber yanıtı alır ve bir Resource<NewsResponse> türünde yanıt döndürür.
        if (response.isSuccessful){ //Fonksiyon bir Response nesnesi alır ve Resource tipinde bir değer döndürür.
            response.body()?.let { resultResponse -> //Başlangıçta, arama haberleri Resource nesnesi Loading durumuna atanır ve sonuç dönene kadar bu durum devam eder
                searchNewsPage++
                if (searchNewsResponse == null){ //Ardından, yanıtın başarılı olup olmadığı kontrol edilir.
                    searchNewsResponse=resultResponse // //Eğer yanıt başarılıysa, yanıtın gövdesi olan NewsResponse nesnesi işlenir ve Resource tipinde bir Success durumunda döndürülür.
                }else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            //Yanıtın gövdesi olan NewsResponse nesnesi içindeki Article listesi, arama sayfası numarası arttırılarak mevcut arama haberleri nesnesine eklenir.
            }
        }
        return Resource.Error(response.message()) //Eğer yanıt başarısızsa, Resource tipinde bir Error durumunda döndürülür ve hata mesajı yanıt mesajı olarak ayarlanır.
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        //Bu fonksiyon, bir makaleyi veritabanına kaydetmek için kullanılır. ViewModel kapsamında bir coroutine oluşturulur ve veritabanına erişmek için NewsRepository sınıfındaki upsert fonksiyonu çağrılır. upsert, veritabanında makale varsa günceller, aksi takdirde yeni bir makale ekler.
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews() //getSavedNews() fonksiyonu, newsRepository objesi üzerinden veritabanında kaydedilmiş haberleri getirir. Bu fonksiyon, NewsRepository sınıfında tanımlanmış bir fonksiyonu çağırmaktadır. Sonuç olarak, veritabanındaki tüm kaydedilmiş haberleri içeren bir liste döndürür. Bu işlem asenkron olarak gerçekleştirilir ve bir LiveData nesnesi döndürür.

    fun deleteArticle(article: Article) = viewModelScope.launch {
        //Bu kod bloğu, bir makale öğesini veritabanından silmek için kullanılır. deleteArticle fonksiyonu, bir Article nesnesi alır ve bu nesneyi veritabanından silmek için newsRepository'ye gönderir. viewModelScope kullanılarak, işlem View Model'in yaşam döngüsüne bağlanır ve işlem View Model öldüğünde sonlandırılır. Bu işlev, belirtilen makale öğesinin veritabanından kaldırılmasını sağlar.
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading()) //breakingNews canlı veri nesnesinin değeri Resource.Loading() olarak ayarlanır.
        try {
            if (hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage) // internet bağlantısı varsa, newsRepository.getBreakingNews(countryCode, breakingNewsPage) ile haberleri çağırır.
                breakingNews.postValue(handleBreakingNewsResponse(response)) //çağrının yanıtını handleBreakingNewsResponse(response) işleyicisi ile işler ve sonucu breakingNews canlı veri nesnesine postValue() yöntemiyle yerleştirir.
            }else{ //. İnternet bağlantısı yoksa, Resource.Error("No internet connection") ile bir hata mesajı oluşturulur ve breakingNews canlı veri nesnesine postValue() yöntemiyle yerleştirilir.
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){ //İşlem sırasında herhangi bir hata oluşursa, hata tipi kontrol edilir ve uygun hata mesajı breakingNews canlı veri nesnesine yerleştirilir.
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network failure."))
                else -> breakingNews.postValue(Resource.Error("Conversion error."))
            }
        }
    }


    private suspend fun searchNewsCall(searchQuery: String) {//Bu kod bloğu, haber araması yapmak için kullanılan asenkron bir işlevi tanımlar.
        //suspend : Bu işlevin askıya alınabileceğini ve coroutines kullanarak çalıştırılabileceğini belirtir.
        searchNews.postValue(Resource.Loading()) //searchNews.postValue(Resource.Loading()) : Haber arama işleminin başladığını belirtmek için, searchNews adlı MutableLiveData'ya bir Resource.Loading objesi atar.
        try {
            if (hasInternetConnection()) { //hasInternetConnection() : İnternet bağlantısının olup olmadığını kontrol eder.
                val response = newsRepository.searchNews(searchQuery, searchNewsPage) //newsRepository.searchNews(searchQuery, searchNewsPage) : Haberleri aramak için NewsRepository sınıfındaki searchNews fonksiyonunu çağırır ve sonuç olarak bir Response nesnesi döndürür.
                searchNews.postValue(handleSearchNewsResponse(response)) //searchNews.postValue(handleSearchNewsResponse(response)) : Haber arama işleminin tamamlandığını belirtmek için, searchNews adlı MutableLiveData'ya handleSearchNewsResponse(response) ile döndürülen bir Resource objesi atar.
            } else {
                searchNews.postValue(Resource.Error("No internet connection")) //Resource.Error("No internet connection") : Eğer internet bağlantısı yoksa, searchNews adlı MutableLiveData'ya bir Resource.Error objesi atar ve "No internet connection" hatası mesajını yayar.
            }
        }catch (t:Throwable){ //catch(t:Throwable) : try bloğunda herhangi bir hata olması durumunda yakalanan hata türünü kontrol eder.
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network failure.")) //is IOException : Eğer hata bir IOException ise, searchNews adlı MutableLiveData'ya bir Resource.Error objesi atar ve "Network failure." hatası mesajını yayar.
                else -> breakingNews.postValue(Resource.Error("Conversion error.")) //breakingNews.postValue(Resource.Error("Conversion error.")) : searchNews işlevinde bir hata oluşması durumunda, searchNews adlı MutableLiveData'ya bir Resource.Error objesi atar ve "Conversion error." hatası mesajını yayar.
            }
        }
    }

    private fun hasInternetConnection():Boolean{//Bu fonksiyon internet bağlantısı kontrolünü gerçekleştirmek için kullanılır.
        val connectivityManager = getApplication<NewsApplication>().getSystemService( //getApplication<NewsApplication>() metodu, uygulamanın NewsApplication sınıfını getirir.
            Context.CONNECTIVITY_SERVICE//Context.CONNECTIVITY_SERVICE sabiti, internet bağlantısı ile ilgili bilgileri tutan bir servis referansını temsil eder.
        )as ConnectivityManager //as ConnectivityManager ifadesi, referansın ConnectivityManager sınıfına cast edilmesini sağlar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ifadesi, cihazın minimum API sürümü 23 ise işlem yapılacağını belirtir.
            val activeNetwork=connectivityManager.activeNetwork ?:return false //connectivityManager.activeNetwork özelliği, cihazın mevcut aktif ağını verir. Ağa bağlı değilse null döner.
            val capabilities= //connectivityManager.getNetworkCapabilities(activeNetwork) metodu, belirtilen ağın özelliklerini alır ve bir NetworkCapabilities objesi döndürür. Bu objenin özellikleri, ağ tipi, hızı, güvenliği vb. bilgileri içerir.
                connectivityManager.getNetworkCapabilities(activeNetwork) ?:return false
            return when{//capabilities.hasTransport(TRANSPORT_WIFI) ifadesi, capabilities objesindeki özellikler arasında TRANSPORT_WIFI (WIFI ağı) özelliği varsa true döndürür.
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) ->true //capabilities.hasTransport(TRANSPORT_CELLULAR) ifadesi, capabilities objesindeki özellikler arasında TRANSPORT_CELLULAR (mobil ağ) özelliği varsa true döndürür.
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true //capabilities.hasTransport(TRANSPORT_ETHERNET) ifadesi, capabilities objesindeki özellikler arasında TRANSPORT_ETHERNET (Ethernet) özelliği varsa true döndürür.
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                //connectivityManager.activeNetworkInfo özelliği, cihazın mevcut ağı ile ilgili bilgi sağlayan bir NetworkInfo objesi döndürür. Bu objenin özellikleri, ağ tipi, hızı, güvenliği vb. bilgileri içerir.
                return when(type){ //return when(type) ifadesi, activeNetworkInfo objesinin type özelliğine göre işlem yapar. TYPE_WIFI (WIFI ağı) ise true döndürür,
                    TYPE_WIFI-> true
                    ConnectivityManager.TYPE_MOBILE->true //ConnectivityManager.TYPE_MOBILE (mobil ağ) ise true döndürür,
                    TYPE_ETHERNET -> true //TYPE_ETHERNET (Ethernet) ise true döndürür, diğer durumlarda false döndürür.
                    else ->false
                }
            }
        }
        return false
    }
}