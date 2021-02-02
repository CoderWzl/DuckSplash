package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.adapter.CollectionPagingAdapter
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.databinding.FragmentCollectionListBinding
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.viewmodel.CollectionListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CollectionListFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionListFragment()
    }

    private lateinit var viewBinding: FragmentCollectionListBinding
    private val viewModel: CollectionListViewModel by viewModels()

    @Inject lateinit var mAdapter: CollectionPagingAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCollectionListBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
                footer = FooterLoadStateAdapter {
                    mAdapter.retry()
                }
        )
        mAdapter.addLoadStateListener {
            viewBinding.apply {
                recyclerView.isVisible = it.source.refresh is LoadState.NotLoading
                loadingLayout.loadingContainer.isVisible = it.source.refresh !is LoadState.NotLoading
                loadingLayout.loadingTip.isVisible = it.source.refresh is LoadState.Error
                loadingLayout.progressBar.isVisible = it.source.refresh !is LoadState.Error
                loadingLayout.loadingTip.setOnClickListener {
                    mAdapter.retry()
                }
            }
        }
        mAdapter.onCollectionClickListener = {
            val fullName = if (it.coverPhoto.user.lastName == null) {
                it.coverPhoto.user.firstName
            } else {
                it.coverPhoto.user.firstName + " " + it.coverPhoto.user.lastName
            }
            findNavController().navigateSafe(
                NavMainDirections.actionGlobalToCollectionDetailFragment(
                    it.id,
                    it.title,
                    it.totalPhotos,
                    it.description,
                    fullName
                )
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
        lifecycleScope.launch {
            viewModel.getCollections().collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}