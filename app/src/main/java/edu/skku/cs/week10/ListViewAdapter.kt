package edu.skku.cs.week10

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

data class Show(
    val score: Double? = null,
    val show: Showinfo? = null
){
    data class Showinfo(val id: Int?, val name: String?)
}

data class Episode(
    val number: Int? = null,
    val season: Int? = null,
    val name: String? = null,
    val airdate: String? = null
)

class ListViewAdapter(val context : Context, val items:List<Episode>): BaseAdapter() {
    override fun getCount(): Int {
        return items.size

    }

    override fun getItem(p0: Int): Any {
        return items.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.episode_item, null)

        val season_tv = view.findViewById<TextView>(R.id.season)
        val number_tv = view.findViewById<TextView>(R.id.number)
        val name_tv = view.findViewById<TextView>(R.id.name)
        val airdate_tv = view.findViewById<TextView>(R.id.airdate)

        season_tv.text = items.get(p0).season.toString()
        number_tv.text = items.get(p0).number.toString()
        name_tv.text = items.get(p0).name.toString()
        airdate_tv.text = items.get(p0).airdate.toString()

        return view
    }
}