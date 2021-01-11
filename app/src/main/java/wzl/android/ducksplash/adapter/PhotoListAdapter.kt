package wzl.android.ducksplash.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import wzl.android.ducksplash.databinding.ItemPhotoListBinding
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2021/1/11
 *@author zhilin
 */
class PhotoListAdapter :
    ListAdapter<PhotoModel, PhotoListAdapter.ViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPhotoListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupView(getItem(position))
    }

    inner class ViewHolder(private val viewBinding: ItemPhotoListBinding):
        RecyclerView.ViewHolder(viewBinding.root) {

            fun setupView(item: PhotoModel) {
                viewBinding.imageView.setBackgroundColor(Color.parseColor(item.color))
                Glide.with(itemView.context)
                    .load(item.urls.thumb)
                    .centerCrop()
                    .into(viewBinding.imageView)
            }
    }
}

private class PhotoDiffCallback : DiffUtil.ItemCallback<PhotoModel>() {
    override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem == newItem
    }
}