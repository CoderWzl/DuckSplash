package wzl.android.ducksplash.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.PhotoDiffCallback
import wzl.android.ducksplash.adapter.PhotoPagingAdapter
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.viewmodel.UserFavoriteViewModel
import javax.inject.Inject

/**
 *Created on 2/1/21
 *@author zhilin
 */
@AndroidEntryPoint
class UserFavoriteFragment: UserContentListFragment() {

    private val viewModel by viewModels<UserFavoriteViewModel>()
    @Inject lateinit var mAdapter: PhotoPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
            footer = FooterLoadStateAdapter{
                mAdapter.retry()
            }
        )
    }

    private fun initAdapter() {
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
        mAdapter.addLoadStateListener {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.listUserLikePhotos(username).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}