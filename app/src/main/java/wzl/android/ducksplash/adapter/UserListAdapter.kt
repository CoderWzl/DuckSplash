package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.databinding.ItemUserListBinding
import wzl.android.ducksplash.model.UserModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail

/**
 *Created on 1/18/21
 *@author zhilin
 */
class UserListAdapter : ListAdapter<UserModel, UserListAdapter.ViewHolder>(UserDiffCallback()) {

    inner class ViewHolder(val viewBinding: ItemUserListBinding):
        RecyclerView.ViewHolder(viewBinding.root) {

            fun setupView(item: UserModel) {
                viewBinding.apply {
                    userHead.loadCirclePhotoUrl(item.profileImage.large)
                    userName.text = "${item.firstName} ${item.lastName?:""}"
                    userInsName.text = "@${item.username}"
                    image1.aspectRatio = 16 / 9.0
                    image2.aspectRatio = 16 / 9.0
                    image3.aspectRatio = 16 / 9.0
                    if (item.photos == null || item.photos.isEmpty()) {
                        imageContainer.visibility = View.GONE
                    } else {
                        imageContainer.visibility = View.VISIBLE
                        val img1 = item.photos[0].urls.raw + "&w=400&q=80&fm=webp"
                        val img2 = item.photos[1].urls.raw + "&w=400&q=80&fm=webp"
                        val img3 = item.photos[2].urls.raw + "&w=400&q=80&fm=webp"
                        image1.loadPhotoUrl(img1)
                        image2.loadPhotoUrl(img2)
                        image3.loadPhotoUrl(img3)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupView(getItem(position))
    }
}

private class UserDiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem == newItem
    }

}