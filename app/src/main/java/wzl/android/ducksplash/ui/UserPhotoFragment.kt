package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.PhotoDiffCallback
import wzl.android.ducksplash.adapter.PhotoPagingAdapter
import wzl.android.ducksplash.viewmodel.UserPhotoViewModel
import kotlin.math.log

/**
 *Created on 2/1/21
 *@author zhilin
 */
@AndroidEntryPoint
class UserPhotoFragment: UserContentListFragment() {

    private val viewModel: UserPhotoViewModel by viewModels()
    private val mAdapter: PhotoPagingAdapter = PhotoPagingAdapter(PhotoDiffCallback())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.testText.text = "user photo"
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
            footer = FooterLoadStateAdapter{
                mAdapter.retry()
            }
        )
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.onPhotoClickListener = {

        }
        mAdapter.onUserClickListener = {

        }
        mAdapter.addLoadStateListener {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("zhilin", "onActivityCreated: $username")
        lifecycleScope.launch {
            viewModel.listUserPhotos(username).collectLatest {
                Log.d("zhilin", "onActivityCreated: $it")
                mAdapter.submitData(it)
            }
        }
    }
}