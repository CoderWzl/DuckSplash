package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import wzl.android.ducksplash.SearchType
import wzl.android.ducksplash.adapter.SearchFragmentAdapter
import wzl.android.ducksplash.databinding.FragmentSearchBinding
import wzl.android.ducksplash.util.hideKeyboard
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.SearchViewModel

private const val TAG = "SearchFragment"
class SearchFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel
    private val viewBinding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.apply {
            root.reserveStatusBar()
            toolBar.setNavigationOnClickListener {
                it.hideKeyboard()
                findNavController().navigateUp()
            }
            textInputLayout.editText?.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.updateQuery(textInputLayout.editText?.text.toString())
                    textView.hideKeyboard()
                }
                return@setOnEditorActionListener false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.viewPager.adapter = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        Log.d(TAG, "onActivityCreated: $viewModel")
        viewBinding.viewPager.adapter = SearchFragmentAdapter(this@SearchFragment, viewModel)
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager) { tab, position ->
            tab.text = requireContext().getString(SearchType.values()[position].titleId)
        }.attach()
    }

}