package com.dilara.news

import android.app.Application

class NewsApplication:Application (){//NewsApplication sınıfı, Application sınıfından kalıtım alan bir sınıftır. Uygulama başlatıldığında, önceden yapılandırılmış bazı özelliklerin yüklenmesi için kullanılır. Bu sınıf, uygulamanın ömrü boyunca küresel olarak erişilebilir olacak NewsRepository nesnesinin oluşturulmasını sağlar.
    //onCreate() yöntemi, uygulama başlatıldığında çağrılan yöntemdir. Bu yöntemde, NewsRepository nesnesi oluşturulur ve NewsViewModelProviderFactory sınıfına bu nesne ve uygulama bağlamı (application) parametre olarak geçilerek bir ViewModelProvider nesnesi oluşturulur.
    //Bu ViewModelProvider nesnesi, NewsViewModel sınıfının örneğini oluşturmak için kullanılır.
    //Sonrasında, ActivityMainBinding sınıfı kullanılarak ana etkinlik içeriği ayarlanır ve alt gezinme çubuğu, ana etkinlik içeriğine bağlı bir navigasyon kontrolörüyle ayarlanır.
}