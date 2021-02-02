package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.PhotoDiffCallback
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.PhotoPagingAdapter
import wzl.android.ducksplash.databinding.FragmentPhotoListBinding
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.viewmodel.NavMainViewModel
import wzl.android.ducksplash.viewmodel.PhotoListViewModel
import javax.inject.Inject

/**
 * zhilin
 * 图片列表界面
 */
@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoListFragment()
    }

    private lateinit var viewBinding: FragmentPhotoListBinding

    private val viewModel: PhotoListViewModel by viewModels()
    @Inject lateinit var mAdapter: PhotoPagingAdapter

    private val vm by navGraphViewModels<NavMainViewModel>(R.id.nav_main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = FragmentPhotoListBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
            footer = FooterLoadStateAdapter {
                mAdapter.retry()
            }
        )
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.addLoadStateListener { state ->
            viewBinding.recyclerView.isVisible = state.source.refresh is LoadState.NotLoading
            viewBinding.loadingLayout.loadingContainer.isVisible = state.source.refresh !is LoadState.NotLoading
            viewBinding.loadingLayout.loadingTip.isVisible = state.source.refresh is LoadState.Error
            viewBinding.loadingLayout.progressBar.isVisible = state.source.refresh !is LoadState.Error
            viewBinding.loadingLayout.loadingTip.setOnClickListener {
                mAdapter.retry()
            }
        }
        mAdapter.onPhotoClickListener = {
            it?.let {
                findNavController().navigateSafe(
                    NavMainDirections.actionGlobalToPhotoDetailFragment(it)
                )
            }
        }
        mAdapter.onUserClickListener = {
            it?.let {
                findNavController().navigateSafe(
                    NavMainDirections.actionGlobalUserFragment(it)
                )
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch { 
            viewModel.getPhotos().collectLatest {
                mAdapter.submitData(it)
            }
        }
        vm.photoIso.observe(viewLifecycleOwner) { it ->
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

}