package wzl.android.ducksplash.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wzl.android.ducksplash.adapter.PhotoListAdapter
import wzl.android.ducksplash.adapter.PhotoLoadStateAdapter
import wzl.android.ducksplash.adapter.PhotoPagingAdapter
import wzl.android.ducksplash.api.createApiService
import wzl.android.ducksplash.api.httpClient
import wzl.android.ducksplash.databinding.FragmentPhotoListBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.repository.PhotoRepository
import wzl.android.ducksplash.viewmodel.PhotoListViewModel
import wzl.android.ducksplash.viewmodel.PhotoListViewModelFactory

private const val TAG = "PhotoListFragment"

/**
 * zhilin
 * 图片列表界面
 */
class PhotoListFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoListFragment()
    }

    private lateinit var viewBinding: FragmentPhotoListBinding

    private lateinit var viewModel: PhotoListViewModel
    private val mAdapter = PhotoPagingAdapter(object : DiffUtil.ItemCallback<PhotoModel>() {
        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem == newItem
        }

    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = FragmentPhotoListBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
            footer = PhotoLoadStateAdapter {
                mAdapter.retry()
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, PhotoListViewModelFactory(PhotoRepository(
            createApiService(okHttpClient = httpClient)))).get(PhotoListViewModel::class.java)
        lifecycleScope.launch { 
            viewModel.getPhotos().collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}