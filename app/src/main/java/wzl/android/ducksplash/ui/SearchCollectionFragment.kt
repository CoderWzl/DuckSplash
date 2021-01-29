package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.adapter.CollectionDiffCallback
import wzl.android.ducksplash.adapter.CollectionPagingAdapter
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.databinding.FragmentSearchCollectionBinding
import wzl.android.ducksplash.util.navigateSafe
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

    private val mAdapter = CollectionPagingAdapter(CollectionDiffCallback()) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchCollectionBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.recyclerView.adapter = mAdapter.withLoadStateFooter(
                footer = FooterLoadStateAdapter {
                    mAdapter.retry()
                }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: $viewModel")
        viewModel.queryLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "onActivityCreated: queryLiveData $it")
            if (it.isNotBlank()) {
                viewModel.searchCollections(it).observe(viewLifecycleOwner) {
                    lifecycleScope.launch {
                        mAdapter.submitData(it)
                    }
                }
            }
        }
    }

}