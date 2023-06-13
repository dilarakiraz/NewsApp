package com.dilara.news.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dilara.news.ui.MainActivity
import com.dilara.news.R
import com.dilara.news.adapters.NewsAdapter
import com.dilara.news.databinding.FragmentBreakingNewsBinding
import com.dilara.news.ui.NewsViewModel
import com.dilara.news.util.Constants.QUERY_PAGE_SIZE
import com.dilara.news.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment:Fragment() {

    private var _binding:FragmentBreakingNewsBinding?=null
    val binding get()= _binding!!

    private val viewModel:NewsViewModel by activityViewModels()
    lateinit var newsAdapter:NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //Bu kod parçacığı, fragmentin görünümü oluşturulduğunda RecyclerView'in yapılandırılmasını ve verilerin gösterilmesini sağlar.
        // Ayrıca, haber makalelerine tıklanma olayını dinler ve kırık haberleri (breaking news) almak için ViewModel'den gelen verileri işler.
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView() //RecyclerView'i yapılandırır ve haber makalelerini görüntülemek için kullanılan adapteri ayarlar.

        newsAdapter.setOnItemClickListener {
            //haber makalelerine tıklanma olayını dinler ve tıklanan haber makalesini bir pakete ekleyerek ArticleFragment'e geçiş yapar.
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer{ response-> //ViewModel'den gelen kırık haber verilerini gözlemleyerek işler.
            when(response){
                is Resource.Success -> { //is Resource.Success durumunda, başarılı bir şekilde veri alındığında çalışır. Veri mevcutsa, RecyclerView'e verileri gönderir, sayfalama için gerekli kontrolleri yapar ve gerekirse RecyclerView'in padding'ini ayarlar.
                    hideProgressBar()
                    response.data?.let {newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults / QUERY_PAGE_SIZE +2
                        isLastPage=viewModel.breakingNewsPage==totalPages
                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
            is Resource.Error ->{ //is Resource.Error durumunda, hata durumunda çalışır. Hata mesajını kullanıcıya gösterir.
                hideProgressBar()
                response.message?.let {message->
                    Toast.makeText(activity,"An error occured: $message",Toast.LENGTH_LONG).show()
                }
            }
             is Resource.Loading-> showProgressBar() //is Resource.Loading durumunda, veriler yüklenirken çalışır. Kullanıcıya ilerleme çubuğunu gösterir.

            }
        })
    }

    private fun hideProgressBar(){ //hideProgressBar() işlevi, sayfalama ilerleme çubuğunun görünürlüğünü kapatır ve isLoading değişkenini false olarak ayarlar.
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar(){ //showProgressBar() işlevi, sayfalama ilerleme çubuğunun görünürlüğünü açar ve isLoading değişkenini true olarak ayarlar
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    override fun onCreateView( //onCreateView() işlevi, fragmentin görünümünü oluşturur.
        // İlgili bağlama (_binding) nesnesini inflate eder ve root görünümünü döndürür. Bu işlev, Fragment'in oluşturulduğu yerde çağrılır.
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentBreakingNewsBinding.inflate(inflater,container,false)
        return binding.root
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener=object :RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView,dx:Int,dy:Int){ //onScrolled() işlevi, RecyclerView'daki kaydırma olaylarına yanıt verir.
            // İlk olarak, super.onScrolled() çağrısı ile RecyclerView'daki varsayılan kaydırma işlemleri gerçekleştirilir.
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager //layoutManager değişkeni, RecyclerView'ın layout yöneticisini (LinearLayoutManager) temsil eder.
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition() //firstVisibleItemPosition değişkeni, görünürdeki ilk öğenin konumunu alır.
            val visibleItemCount=layoutManager.childCount //visibleItemCount değişkeni, görünürdeki öğelerin sayısını alır.
            val totalItemCount=layoutManager.itemCount //totalItemCount değişkeni, toplam öğe sayısını alır.

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage //isNotLoadingAndNotLastPage değişkeni, hem yükleme işlemi yapılmıyor hem de son sayfada olunmuyorsa true değerini alır.
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount //isAtLastItem değişkeni, son görünür öğeden sonra gelen tüm öğelerin sayısı, toplam öğe sayısına eşit veya büyükse true değerini alır.
            val isNotAtBeginning = firstVisibleItemPosition >= 0 //isNotAtBeginning değişkeni, ilk görünür öğenin pozitif bir konumda olduğunda true değerini alır.
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE //isTotalMoreThanVisible değişkeni, toplam öğe sayısının görünür öğelerin sayısından büyük veya eşit olduğunda true değerini alır.

            val shouldPaginate= //shouldPaginate değişkeni, sayfalama koşullarının sağlandığı durumlarda true değerini alır.
                isNotLoadingAndNotLastPage && isAtLastItem &&
                        isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){ //Eğer shouldPaginate true ise, viewModel üzerinden getBreakingNews() işlevi çağrılır ve isScrolling değişkeni false olarak ayarlanır.
                viewModel.getBreakingNews("us")
                isScrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) { //onScrollStateChanged() işlevi, RecyclerView'da kaydırma durumu değiştiğinde çağrılır.
            // ikinci parametre olarak newState değeri alır.
            super.onScrollStateChanged(recyclerView, newState) //newState parametresi, RecyclerView'ın yeni kaydırma durumunu temsil eder.
            if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){ //kullanıcının RecyclerView üzerinde parmağıyla kaydırmaya başladığını gösterir.
                isScrolling=true //Eğer newState değeri AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL ise, isScrolling değişkeni true olarak ayarlanır.
            }
        }
    }

    private fun setupRecyclerView(){ // RecyclerView'ı yapılandırır. İlk olarak, newsAdapter nesnesi oluşturulur.
        // Daha sonra, RecyclerView'a adapter atanır, layout yöneticisi (LinearLayoutManager) belirlenir ve scrollListener nesnesi eklenir
        newsAdapter= NewsAdapter()

        binding.rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    override fun onDestroy() { //onDestroy() işlevi, fragmentin yok edildiği zaman çağrılır.
        // Bu noktada, _binding değişkeni null olarak ayarlanır, böylece fragment artık bağlantısızdır ve hafıza sızıntısını önlemek için bellekten serbest bırakılır.
        super.onDestroy()
        _binding=null
    }
}