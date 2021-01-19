package wzl.android.ducksplash.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import wzl.android.ducksplash.adapter.PhotoListAdapter
import wzl.android.ducksplash.databinding.FragmentPhotoListBinding
import wzl.android.ducksplash.viewmodel.PhotoListViewModel

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
    private val mAdapter = PhotoListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = FragmentPhotoListBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = mAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoListViewModel::class.java)
        if (!viewModel.inited) {
            viewModel.photoList.observe(this as LifecycleOwner) {
                Log.d(TAG, "onActivityCreated: $it")
                mAdapter.submitList(it)
            }
            viewModel.loadPhotoList()
            viewModel.inited = true
        }
    }

}