package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.PhotoDiffCallback
import wzl.android.ducksplash.adapter.PhotoPagingAdapter
import wzl.android.ducksplash.databinding.FragmentSearchPhotoBinding
import wzl.android.ducksplash.viewmodel.SearchViewModel

private const val TAG = "SearchPhotoFragment"

class SearchPhotoFragment : Fragment() {

    companion object {
        fun newInstance(vm: SearchViewModel) = SearchPhotoFragment().apply {
            viewModel = vm
        }
    }

    private lateinit var viewModel: SearchViewModel

    private lateinit var viewBinding: FragmentSearchPhotoBinding

    private val mAdapter = PhotoPagingAdapter(PhotoDiffCallback())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchPhotoBinding.inflate(inflater)
        Log.d(TAG, "onCreateView: ")
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
                footer = FooterLoadStateAdapter {
                    mAdapter.retry()
                }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: $viewModel")
        viewModel.queryLiveData.observe(this as LifecycleOwner) {
            Log.d(TAG, "queryLiveData: $it")
            if (it.isNotBlank()) {
                viewModel.searchPhotos(it).observe(viewLifecycleOwner) {
                    lifecycleScope.launch {
                        mAdapter.submitData(it)
                    }
                }
            }
        }
    }

}