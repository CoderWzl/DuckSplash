package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.UserDiffCallback
import wzl.android.ducksplash.adapter.UserPagingAdapter
import wzl.android.ducksplash.databinding.FragmentSearchUserBinding
import wzl.android.ducksplash.viewmodel.SearchViewModel

class SearchUserFragment : Fragment() {

    companion object {
        fun newInstance(vm: SearchViewModel) = SearchUserFragment().apply {
            viewModel = vm
        }
    }

    private lateinit var viewBinding: FragmentSearchUserBinding

    private lateinit var viewModel: SearchViewModel

    private val mAdapter = UserPagingAdapter(UserDiffCallback())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchUserBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
                footer = FooterLoadStateAdapter{
                    mAdapter.retry()
                }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.queryLiveData.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                viewModel.searchUsers(it).observe(viewLifecycleOwner) {
                    lifecycleScope.launch {
                        mAdapter.submitData(it)
                    }
                }
            }
        }
    }

}