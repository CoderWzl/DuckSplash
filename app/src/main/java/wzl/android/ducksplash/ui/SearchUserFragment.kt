package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.adapter.UserListAdapter
import wzl.android.ducksplash.databinding.FragmentSearchUserBinding
import wzl.android.ducksplash.viewmodel.SearchViewModel

private const val TAG = "SearchUserFragment"
class SearchUserFragment : Fragment() {

    companion object {
        fun newInstance(vm: SearchViewModel) = SearchUserFragment().apply {
            viewModel = vm
        }
    }

    private lateinit var viewBinding: FragmentSearchUserBinding

    private lateinit var viewModel: SearchViewModel

    private val mAdapter = UserListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchUserBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.recyclerView.adapter = mAdapter
        viewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastPos =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    if (lastPos == mAdapter.itemCount - 1) {
                        viewModel.searchUserList()
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.queryLiveData.observe(viewLifecycleOwner) {
            viewModel.searchUserList(it)
        }
        viewModel.userSearchResult.observe(viewLifecycleOwner) {
            Log.d(TAG, "onActivityCreated: $it")
            mAdapter.submitList(it.results)
        }
    }

}