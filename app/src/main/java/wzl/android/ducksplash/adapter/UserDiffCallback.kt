package wzl.android.ducksplash.adapter

import androidx.recyclerview.widget.DiffUtil
import wzl.android.ducksplash.model.UserModel
import javax.inject.Inject

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class UserDiffCallback @Inject constructor(): DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem == newItem
    }
}