package com.dilara.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dilara.news.ui.MainActivity
import com.dilara.news.R
import com.dilara.news.adapters.NewsAdapter
import com.dilara.news.databinding.FragmentSavedNewsBinding
import com.dilara.news.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment:Fragment() {

    private var _binding:FragmentSavedNewsBinding?=null
    val binding get()= _binding!!
    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel // fragmentin içinde bulunduğu MainActivity'de tanımlanan viewModel örneğini elde eder. Bu sayede, fragmentin ViewModel'ine erişim sağlanır.
        setupRecyclerView() //RecyclerView'i yapılandırır ve haber makalelerini görüntülemek için kullanılan adapteri ayarlar.

        newsAdapter.setOnItemClickListener {
            //haber makalelerinin tıklanma olayını dinler.
            val bundle=Bundle().apply {
                //tıklanan makalenin bir örneğini bir demet (bundle) içine koyar.
                putSerializable("article",it)
            }
            findNavController().navigate( // Navigation bileşenini kullanarak tıklanan makalenin ayrıntılarının bulunduğu ArticleFragment'e geçişi başlatır.
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallback=object :ItemTouchHelper.SimpleCallback( // bloğu, ItemTouchHelper sınıfının SimpleCallback sınıfından türetilmiş bir geri çağırma (callback) nesnesi oluşturur. Bu nesne, RecyclerView öğesinde sürükleme ve kaydırma işlemlerini yönetir.
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove( // öğelerin taşınması (yer değiştirmesi) durumunda çağrılır. Bu durumda true değeri döndürülerek taşıma işlemine izin verilir.
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //onSwiped işlevi, öğelerin kaydırılması durumunda çağrılır. Bu durumda, kaydırılan öğenin pozisyonu ve yönü alınır. Daha sonra, ViewModel üzerinden deleteArticle işlevi çağrılarak makale silinir ve kullanıcıya bir Snackbar mesajı gösterilir. Snackbar mesajının içinde "Undo" adında bir geri alma seçeneği bulunur ve bu seçeneğe tıklanması durumunda silme işlemi geri alınır.
                val position =viewHolder.adapterPosition
                val article=newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Deleted!",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            // ItemTouchHelper nesnesini oluşturur ve RecyclerView'e bağlar. Bu sayede, sürükleme ve kaydırma işlemleri RecyclerView'de etkin hale gelir.
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles -> // ViewModel üzerindeki getSavedNews işlevinin sonucunu izler. Bu işlev, kaydedilmiş haber makalelerinin listesini döndürür. Observer, bu liste güncellendiğinde tetiklenir ve yeni listeyle birlikte haberlerin RecyclerView adapterine iletilmesini sağlar.
            newsAdapter.differ.submitList(articles)
        })

        //Bu kod parçacığı, fragmentin görünümü oluşturulduğunda ViewModel'e erişim sağlar, RecyclerView'i yapılandırır ve kaydedilmiş haber makalelerini görüntüler.
    // Ayrıca, haber makalelerinin tıklanma olayını dinler, sürükleme ve kaydırma işlemlerini yönetir ve kaydedilmiş haber makalelerinin güncellemelerini takip eder.
    }

    private fun setupRecyclerView(){ //setupRecyclerView() işlevi, RecyclerView'i yapılandırır ve haber makalelerini görüntülemek için kullanılan adapteri ayarlar.
        newsAdapter= NewsAdapter() //NewsAdapter sınıfından bir örnek oluşturur.

        binding.rvSavedNews.apply {
            // RecyclerView öğesine adapteri ve düzenleyiciyi (layoutManager) atar. Bu sayede, RecyclerView makaleleri görüntülemek için kullanılabilir.
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }

    override fun onCreateView( //fragmentin oluşturulduğu zaman çağrılır ve fragmentin görünümünü oluşturur.
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSavedNewsBinding.inflate(inflater,container,false) //ragmentSavedNewsBinding sınıfını kullanarak fragmentin görünümünü şişirir (inflate) ve _binding özelliğine atar.

        return binding.root
    }

    override fun onDestroy() { //onDestroy() fonksiyonu, fragmentin yok edildiği zaman çağrılır ve bağlantıyı (binding) boşaltır.
        super.onDestroy()
        _binding=null
    }
}//Bu kod parçacığı, RecyclerView'in yapılandırılması ve fragmentin oluşturulması/yok edilmesiyle ilgili işlemleri içerir. RecyclerView yapılandırması, fragmentin oluşturulduğu görünümün şişirilmesi ve bağlantının temizlenmesi gibi işlemler yer alır.