package com.dilara.news.models

data class NewsResponse( //Bu sınıf, haberlerin alınması sırasında API'den dönen yanıtın bir temsilidir.
    val status:String, //status: API'den dönen yanıtın durumunu belirtir, örneğin "success" veya "error".
    val totalResults:Int, //totalResults: API'den dönen toplam sonuç sayısıdır.
    val articles:MutableList<Article> //articles: Alınan haberlerin listesi, burada her bir haber, özellikleri belirtilen Article sınıfından bir nesnedir.

)