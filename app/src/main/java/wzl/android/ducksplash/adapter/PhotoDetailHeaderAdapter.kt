package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDetailHeaderVh {
        return PhotoDetailHeaderVh(
            onTagClickListener,
            onUserClickListener,
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
    private val viewBinding: LayoutPhotoDetailHeaderBinding
): RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: PhotoModel) {
        viewBinding.apply {
            manufacturer.text = item.exif?.make
            model.text = item.exif?.model
            focalLength.text = item.exif?.focalLength
            exposure.text = item.exif?.iso.toString()
            size.text = "${item.width} x ${item.height}"
            color.text = item.color
            shutterTime.text = item.exif?.exposureTime
            aperture.text = item.exif?.aperture
            download.text = item.downloads.toString()
            favorite.text = item.likes.toString()
            view.text = item.views.toString()
            val headUrl = item.user.profileImage.large
            userHead.loadCirclePhotoUrl(headUrl)
            userName.text = "${item.user.firstName} ${item.user.lastName?:""}"
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
                onUserClickListener?.invoke(item.user)
            }
            userHead.setOnClickListener(clickListener)
            userName.setOnClickListener(clickListener)
        }
    }
}