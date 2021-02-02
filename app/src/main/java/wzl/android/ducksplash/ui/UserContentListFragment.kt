package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.databinding.FragmentUserContentListBinding

/**
 *Created on 2/1/21
 *@author zhilin
 */
const val ARG_USER_NAME = "arg_user_name"

open class UserContentListFragment: Fragment() {

    protected lateinit var username: String
    protected lateinit var viewBinding: FragmentUserContentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USER_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentUserContentListBinding.inflate(inflater)
        return viewBinding.root
    }

    companion object {
        fun <T: UserContentListFragment> newInstance(clazz: Class<T>, username: String): Fragment{
            return clazz.newInstance().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_NAME, username)
                }
            }
        }
    }

}