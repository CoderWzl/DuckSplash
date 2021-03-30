package wzl.android.ducksplash.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.ItemAddCollectionBinding
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.CollectionPhotoResult
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import javax.inject.Inject

/**
 *Created on 3/21/21
 *@author zhilin
 * 我的界面添加图集
 */
class AddCollectionAdapter @Inject constructor(
    diffCallback: CollectionDiffCallback
): ListAdapter<CollectionModel, AddCollectionVH>(diffCallback) {

    var onItemClickListener: ((collection: CollectionModel, position: Int) -> Unit)? = null
    private var _stateMap: MutableMap<Int, AddState>? = null
    private val _currentUserCollections: MutableList<Int> = mutableListOf()

    fun submitCurrentUserCollections(list: List<Int>?) {
        list?.let {
            _currentUserCollections.clear()
            _currentUserCollections.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun changeItemAddState(id: Int, state: AddState) {
        if (_stateMap == null) {
            _stateMap = HashMap()
        }
        _stateMap?.let { map ->
            if (!map.contains(id) || map[id] != state) {
                map[id] = state
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: AddCollectionVH, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindView(item, _stateMap, _currentUserCollections)
            holder.itemView.setOnClickListener { onItemClickListener?.invoke(item, position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCollectionVH {
        return AddCollectionVH(
            ItemAddCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

class AddCollectionVH(
    private val viewBinding: ItemAddCollectionBinding
): RecyclerView.ViewHolder(viewBinding.root) {

    fun bindView(item: CollectionModel, map: Map<Int, AddState>?, currentCollections: List<Int>) {
        viewBinding.collectionName.text = item.title
        viewBinding.photoCount.text = itemView.context.getString(
            R.string.photo_number,
            item.totalPhotos?:0
        )
        viewBinding.lockSign.isVisible = item.private?: false
        item.coverPhoto?.let { photo ->
            val imageUrl = photo.urls.raw + "&w=800&q=80&fm=webp"
            val thumbUrl = photo.urls.raw + "&w=200&q=80&fm=webp"
            viewBinding.cover.loadPhotoUrlWithThumbnail(
                url = imageUrl,
                color = photo.color,
                thumbnailUrl = thumbUrl
            )
        }
        if (currentCollections.contains(item.id)) {
            viewBinding.addProgress.isVisible = false
            viewBinding.addedIcon.isVisible = true
            viewBinding.coverOverlay.setBackgroundColor(
                ContextCompat.getColor(itemView.context, R.color.green_overlay)
            )
        } else {
            map?.let { stateMap ->
                if (stateMap.containsKey(item.id)) {
                    when (stateMap[item.id]) {
                        is AddState.NotAdd -> {
                            viewBinding.addProgress.isVisible = false
                            viewBinding.addedIcon.isVisible = false
                            viewBinding.coverOverlay.setBackgroundColor(
                                ContextCompat.getColor(itemView.context, R.color.black_overlay)
                            )
                        }
                        is AddState.Adding,
                        is AddState.Removing -> {
                            viewBinding.addProgress.isVisible = true
                            viewBinding.addedIcon.isVisible = false
                        }
                        is AddState.Added -> {
                            viewBinding.addProgress.isVisible = false
                            viewBinding.addedIcon.isVisible = true
                            viewBinding.coverOverlay.setBackgroundColor(
                                ContextCompat.getColor(itemView.context, R.color.green_overlay)
                            )
                        }
                    }
                } else {
                    viewBinding.addProgress.isVisible = false
                    viewBinding.addedIcon.isVisible = false
                    viewBinding.coverOverlay.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.black_overlay)
                    )
                }
            }
        }
    }
}

sealed class AddState {
    data class NotAdd(val collectionId: Int): AddState()
    data class Added(val result: CollectionPhotoResult): AddState()
    data class Adding(val collectionId: Int): AddState()
    data class Removing(val collectionId: Int): AddState()
}