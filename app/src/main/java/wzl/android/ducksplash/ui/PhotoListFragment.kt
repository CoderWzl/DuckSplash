package wzl.android.ducksplash.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import wzl.android.ducksplash.PhotoListType
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.PhotoListAdapter
import wzl.android.ducksplash.databinding.PhotoListFragmentBinding
import wzl.android.ducksplash.viewmodel.PhotoListViewModel
import java.security.acl.Owner

private const val TAG = "PhotoListFragment"
private const val PARAM_PHOTO_LIST_TYPE = "photo_list_type"

/**
 * zhilin
 * 图片列表界面
 */
class PhotoListFragment : Fragment() {

    companion object {
        fun newInstance(listType: PhotoListType) = PhotoListFragment().apply {
            Log.d(TAG, "newInstance: ${listType.name}")
            arguments = Bundle().apply {
                putString(PARAM_PHOTO_LIST_TYPE, listType.name)
            }
        }
    }

    private val photoListType: PhotoListType by lazy {
        arguments?.let {
            val listType = it.getString(PARAM_PHOTO_LIST_TYPE)
            return@lazy if (listType == null) {
                PhotoListType.PHOTO_LIST_NEW
            } else {
                PhotoListType.valueOf(listType)
            }
        }
        PhotoListType.PHOTO_LIST_NEW
    }

    private val viewBinding: PhotoListFragmentBinding by lazy {
        PhotoListFragmentBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: PhotoListViewModel
    private val mAdapter = PhotoListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: ")
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: ")
        viewModel = ViewModelProvider(this).get(PhotoListViewModel::class.java)
        if (!viewModel.inited) {
            viewModel.photoList.observe(this as LifecycleOwner) {
                Log.d(TAG, "onViewCreated: $it")
                mAdapter.submitList(it)
            }
            viewModel.loadPhotoList()
            viewModel.inited = true
        }
    }

}