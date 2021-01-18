package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wzl.android.ducksplash.adapter.UserListAdapter
import wzl.android.ducksplash.databinding.FragmentSearchUserBinding
import wzl.android.ducksplash.viewmodel.SearchViewModel

class SearchUserFragment : Fragment() {

    companion object {
        fun newInstance(vm: SearchViewModel) = SearchUserFragment().apply {
            viewModel = vm
        }
    }

    private val viewBinding: FragmentSearchUserBinding by lazy {
        FragmentSearchUserBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: SearchViewModel

    private val mAdapter = UserListAdapter()

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
        viewModel.queryLiveData.observe(viewLifecycleOwner) {
            viewModel.searchUserList(it)
        }
        viewModel.userSearchResult.observe(viewLifecycleOwner) {
            mAdapter.submitList(it.results)
        }
    }

}