package wzl.android.ducksplash.adapter

import androidx.recyclerview.widget.DiffUtil
import wzl.android.ducksplash.model.CollectionModel

class CollectionDiffCallback: DiffUtil.ItemCallback<CollectionModel>() {
    override fun areItemsTheSame(oldItem: CollectionModel, newItem: CollectionModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CollectionModel, newItem: CollectionModel): Boolean {
        return oldItem == newItem
    }

}