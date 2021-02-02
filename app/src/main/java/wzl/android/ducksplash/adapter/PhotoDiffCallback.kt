package wzl.android.ducksplash.adapter

import androidx.recyclerview.widget.DiffUtil
import wzl.android.ducksplash.model.PhotoModel
import javax.inject.Inject

class PhotoDiffCallback @Inject constructor() : DiffUtil.ItemCallback<PhotoModel>() {
    override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem == newItem
    }
}