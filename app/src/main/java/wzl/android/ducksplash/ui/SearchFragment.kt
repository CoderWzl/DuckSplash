package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.SearchType
import wzl.android.ducksplash.adapter.SearchFragmentAdapter
import wzl.android.ducksplash.databinding.FragmentSearchBinding
import wzl.android.ducksplash.util.hideKeyboard
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.SearchViewModel

private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var viewBinding: FragmentSearchBinding
    private lateinit var mAdapter: SearchFragmentAdapter

    // 别这么使用，这将导致重新走 onCreateView 生命周期的时候复用上一次 onDestroyView
    // 之前的状态，ViewPage，RecyclerView 复用的话存储的状态需要处理，每次 回调 onCreateView
    // 方法重新 inflate 一个 ViewBinding 实例
    /*private val viewBinding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.textInputLayout.editText?.setText(viewModel.queryLiveData.value?:"")
        Log.d(TAG, "onActivityCreated: $viewModel")
        mAdapter = SearchFragmentAdapter(this@SearchFragment, viewModel)
        viewBinding.viewPager.adapter = mAdapter
        viewBinding.viewPager.offscreenPageLimit = 1
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager) { tab, position ->
            tab.text = requireContext().getString(SearchType.values()[position].titleId)
        }.attach()
    }

}