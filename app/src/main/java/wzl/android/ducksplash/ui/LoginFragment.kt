package wzl.android.ducksplash.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import wzl.android.ducksplash.util.customtabs.CustomTabsHelper
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.R
import wzl.android.ducksplash.api.ApiState
import wzl.android.ducksplash.databinding.FragmentLoginBinding
import wzl.android.ducksplash.util.loadBlurredImage
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.LoginViewModel

/**
 *Created on 2021/3/10
 *@author zhilin
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewBinding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginBinding.inflate(inflater)
        viewBinding.root.reserveStatusBar()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.loginButton.setOnClickListener { openUnsplashLoginTab() }
        viewBinding.createAccountButton.setOnClickListener { openCreateAccountTab() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.photoData.observe(viewLifecycleOwner) {
            val url = it.urls.raw + "&w=1200&q=80&fm=webp"
            viewBinding.randomPhoto.loadBlurredImage(
                url = url,
                color = it.color
            )
        }
        viewModel.authCode.observe(viewLifecycleOwner) {
            Log.d("wzl", "onActivityCreated: $it")
            viewModel.startLogin(it)
        }
        viewModel.loginState.observe(viewLifecycleOwner) {
            when(it) {
                is ApiState.Success -> {
                    Toast.makeText(requireContext(), "login success", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is ApiState.Error -> {
                    Toast.makeText(requireContext(), "login error : ${it.message}", Toast.LENGTH_SHORT).show()
                }
                is ApiState.Loading -> {
                    // TODO: 2021/3/12
                }
            }
        }
        viewModel.getRandomPhoto()
    }

    private fun openUnsplashLoginTab() = openCustomTab(viewModel.loginUrl)

    private fun openCreateAccountTab() = openCustomTab(getString(R.string.unsplash_join_url))

    private fun openCustomTab(uriString: String) {
        CustomTabsHelper.openCustomTab(requireContext(), Uri.parse(uriString))
    }

}