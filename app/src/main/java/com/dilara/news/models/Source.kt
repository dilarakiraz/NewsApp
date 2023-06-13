package com.dilara.news.models

data class Source( //Source sınıfı, bir haber kaynağının adını ve kimliğini temsil eden bir veri sınıfıdır. id alanı, bir haber kaynağının benzersiz kimliğini temsil eder ve name alanı, bir haber kaynağının adını temsil eder.
// Örneğin, id alanı "bbc-news" olan bir kaynak için name alanı "BBC News" olabilir. Bu sınıf genellikle JSON'dan verileri ayrıştırmak için kullanılır.
    val id: Any,
    val name: String
){

}