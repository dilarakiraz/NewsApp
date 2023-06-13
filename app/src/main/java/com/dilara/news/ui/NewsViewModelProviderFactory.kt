package com.dilara.news.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dilara.news.repository.NewsRepository

class NewsViewModelProviderFactory ( //Bu satır, NewsViewModelProviderFactory sınıfını tanımlar. Bu sınıf, ViewModelProvider.Factory arayüzünü uygulayacaktır. Ayrıca, bu sınıfın iki özellik içereceği belirtilir: bir Application örneği ve bir NewsRepository örneği.
    val app: Application,
    val newsRepository: NewsRepository
    ):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T { //Bu satır, ViewModelProvider.Factory arayüzünün create metodunu uygular. Bu metod, bir ViewModel sınıfı oluşturmak için kullanılır. Burada, T türü bir ViewModel sınıfını belirtir. T'nin bir ViewModel olduğundan emin olmak için, modelClass değişkenini kullanarak kontrol edilir.
        //Daha sonra, NewsViewModel sınıfı oluşturulur ve app ve newsRepository özellikleri kullanılır. Son olarak, oluşturulan NewsViewModel örneği T türüne dönüştürülür ve geri döndürülür.
        return NewsViewModel(app,newsRepository) as T
        //Sonuç olarak, bu kod parçası, NewsViewModel sınıfının doğru şekilde oluşturulmasını sağlamak için bir ViewModelProviderFactory sınıfı oluşturur. Bu, ViewModel örneğinin uygulamanın yaşam döngüsüne uygun şekilde yönetilmesini sağlar.
    }
}

//Bu kod parçası, Android uygulamalarında ViewModel sınıfının kullanımını kolaylaştırmak için bir ViewModelProviderFactory sınıfı oluşturur. Bu sınıf, ViewModelProvider tarafından kullanılarak, ViewModel sınıflarının doğru şekilde oluşturulmasını ve enjekte edilmesini sağlar.
//
//Bu özel sınıf, Application sınıfı ve NewsRepository sınıfı bağımlılıklarını alır ve ViewModel oluşturma işlemlerini gerçekleştirir. create() yöntemi, oluşturulacak ViewModel sınıfının türüne bağlı olarak NewsViewModel sınıfını oluşturur ve geri döndürür.
// T tipi, burada ViewModel türüdür ve NewsViewModel sınıfına dönüştürülebilir olduğundan, bir instance oluşturulur ve geri döndürülür.