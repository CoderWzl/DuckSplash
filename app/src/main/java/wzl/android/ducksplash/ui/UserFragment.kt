package wzl.android.ducksplash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import wzl.android.ducksplash.databinding.FragmentUserBinding
import wzl.android.ducksplash.util.reserveStatusBar

/**
 *Created on 1/31/21
 *@author zhilin
 * 用户详情界面
 */
class UserFragment: Fragment() {

    private lateinit var viewBinding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentUserBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.appBarLayout.reserveStatusBar()
    }
}