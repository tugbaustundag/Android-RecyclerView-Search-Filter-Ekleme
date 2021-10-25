package com.smality.searchrecyclerview

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.*
import com.smality.searchrecyclerview.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var adapter: RecyclerView_Adapter
    lateinit var countryrv: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //SearchView de bulunan Arama iconu tanımlayıp rengini belirledim
        val searchIcon = binding.countrySearch.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)
        //SearchView de bulunan kapama iconunu tanımlayıp rengini belirledim
        val cancelIcon = binding.countrySearch.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)
        //SearchView alanında aranacak yazının rengini belirlemek için TextView tanımladım.
        val textView = binding.countrySearch.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.BLACK)

        countryrv = findViewById(R.id.country_rv)
        countryrv.layoutManager = LinearLayoutManager(countryrv.context)
        countryrv.setHasFixedSize(true)
        //Listelemedeki çizgileri oluşturmak için Recyclerview özellik ekliyorum.
        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        countryrv.addItemDecoration(decorator)
        //SearchView alanına aranacak kelime yazılırken karakter değişikliklerini dinleyen metod
        // Aramada kullanıcı yazarken karakter değişikliklerini dinler.
        binding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                //Karakter değişikliğini RecyclerView_Adapter sınıfına aktararak filtrelemeyi sağlar
                adapter.filter.filter(newText)
                return false
            }

        })
        //Ülke isim ve bayrak bilgisini getiren metodu çağırdık
        getListOfCountries()
    }

    private fun getListOfCountries() {
        //getISOCountries metodu ile tüm 2 harfli ülke kodlarının bir listesini alıyoruz
        val isoCountryCodes = Locale.getISOCountries()
        val countryListWithEmojis = ArrayList<String>()
        for (countryCode in isoCountryCodes) {
            //countryCode ile ülke isimleri elde ediyoruz
            val locale = Locale("", countryCode)
            val countryName = locale.displayCountry
            //countryCode ile ülkenin bayrak görsellerini String halini elde editoruz.
            val flagOffset = 0x1F1E6
            val asciiOffset = 0x41
            val firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset
            val secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset
            val flag = (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
            //Bayrak ve ülke isminide ArrayList ekliyoruz.
            countryListWithEmojis.add("$flag $countryName")
        }
        adapter = RecyclerView_Adapter(countryListWithEmojis)
        countryrv.adapter = adapter
    }
}
