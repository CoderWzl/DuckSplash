package wzl.android.ducksplash.model

import wzl.android.ducksplash.PhotoListType

/**
 *Created on 1/10/21
 *@author zhilin
 * 主页上方 tab 数据
 */
data class ListTabModel(
    val photoListType: PhotoListType,
    val title: String
)
