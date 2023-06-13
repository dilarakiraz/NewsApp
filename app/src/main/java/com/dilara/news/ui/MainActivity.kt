package com.dilara.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dilara.news.R
import com.dilara.news.databinding.ActivityMainBinding
import com.dilara.news.db.ArticleDatabase
import com.dilara.news.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding //binding özelliği, ActivityMainBinding adlı veri bağlama sınıfından bir örneği tutar. Veri bağlama, XML dosyalarındaki kullanıcı arayüzü bileşenlerini, sınıf dosyasındaki sınıf özellikleriyle eşleştirerek erişimi kolaylaştıran bir tekniktir.
    lateinit var viewModel: NewsViewModel //viewModel özelliği, NewsViewModel sınıfından bir örneği tutar. NewsViewModel, kullanıcı arayüzü ile veri kaynağı arasındaki arabirimdir ve kullanıcı arayüzünden verileri alır, verileri işler ve görüntüler için hazır hale getirir.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val newsRepository = NewsRepository(ArticleDatabase(this)) //NewsRepository sınıfından bir örnek oluşturur ve ArticleDatabase sınıfından geçirerek NewsRepository sınıfı için bir veritabanı bağlantısı oluşturur.
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java] //ViewModelFactory'nin bir örneğini oluşturur. ViewModelProvider'ın ilk parametresi this olarak ayarlanırken, ikinci parametre, oluşturduğu NewsRepository örneği ile birlikte oluşturulan ViewModelFactory'dir.

        binding=ActivityMainBinding.inflate(layoutInflater) //binding değişkeni, ActivityMainBinding sınıfı tarafından sağlanan inflate() yöntemi kullanılarak oluşturulur ve setContentView() yöntemiyle görüntülenir.

        setContentView(binding.root)

        binding.bottomNavigationView.setupWithNavController( //bottomNavigationView öğesi, setupWithNavController() yöntemi kullanılarak geçerli gezinme denetleyicisi (NavHostFragment) ile bağlanır.
            Navigation.findNavController(
                this,
                R.id.newsNavHostFragment
            )
        )

    }
}