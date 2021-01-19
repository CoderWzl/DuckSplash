package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wzl.android.ducksplash.adapter.CollectionListAdapter
import wzl.android.ducksplash.databinding.FragmentSearchCollectionBinding
import wzl.android.ducksplash.viewmodel.SearchViewModel

private const val TAG = "SearchCollectionFragmen"
class SearchCollectionFragment : Fragment() {

    companion object {
        fun newInstance(vm: SearchViewModel) = SearchCollectionFragment().apply {
            viewModel = vm
        }
    }

    private lateinit var viewModel: SearchViewModel

    private lateinit var viewBinding: FragmentSearchCollectionBinding

    private lateinit var mAdapter: CollectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchCollectionBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = CollectionListAdapter(this)
        viewBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: $viewModel")
        viewModel.queryLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "onActivityCreated: queryLiveData $it")
            viewModel.searchCollectionList(it)
        }
        viewModel.collectionSearchResult.observe(viewLifecycleOwner) {
            Log.d(TAG, "onActivityCreated: collectionSearchResult $it")
            mAdapter.submitList(it.results)
        }
    }

}