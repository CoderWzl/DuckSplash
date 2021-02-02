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
import wzl.android.ducksplash.adapter.SimplePhotoPagingAdapter
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.viewmodel.UserPhotoViewModel
import javax.inject.Inject

/**
 *Created on 2/1/21
 *@author zhilin
 */
@AndroidEntryPoint
class UserPhotoFragment: UserContentListFragment() {

    private val viewModel: UserPhotoViewModel by viewModels()
    @Inject lateinit var mAdapter: SimplePhotoPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
            footer = FooterLoadStateAdapter{
                mAdapter.retry()
            }
        )
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.onPhotoClickListener = {
            findNavController().navigateSafe(
                NavMainDirections.actionGlobalToPhotoDetailFragment(it)
            )
        }
        mAdapter.addLoadStateListener {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.listUserPhotos(username).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }
}