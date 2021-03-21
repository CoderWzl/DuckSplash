package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import wzl.android.ducksplash.repository.UserRepository

/**
 *Created on 2021/3/19
 *@author zhilin
 * 共享 ViewModel 生命周期不依赖 Fragment 生命周期，其依赖 Fragment 所依附的 Activity 的生命周期
 * 实现 Fragment 之间共享数据。
 */
class MainSharedViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

}