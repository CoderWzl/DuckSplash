package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.databinding.ItemTagListBinding
import wzl.android.ducksplash.model.TagModel

/**
 *Created on 1/29/21
 *@author zhilin
 */
class TagListAdapter: ListAdapter<TagModel, TagViewHolder>(TagDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            ItemTagListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TagViewHolder(
    private val viewBinding: ItemTagListBinding
): RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: TagModel) {
        viewBinding.tagTextView.text = item.title
    }
}

class TagDiffCallback: DiffUtil.ItemCallback<TagModel>() {
    override fun areItemsTheSame(oldItem: TagModel, newItem: TagModel): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: TagModel, newItem: TagModel): Boolean {
        return oldItem == newItem
    }

}