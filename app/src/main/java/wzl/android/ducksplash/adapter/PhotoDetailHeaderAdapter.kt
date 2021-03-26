package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.LayoutPhotoDetailHeaderBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.UserModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl

/**
 *Created on 1/29/21
 *@author zhilin
 */
class PhotoDetailHeaderAdapter(): RecyclerView.Adapter<PhotoDetailHeaderVh>() {

    var photo: PhotoModel? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onTagClickListener: ((tag: String) -> Unit)? = null
    var onUserClickListener: ((user: UserModel) -> Unit)? = null
    var onDownloadClickListener: (() -> Unit)? = null
    var onBookmarkClickListener: (() -> Unit)? = null
    var onFavoriteClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDetailHeaderVh {
        return PhotoDetailHeaderVh(
            onTagClickListener,
            onUserClickListener,
            onDownloadClickListener,
            onBookmarkClickListener,
            onFavoriteClickListener,
            LayoutPhotoDetailHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoDetailHeaderVh, position: Int) {
        if (photo != null) {
            holder.bind(photo!!)
        }
    }

    override fun getItemCount(): Int {
        return if (photo == null) 0 else 1
    }
}

class PhotoDetailHeaderVh(
    private val onTagClickListener: ((tag: String) -> Unit)?,
    private val onUserClickListener: ((user: UserModel) -> Unit)?,
    private val onDownloadClickListener: (() -> Unit)?,
    private val onBookmarkClickListener: (() -> Unit)?,
    private val onFavoriteClickListener: (() -> Unit)?,
    private val viewBinding: LayoutPhotoDetailHeaderBinding
): RecyclerView.ViewHolder(viewBinding.root) {

    private val unknownStr: String = itemView.context.getString(R.string.unknown)

    fun bind(item: PhotoModel) {
        viewBinding.apply {
            manufacturer.text = item.exif?.make?:unknownStr
            model.text = item.exif?.model?:unknownStr
            focalLength.text = item.exif?.focalLength?:unknownStr
            exposure.text = if (item.exif?.iso == null) unknownStr else item.exif.iso.toString()
            size.text = "${item.width} x ${item.height}"
            color.text = item.color
            shutterTime.text = item.exif?.exposureTime?:unknownStr
            aperture.text = item.exif?.aperture?:unknownStr
            download.text = item.downloads.toString()
            favorite.text = item.likes.toString()
            view.text = item.views.toString()
            val headUrl = item.user?.profileImage?.large
            userHead.loadCirclePhotoUrl(headUrl)
            userName.text = "${item.user?.firstName?:""} ${item.user?.lastName?:""}"
            tagList.layoutManager = LinearLayoutManager(
                itemView.context,
                RecyclerView.HORIZONTAL,
                false
            )
            tagList.adapter = TagListAdapter().also {
                it.submitList(item.tags)
                it.onTagClickListener = onTagClickListener
            }
            val clickListener = View.OnClickListener{
                item.user?.let {
                    onUserClickListener?.invoke(it)
                }
            }
            userHead.setOnClickListener(clickListener)
            userName.setOnClickListener(clickListener)
            downloadButton.setOnClickListener {
                onDownloadClickListener?.invoke()
            }
            favoriteButton.setOnClickListener {
                onFavoriteClickListener?.invoke()
            }
            bookmarkButton.setOnClickListener {
                onBookmarkClickListener?.invoke()
            }
            favoriteButton.setImageResource(
                if (item.likedByUser == true)
                    R.drawable.ic_favorite_filled_24dp
                else
                    R.drawable.ic_favorite_border_24dp
            )
            bookmarkButton.setImageResource(
                if (item.currentUserCollections?.isNotEmpty() == true)
                    R.drawable.ic_bookmark_filled_24dp
                else
                    R.drawable.ic_bookmark_border_24dp
            )
        }
    }
}