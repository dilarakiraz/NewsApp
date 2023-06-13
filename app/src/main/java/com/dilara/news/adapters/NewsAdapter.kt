package com.dilara.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dilara.news.R
import com.dilara.news.databinding.ItemArticlePreviewBinding
import com.dilara.news.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter() :RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){//Bu kod bloğu, NewsAdapter adlı sınıfın tanımını içerir. Bu sınıf, bir RecyclerView'a veri bağlamak ve görüntülemek için kullanılan bir adaptördür. Adaptörün içinde, öğelerin nasıl görüntüleneceği ve öğelerde hangi verilerin gösterileceği tanımlanmıştır.

    inner class ArticleViewHolder(val binding:ItemArticlePreviewBinding): //inner class ArticleViewHolder iç içe sınıfı, her bir görüntü öğesinin içeriğini görüntüleyecek olan öğe tutucuyu tanımlar. Öğe tutucu, görünüm öğelerinin referanslarını saklar ve verileri görünüme bağlamak için kullanılır. RecyclerView.ViewHolder sınıfından miras alınır.
        RecyclerView.ViewHolder(binding.root) //binding.root ifadesi, öğe tutucusunun kök görünümünü döndürür. Kök görünüm, her öğenin ana düzen dosyasını temsil eder ve burada ItemArticlePreviewBinding sınıfı tarafından temsil edilir.

    private val differCallback= object :DiffUtil.ItemCallback<Article>(){//Bu kod bloğu, RecyclerView listesinde görüntülenecek öğelerin farklılaştırılması için kullanılan DiffUtil sınıfı için bir geri arama nesnesi oluşturur.
    // differCallback, Article sınıfının öğeleri için iki öğenin aynı olduğunu (aynı URL'ye sahip olduklarını) ve içeriklerinin aynı olduğunu kontrol eder.
        //DiffUtil.ItemCallback<Article>() olarak adlandırılan DiffUtil.ItemCallback sınıfından türetilen bir anonim sınıf tanımlanır.
    // Bu sınıfın areItemsTheSame() yöntemi, iki öğenin benzersiz kimliğini karşılaştırarak aynı olup olmadığını belirler.
    // areContentsTheSame() yöntemi ise, iki öğenin içeriklerini karşılaştırarak aynı olup olmadığını belirler.
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }//Bu geri arama nesnesi, ListAdapter sınıfının bir örneği oluşturulurken kullanılır ve RecyclerView listesindeki öğelerin değiştirilmesi gerektiğinde kullanılır. Bu sayede, RecyclerView listesi hızlı bir şekilde güncellenebilir ve tekrar oluşturulabilir.

    val differ = AsyncListDiffer(this,differCallback) //Bu kod, ListAdapter'ın alt sınıfında tanımlanmış bir AsyncListDiffer nesnesi oluşturur. AsyncListDiffer, bir önceki listedeki öğelerle karşılaştırılarak yeni bir liste oluşturur. Bu nesne, RecyclerView listesindeki öğelerin değiştirilmesi gerektiğinde kullanılır ve DiffUtil'in areItemsTheSame ve areContentsTheSame yöntemlerini kullanarak iki liste arasındaki farkları bulur.
    //AsyncListDiffer'in yapısı, birbirinden farklı iş parçacıklarında farklılaştırma işlemlerinin gerçekleştirilmesini sağlar, böylece uygulama kullanıcısına kesintisiz bir deneyim sunulabilir. Bu nesne, ListAdapter'ın içindeki submitList() yöntemi tarafından kullanılır ve RecyclerView listesindeki öğelerin değiştirilmesi gerektiğinde otomatik olarak tetiklenir.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder= //oluşturulması için kullanılır.
        //onCreateViewHolder yöntemi, RecyclerView'da her bir görüntü öğesi oluşturulduğunda çağrılır. Bu yöntem, parent olarak adlandırılan ViewGroup nesnesini ve viewType olarak adlandırılan öğe türü değerini alır. viewType, farklı öğe türleri için kullanılabilir.

            ArticleViewHolder( //ArticleViewHolder sınıfının bir örneği oluşturulur. Bu sınıf, görünüm öğelerinin içeriğini ve görüntülenme şeklini yönetir. Örneğin, bu sınıf, başlık, açıklama, resim vb. öğelerin yerleşimini ve düzenini belirleyebilir.
                ItemArticlePreviewBinding.inflate( //ItemArticlePreviewBinding sınıfından bir örnek oluşturulur ve bu sınıf, öğe arayüzünün görünüm yapısını ve bileşenlerini tanımlar. inflate() yöntemi, arayüzün XML dosyasından şişirilerek bir View nesnesine dönüştürülmesini sağlar.
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )//parent ve false parametreleri, oluşturulan View nesnesinin hemen RecyclerView'da görüntülenmemesi için kullanılır. Son olarak, bu View nesnesi ArticleViewHolder sınıfına gönderilerek, sınıfın bir örneği oluşturulur ve bu örnek, RecyclerView'da görüntülenen öğenin arayüzünü yönetir.
            )



    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {//Bu kod bloğu, ArticleViewHolder sınıfındaki görünümlere verileri bağlar. differ değişkeni, AsyncListDiffer sınıfından bir örnektir ve bu öğelerin nasıl değiştiğini izler ve otomatik olarak listeyi günceller.
        //onBindViewHolder fonksiyonu, position parametresine göre belirtilen öğeyi alır ve onu holder görünümüne yerleştirir. Bu işlem, Glide kütüphanesi kullanılarak resim yüklemek
        val article = differ.currentList[position]
        holder.itemView.apply { //Burada Glide kütüphanesi kullanılarak, ivArticleImage ImageView öğesi içerisine, verilen article.urlToImage url'sinden görsel yüklenir.
            //tvSource, tvTitle, tvDescription, ve tvPublishedAt TextView öğeleri, ilgili article nesnesinin özelliklerine göre set edilir.
            Glide.with(holder.itemView.context).load(article.urlToImage).into(ivArticleImage)
            tvSource.text=article.source?.name
            tvTitle.text=article.title
            tvDescription.text=article.description
            tvPublishedAt.text=article.publishedAt

            holder.itemView.setOnClickListener {//onItemClickListener tetiklendiğinde bir işlev çağırmak da dahil olmak üzere, her öğe için yapılır.
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {//getItemCount() metodu, listenin mevcut öğelerinin sayısını, differ öğesinin currentList özelliğinin boyutunu döndürür. Bu, RecyclerView'ın içinde gösterilecek öğelerin sayısını belirler.
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null //Bu bir sınıf değişkenidir ve bu sınıfın içinde tanımlanmış bir lambda işlevi içerir.
    // Bu değişken, öğelerin tıklanmasına yanıt olarak çağrılacak işlevi tutar. Bu değişkenin değeri bir kez ayarlandıktan sonra, öğeler tıklandığında tetiklenecek olan işlev belirlenmiş olur.

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }//Bu kod bloğu, onItemClickListener değişkenini, bir öğeye tıklandığında yapılacak işlevi çağırmak üzere ayarlar. setOnItemClickListener fonksiyonu, bir Article öğesi ile çağrılacak bir işlev parametresi alır ve bu işlevi, onItemClickListener değişkenine atar. Bu işlev, bir öğe tıklandığında onBindViewHolder fonksiyonunda tetiklenecektir.

}