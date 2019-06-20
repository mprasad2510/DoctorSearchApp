package org.mp.doctorsearchapp.di.feature.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.doctors_list_items.view.*
import org.mp.doctorsearchapp.data.remote.model.Doctors
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.model.GlideUrl




class DoctorListAdapter (val context : Context,val dataSource: List<Doctors>,private val share:(Doctors)->Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(org.mp.doctorsearchapp.R.layout.doctors_list_items, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(dataSource[position]){
            holder.name?.text = name
            holder. address?.text = address
            val imageUrl = "https://api.staging.vivy.com/api/doctors/" + { id } + "/keys/profilepictures?name="
                Glide.with(context)
                    .load(Headers.getUrlWithHeaders(imageUrl))
                    .into(holder.image)
            holder.item.setOnClickListener {
                share.invoke(this)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item = view
    val name = view.headlineTv
    val image = view.coverImage
    val address = view.descriptionTv
}
internal object Headers {

    private val AUTHORIZATION = "${HomeActivity.mAccessToken}"

    fun getUrlWithHeaders(url: String): GlideUrl {
        return GlideUrl(
            url, LazyHeaders.Builder()
                .addHeader("Authorization", AUTHORIZATION)
                .build()
        )
    }
}

