package com.dilara.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dilara.news.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.dilara.news.databinding.FragmentArticleBinding
import com.dilara.news.ui.NewsViewModel

class ArticleFragment:Fragment() {

    private var _binding:FragmentArticleBinding?=null //FragmentArticleBinding sınıfının nullable bir referansını _binding değişkenine atar. Bu değişken, fragmentin layout dosyasıyla ilişkilendirilmiş olan öğeleri temsil eder.
    val binding get() = _binding!! //_binding değişkenine güvenli erişim sağlamak için bir getter işlevi oluşturur. Bu sayede, _binding değişkeni null olmadığından emin olunur.
    lateinit var viewModel:NewsViewModel //NewsViewModel türünde bir değişken olan viewModeli tanımlar. Bu değişken, haber verilerine erişim sağlayan ViewModel'i temsil eder. lateinit ifadesi, değişkenin daha sonra başlatılacağını belirtir.
    val args: ArticleFragmentArgs by navArgs() //ArticleFragmentArgs sınıfından oluşturulan bir args değişkenini tanımlar. navArgs() ifadesi, fragmentin argümanlarını almak için kullanılır. Bu değişken, fragmente geçirilen argümanları temsil eder ve argüman değerlerine erişimi sağlar.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //Bu kod parçacığı, onViewCreated fonksiyonunu içerir. Bu fonksiyon, fragmentin oluşturulduğu zaman çağrılır ve fragmentin görünümü oluşturulduktan sonra yapılması gereken işlemleri içerir.
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel // fragmentin içinde bulunduğu MainActivity'de tanımlanan viewModel örneğini elde eder. Bu sayede, fragmentin ViewModel'ine erişim sağlanır.

        val article = args.article //fragmente geçirilen argümanlardan article nesnesini alır. Bu nesne, seçilen haber makalesini temsil eder.
        binding.webView.apply {
            //webView adlı WebView öğesine ayarlar yapar ve içeriği yükler.
            webViewClient= WebViewClient() //WebView'in içerik yüklemesi ve gezinme olaylarını yöneten bir WebViewClient atanır.
            loadUrl(article.url!!) //WebView'e makalenin URL'sini yüklemek için kullanılır. article.url ifadesi, seçilen makalenin URL'sini temsil eder.
        }
        binding.fab.setOnClickListener {
            // Floating Action Button'a bir tıklama olayı dinleyicisi ekler.
            viewModel.saveArticle(article) // ViewModel üzerinden saveArticle işlevini çağırarak makaleyi favorilere kaydeder.
            Snackbar.make(view,"Favorilere Eklendi",Snackbar.LENGTH_SHORT).show() //kısa bir Snackbar mesajı görüntüler ve kullanıcıya favorilere eklendiğini bildirir.
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentArticleBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}