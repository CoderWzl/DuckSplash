package wzl.android.ducksplash.model

import kotlinx.parcelize.Parcelize
import wzl.android.ducksplash.PhotoListType

/**
 *Created on 1/10/21
 *@author zhilin
 * 主页上方 tab 数据
 */
@Parcelize
data class ListTabModel(
    val photoListType: PhotoListType,
    val title: String
)
