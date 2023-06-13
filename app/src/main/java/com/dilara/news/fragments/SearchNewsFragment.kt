package com.dilara.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dilara.news.ui.MainActivity
import com.dilara.news.R
import com.dilara.news.adapters.NewsAdapter
import com.dilara.news.databinding.FragmentSearchNewsBinding
import com.dilara.news.ui.NewsViewModel
import com.dilara.news.util.Constants
import com.dilara.news.util.Constants.SEARCH_NEWS_TIME_DELAY
import com.dilara.news.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment() {

    private var _binding:FragmentSearchNewsBinding?=null//_binding değişkeni, SearchNewsFragment'in bağlama sınıfı olan FragmentSearchNewsBinding tipinde bir nullable değişken olarak tanımlanır.
    // Bağlama sınıfı, fragment'in XML dosyasındaki arayüz öğelerine erişmek için kullanılır.
    val binding get()= _binding!! //binding değişkeni, bağlama sınıfını temsil eden _binding değişkenine referans oluşturur.
    // Bu değişken, güvenli null olmayan (non-null) bir şekilde kullanılmak üzere tanımlanır.
    lateinit var viewModel:NewsViewModel  //viewModel değişkeni, NewsViewModel tipinde bir geç başlatılan değişken olarak tanımlanır.
    // Bu değişken, haberlerin veri yönetimi ve iş mantığından sorumlu olan ViewModel sınıfına referans oluşturur. lateinit anahtar kelimesi, değişkenin geç başlatılacağını ve başlatılmadan önce doğrudan erişim yapılmaması gerektiğini belirtir.
    lateinit var newsAdapter: NewsAdapter //newsAdapter değişkeni, NewsAdapter tipinde bir geç başlatılan değişken olarak tanımlanır.
    // Bu değişken, haberlerin RecyclerView'da gösterilmesi için kullanılan adaptöre referans oluşturur. lateinit anahtar kelimesi, değişkenin geç başlatılacağını ve başlatılmadan önce doğrudan erişim yapılmaması gerektiğini belirtir.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel //viewModel değişkeni, fragmentin bağlı olduğu MainActivity'deki ViewModel referansına atanır.
        // Bu, haber verilerinin yönetimi ve iş mantığından sorumlu olan ViewModel'e erişimi sağlar.
        setupRecyclerView() //setupRecyclerView() fonksiyonu, RecyclerView'i yapılandırmak için çağrılır.
        // Bu fonksiyon, RecyclerView'in görünüm düzenini ve adaptörünü ayarlamayı içerir.

        newsAdapter.setOnItemClickListener {
            //newsAdapter adaptörünün, öğe tıklama olaylarını dinlemek üzere bir dinleyiciye sahip olmasını sağlar.
            //{ } içindeki kod, bir öğe tıklandığında yapılacak işlemleri belirtir.
            //it parametresi, tıklanan öğeyi temsil eder.
            val bundle=Bundle().apply {
                //Bir Bundle oluşturulur ve içine "article" adında bir seri hale getirilebilir nesne eklenir.
                // Bu nesne, tıklanan haber öğesini içerir.
                putSerializable("article",it)
            }
            findNavController().navigate(
                //Navigation component kullanılarak, SearchNewsFragment'ten ArticleFragment'e geçiş yapılır.
                //R.id.action_searchNewsFragment_to_articleFragment geçişin hedef aksiyonunu temsil eder.
                //bundle parametresi, geçiş sırasında taşınacak verileri içerir.
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        var job: Job?=null//job değişkeni, gerçekleştirilen arama işleminin takibini yapmak için kullanılan bir Job nesnesini temsil eder. İlk olarak, null olarak başlatılır.
        binding.etSearch.addTextChangedListener{  editable ->
            //binding.etSearch ifadesi, arama metni için kullanılan EditText bileşenine erişim sağlar.
            //addTextChangedListener metodu, EditText'in metin değişikliklerini dinlemek için bir TextWatcher nesnesini bağlar.
            job?.cancel() //Önceki arama işlemi hala devam ediyorsa, job nesnesini iptal eder.
            // Böylece, yeni bir arama yapılırken önceki arama işlemi durdurulur.
            job = MainScope().launch {
                //Yeni bir Job nesnesi oluşturur ve job değişkenine atar.
                //MainScope() bir Scope oluşturur ve launch metodu bu Scope içinde bir coroutine başlatır.
                // Bu, arama işleminin arka planda gerçekleştirilmesini sağlar.
                delay(SEARCH_NEWS_TIME_DELAY) //Belirli bir süre (SEARCH_NEWS_TIME_DELAY) boyunca gecikme yapar.
                // Bu, kullanıcının metin girişini tamamlamasını beklemek için kullanılır.
                editable?.let {
                    //editable değişkeni, metin değişikliği ile birlikte gelen düzenlenebilir (editable) metni temsil eder.
                    //let fonksiyonu, editable değişkeninin null olmadığı durumlarda içindeki kod bloğunu çalıştırır.
                    if (editable.toString().isNotEmpty()){
                        //Düzenlenebilir metin, boş olmadığı durumda (yani bir arama terimi girilmişse) içerideki kod bloğunu çalıştırır.
                        viewModel.searchNews(editable.toString()) //ViewModel sınıfındaki searchNews fonksiyonunu çağırarak, verilen arama terimini kullanarak haber arama işlemini başlatır.
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer{ response->
            //kısmı, haber arama işleminin sonucunu dinlemek için bir Observer oluşturur.
            // Bu Observer, LiveData olan searchNews değişkenini izler ve değişiklik olduğunda belirtilen aksiyonları gerçekleştirir.
            //viewModel.searchNews LiveData'sı, haber arama işleminin sonucunu temsil eder.
            //observe(viewLifecycleOwner, Observer { response -> }) kısmı, Observer'ı oluşturur ve viewLifecycleOwner'a bağlar. Bu sayede Observer, Fragment'ın yaşam döngüsü boyunca aktif kalır ve otomatik olarak güncellenir.
            when(response){ //response değişkeninin durumuna göre farklı işlemler yapılmasını sağlar
                is Resource.Success ->{ //Haber arama işlemi başarılı olduysa bu durum gerçekleşir. İçerideki blok, newsResponse ile gelen haber verilerini kullanarak RecyclerView'a haberleri gönderir. Ayrıca, toplam sayfa sayısını hesaplar ve isLastPage durumunu günceller.
                    hideProgressBar()
                    response.data?.let { newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage ==totalPages
                        if (isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> { //Haber arama işleminde bir hata oluştuysa bu durum gerçekleşir. İçerideki blok, hata mesajını gösteren bir Toast mesajı görüntüler.
                    hideProgressBar()
                    response.message?.let { message->
                        Toast.makeText(activity,"An error occured: $message",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> showProgressBar() //Haber arama işlemi devam ediyorsa bu durum gerçekleşir. İçerideki blok, ilerleme çubuğunu gösterir
            } //hideProgressBar(), showProgressBar() ve diğer işlevler, ilerleme çubuğunu kontrol etmek ve görünürlüğünü ayarlamak için kullanılır. Bu işlevler, ilgili görünümleri güncellemek için kullanılır.
        })
    }

    private fun hideProgressBar(){ //hideProgressBar() fonksiyonu, ilerleme çubuğunu gizlemek ve isLoading değişkenini false olarak ayarlamak için kullanılır.
        binding.paginationProgressBar.visibility=View.INVISIBLE //binding.paginationProgressBar.visibility = View.INVISIBLE ifadesi, ilerleme çubuğunun görünürlüğünü INVISIBLE olarak ayarlar, yani görünmez hale getirir.
        isLoading=false //isLoading = false ifadesi, isLoading değişkenini false olarak ayarlar. Bu değişken, ilerleme çubuğunun görünüp görünmediğini kontrol etmek için kullanılır.
    }
    private fun showProgressBar(){ //showProgressBar() fonksiyonu, ilerleme çubuğunu göstermek ve isLoading değişkenini true olarak ayarlamak için kullanılır.
        binding.paginationProgressBar.visibility=View.VISIBLE //binding.paginationProgressBar.visibility = View.VISIBLE ifadesi, ilerleme çubuğunun görünürlüğünü VISIBLE olarak ayarlar, yani görünür hale getirir.
        isLoading=true //isLoading = true ifadesi, isLoading değişkenini true olarak ayarlar. Bu değişken, ilerleme çubuğunun görünüp görünmediğini kontrol etmek için kullanılır.
    }
    private fun setupRecyclerView(){ //setupRecyclerView() fonksiyonu, RecyclerView'ı yapılandırmak için kullanılır.
        newsAdapter= NewsAdapter() //newsAdapter = NewsAdapter() ifadesi, NewsAdapter sınıfından bir örnek oluşturur ve newsAdapter değişkenine atar.
        binding.rvSearchNews.apply {
            //rvSearchNews adlı RecyclerView'a uygulanan ayarları tanımlar.
            adapter=newsAdapter //RecyclerView'a newsAdapter'ı atar. Bu, RecyclerView'ın hangi adaptörü kullanacağını belirtir.
            layoutManager=LinearLayoutManager(activity) //RecyclerView'ın hangi düzenleyiciyi kullanacağını belirtir. Bu durumda, lineer bir düzenleyici olan LinearLayoutManager kullanılır.
            addOnScrollListener(this@SearchNewsFragment.scrollListener) //RecyclerView'a kaydırma olaylarını dinleyen bir scrollListener ekler. this@SearchNewsFragment ifadesi, mevcut fragmenta erişmek için kullanılır ve scrollListener bu fragmentın bir üyesi olarak tanımlanır.
        }
    }

    var isLoading = false
    var isLastPage=false
    var isScrolling=false

    val scrollListener=object :RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView,dx:Int,dy:Int){ //Bu işlev, RecyclerView'da kaydırma işlemi gerçekleştiğinde tetiklenir. İçerideki blok, kaydırma işlemiyle ilgili kontrolleri yapar ve sayfalama işlemlerini yönetir.
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager //RecyclerView'ın kullanılan düzenleyicisini alır ve layoutManager değişkenine atar. Bu durumda, LinearLayoutManager kullanılmaktadır.
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition() //görünen ilk öğenin pozisyonunu alır.
            val visibleItemCount=layoutManager.childCount //görünen öğelerin sayısını alır.
            val totalItemCount=layoutManager.itemCount // toplam öğe sayısını alır.

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage //yüklenme durumunda olunmadığını ve son sayfada olunmadığını kontrol eder.
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount //son öğede olunup olunmadığını kontrol eder.
            val isNotAtBeginning = firstVisibleItemPosition >= 0 //başlangıçta olup olunmadığını kontrol eder.
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE //toplam öğe sayısının görünen öğe sayısından fazla olup olmadığını kontrol eder.

            val shouldPaginate = //sayfalamanın yapılıp yapılmayacağını kontrol eder. Tüm koşullar sağlandığında sayfalama işlemi gerçekleştirilir.
                isNotLoadingAndNotLastPage && isAtLastItem &&
                        isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.searchNews(binding.etSearch.text.toString()) //ViewModel'deki searchNews fonksiyonunu çağırarak haberleri arar.
                isScrolling=false
            }
        }
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) { //Bu işlev, RecyclerView'ın kaydırma durumu değiştiğinde tetiklenir. İçerideki blok, kaydırma durumunu kontrol eder ve gerekli ayarlamaları yapar.
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { //aydırma durumunun SCROLL_STATE_TOUCH_SCROLL olduğunu kontrol eder. Bu durumda, kullanıcı dokunarak kaydırma yapıyor demektir.
                isScrolling = true //isScrolling değişkenini true olarak ayarlar.
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding=FragmentSearchNewsBinding.inflate(inflater,container,false) //FragmentSearchNewsBinding sınıfını kullanarak fragmentin bağlamını oluşturur. Bağlam, fragmentin layout dosyasıyla ilişkilendirilmiş olan öğeleri temsil eder.

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null //bağlam değişkenini null olarak ayarlar. Bu, fragmentin referansını temizlemek ve bellek sızıntılarını önlemek için yapılır.
    }
}//Bu iki fonksiyon, fragmentin yaşam döngüsü sırasında uygun şekilde bağlamı oluşturur ve yok eder. onCreateView fonksiyonu fragmentin görünümünü oluştururken, onDestroy fonksiyonu fragmentin yok edildiğinde bağlamı temizler.