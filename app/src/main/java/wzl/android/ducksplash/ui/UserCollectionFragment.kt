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
import wzl.android.ducksplash.adapter.CollectionPagingAdapter
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.SimpleCollectionPagingAdapter
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.viewmodel.UserCollectionViewModel
import javax.inject.Inject

/**
 *Created on 2/1/21
 *@author zhilin
 */
@AndroidEntryPoint
class UserCollectionFragment: UserContentListFragment() {

    private val viewModel by viewModels<UserCollectionViewModel>()
    @Inject lateinit var mAdapter: SimpleCollectionPagingAdapter

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
        mAdapter.addLoadStateListener {

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.listUserCollections(username).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }
}