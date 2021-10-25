package com.smality.searchrecyclerview

import android.content.*
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.smality.searchrecyclerview.databinding.RecyclerviewRowBinding
import java.util.*
import kotlin.collections.ArrayList
class RecyclerView_Adapter(private var countryList: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var countryFilterList = ArrayList<String>()
    lateinit var mContext: Context

    class CountryHolder(var viewBinding: RecyclerviewRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    init {
        countryFilterList = countryList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val sch = CountryHolder(binding)
        mContext = parent.context
        return sch
    }

    override fun getItemCount(): Int {
        return countryFilterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val countryHolder = holder as CountryHolder
        //Listelenecek ülke isimlerinin ArrayList'den arayüze aktarılması
        countryHolder.viewBinding.selectCountryContainer.setBackgroundColor(Color.TRANSPARENT)
        countryHolder.viewBinding.selectCountryText.setTextColor(Color.BLACK)
        countryHolder.viewBinding.selectCountryText.text = countryFilterList[position]
        //Seçilen ülkenin ismini DetailsActivity aktarıyoruz
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetailsActivity::class.java)
            intent.putExtra("passselectedcountry", countryFilterList[position])
            mContext.startActivity(intent)
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //Aranan yazı
                val charSearch = constraint.toString()
                //Arama alanında bir yazı aranmadıysa tüm ülkeleri countryFilterList aktarıyoruz
                if (charSearch.isEmpty()) {
                    countryFilterList = countryList
                } else {
                    val resultList = ArrayList<String>()
                    for (row in countryList) {
                        //Aranan ülke varsa add metodu ile listeye eklenir
                        if (row.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    countryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }

        }
    }

}