package com.dilara.news.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "articles") //Bu kod parçasında, bir makale öğesi belirtilen özelliklere sahiptir. @Entity işareti, sınıfın bir veritabanı tablosu olduğunu belirtir ve tableName parametresi, bu sınıfın veritabanındaki tablo adını belirler.

data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null, //id: Makale öğesinin benzersiz kimliğini tutar. Bu, @PrimaryKey işaretiyle belirtilir ve autoGenerate özelliği true olarak ayarlanarak, kimliğin otomatik olarak oluşturulacağı belirtilir.
    val author: String?, //author: Makale yazarının adını tutar.
    val content: String?, //content: Makale içeriğini tutar.
    val description: String?,//description: Makale özeti veya açıklamasını tutar.
    val publishedAt: String?,//publishedAt: Makalenin yayınlandığı tarihi ve saati tutar.
    val source: Source?,//source: Makalenin kaynağını (örneğin, "BBC News" veya "The New York Times") temsil eden bir Source nesnesini tutar. Bu nesne, içinde name ve id özellikleri bulunan bir veri sınıfıdır.
    val title: String?,//title: Makale başlığını tutar.
    val url: String?,//url: Makaleye bağlantı veren URL'yi tutar.
    val urlToImage: String?//urlToImage: Makaleye ait resim URL'sini tutar.
):Serializable{//Serializable arabirimini uygulaması, bu sınıfın nesnelerinin diğer bileşenler arasında seri hale getirilerek geçirilebileceği anlamına gelir.

}