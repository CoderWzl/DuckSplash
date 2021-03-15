package wzl.android.ducksplash

import androidx.annotation.StringRes

/**
 *Created on 1/10/21
 *@author zhilin
 * 定义 App 模块中使用的常量
 */
// 数据列表请求时的域名
const val BASE_URL = "https://api.unsplash.com/"
// 获取 token 时的域名
const val BASE_LOGIN_URL = "https://unsplash.com/"
// unsplash 官网申请的 access key
const val USPLASH_CLIENT_ID = "w72LwQkOD-iFlBdIFeqJkGZqbxLBuN5vQ68OlLBhKg0"
const val USPLASH_CLIENT_SECRET = "bz8eLgv6TtWe-fxmFRjq3sGpAITCSIR2O5c9GNC0teg"
// unsplash 第一页 index 为 1
const val INITIAL_PAGE = 1;

const val IMAGE_LARGE_SUFFIX = "&w=1200&q=80&fm=webp"
const val IMAGE_THUMB_SUFFIX = "&w=200&q=80&fm=webp"
const val IMAGE_SMALL_SUFFIX = "&w=300&q=80&fm=webp"

enum class PhotoListType {
    PHOTO_LIST_NEW,
    PHOTO_LIST_COLLECTIONS
}

enum class SearchType(@StringRes val titleId : Int) {
    PHOTO(R.string.photo),
    COLLECTION(R.string.collection),
    USER(R.string.user)
}