package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.databinding.ItemPhotoListBinding

/**
 *Created on 2021/1/11
 *@author zhilin
 */
class PhotoListAdapter(val datas: List<String>) : RecyclerView.Adapter<PhotoListAdapter.PhotoListVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListVH = PhotoListVH(ItemPhotoListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
    ))

    override fun onBindViewHolder(holder: PhotoListVH, position: Int) {
        holder.setupView(position)
    }

    override fun getItemCount(): Int = datas.size

    inner class PhotoListVH(val viewBinding: ItemPhotoListBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        fun setupView(position: Int) {
            viewBinding.textView.text = datas[position]
        }
    }
}