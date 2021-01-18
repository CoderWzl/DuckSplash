package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import wzl.android.ducksplash.adapter.PhotoListAdapter
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

    private val viewBinding: FragmentSearchPhotoBinding by lazy {
        FragmentSearchPhotoBinding.inflate(layoutInflater)
    }

    private val mAdapter = PhotoListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.recyclerView.adapter = mAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: $viewModel")
        viewModel.photoSearchResult.observe(this as LifecycleOwner) {
            Log.d(TAG, "photoSearchResult: ")
            mAdapter.submitList(it.results)
        }
        viewModel.queryLiveData.observe(this as LifecycleOwner) {
            Log.d(TAG, "queryLiveData: $it")
            viewModel.searchPhotoList(it)
        }
    }

}