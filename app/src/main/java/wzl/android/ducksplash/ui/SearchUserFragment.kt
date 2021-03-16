package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.UserDiffCallback
import wzl.android.ducksplash.adapter.UserPagingAdapter
import wzl.android.ducksplash.databinding.FragmentSearchUserBinding
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.util.toast
import wzl.android.ducksplash.viewmodel.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchUserFragment : Fragment() {

    companion object {
        fun newInstance(vm: SearchViewModel) = SearchUserFragment().apply {
            viewModel = vm
        }
    }

    private lateinit var viewBinding: FragmentSearchUserBinding

    private lateinit var viewModel: SearchViewModel

    @Inject lateinit var mAdapter: UserPagingAdapter

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
        mAdapter.onPhotoClickListener = {
            findNavController().navigateSafe(
                NavMainDirections.actionGlobalToPhotoDetailFragment(it)
            )
        }
        mAdapter.onUserClickListener = {
            findNavController().navigateSafe(
                NavMainDirections.actionGlobalUserFragment(it)
            )
        }
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