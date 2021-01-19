package wzl.android.ducksplash.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import wzl.android.ducksplash.adapter.CollectionListAdapter
import wzl.android.ducksplash.databinding.FragmentCollectionListBinding
import wzl.android.ducksplash.viewmodel.CollectionListViewModel

class CollectionListFragment : Fragment() {

    companion object {
        fun newInstance() = CollectionListFragment()
    }

    private lateinit var viewModel: CollectionListViewModel
    private lateinit var viewBinding: FragmentCollectionListBinding

    private val mAdapter: CollectionListAdapter by lazy {
        CollectionListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCollectionListBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CollectionListViewModel::class.java)
        viewModel.apply {
            if (!inited) {
                collectionList.observe(this@CollectionListFragment as LifecycleOwner) {
                    mAdapter.submitList(it)
                }
                loadCollectionList()
                inited = true
            }
        }
    }

}