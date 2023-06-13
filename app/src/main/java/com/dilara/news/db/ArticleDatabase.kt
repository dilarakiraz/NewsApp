package com.dilara.news.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dilara.news.models.Article

@Database( //Bu bir Room veritabanı nesnesi anotasyonudur.
// @Database özniteliği, veritabanı sınıfını belirtir ve veritabanı dosyasının adını ve veritabanı sürüm numarasını tanımlar. Bu örnekte, entities parametresi ile veritabanındaki varlıklar belirtilir ve version parametresi ile veritabanı sürüm numarası belirtilir.
// entities öğesi, veritabanında depolanan her bir öğe için bir varlık sınıfı belirtir. version öğesi, veritabanının şeması değiştiğinde güncellendiğinde arttırılır.
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class) //Bu anotasyon, veritabanında depolanan ve daha sonra geri alınacak verilerin belirli türlerini dönüştürmek için kullanılan dönüştürücü sınıfların kullanılacağını belirtir. Örneğin, bu belirteç, tarihleri ve saatleri SQLite veritabanında depolamak için kullanılan Unix zaman damgası biçiminden dönüştürmek için bir sınıf olan Converters'ı kullanır.

abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDao():ArticleDao //Bu bir soyut metottur ve bu metot, uygulamanın veritabanı işlemlerini gerçekleştirebilmesi için ArticleDao arabirimi üzerinden bir DAO nesnesi sağlar. Bu metot, veritabanı bağlantısı için kullanılan RoomDatabase sınıfı tarafından uygulanacaktır.

    companion object{//Bu kod bloğu, ArticleDatabase sınıfının öğelerine erişim sağlar ve tek bir veritabanı örneği oluşturur.
        @Volatile //@Volatile kelimesi, bu değişkenin birden fazla thread tarafından değiştirilebileceğini ve değişikliklerin diğer thread'lerce görülebilmesi gerektiğini belirtir.
        private var instance:ArticleDatabase?= null //instance değişkeni, ArticleDatabase sınıfının tek örneği olarak tutulur.
        private val LOCK= Any() //LOCK değişkeni, senkronize edilecek obje olarak kullanılır.

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance =it}
        }//operator fun invoke metodu, ArticleDatabase sınıfının örneğini almak için kullanılır. Metodun içinde, instance null ise ve birden fazla thread aynı anda erişemeyeceği LOCK objesi kullanılarak createDatabase fonksiyonu çağrılır ve bir ArticleDatabase örneği oluşturulur. instance'e değer atandıktan sonra bu değer geri döndürülür.

        private fun createDatabase(context: Context) = //createDatabase fonksiyonu, Room veritabanını oluşturur. İlk parametre olan context.applicationContext, context referansını kullanarak, ömür döngüsü Activity'ye bağlı olmayan uygulama bağlamını elde eder. İkinci parametre, oluşturulacak veritabanı sınıfıdır. Son parametre olan "article_db.db", veritabanı dosyasının adını belirtir. build() fonksiyonu, Room veritabanını oluşturur ve bu örneği geri döndürür.
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()

    }
}