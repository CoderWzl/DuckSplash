package wzl.android.ducksplash.api

/**
 *Created on 2021/3/12
 *@author zhilin
 * api 加载状态
 */
sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T? = null): ApiState<T>()
    data class Error(val code: Int? = null, val message: String? = null): ApiState<Nothing>()
}
