package wzl.android.ducksplash.ui.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.adapter.AddCollectionAdapter
import wzl.android.ducksplash.adapter.AddState
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.databinding.BottomSheetAddCollectionBinding
import wzl.android.ducksplash.viewmodel.PhotoDetailViewModel
import javax.inject.Inject

/**
 *Created on 2021/3/17
 *@author zhilin
 */
@AndroidEntryPoint
class AddCollectionBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: PhotoDetailViewModel
    private lateinit var viewBinding: BottomSheetAddCollectionBinding
    @Inject lateinit var adapter: AddCollectionAdapter
    @Inject lateinit var tokenProvider: TokenProtoProvider
    private var photoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(ARGUMENT_PHOTO_ID)?.let {
            photoId = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("zhilin", "onCreateView: $viewModel")
        viewBinding = BottomSheetAddCollectionBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onItemClickListener = { collection, position ->
            Log.d("zhilin", "onViewCreated: $photoId")
            photoId?.let {
                if (viewModel.currentUserCollections.value?.contains(collection.id) == true) {
                    viewModel.removePhotoFromCollection(
                        collectionId = collection.id,
                        photoId = it,
                        position = position
                    ).observe(viewLifecycleOwner) { state ->
                        val collectionId = when (state) {
                            is AddState.Adding -> state.collectionId
                            is AddState.Added -> state.result.collection?.id
                            is AddState.NotAdd -> state.collectionId
                            is AddState.Removing -> state.collectionId
                            is AddState.Error -> state.collectionId
                        } ?: 0
                        adapter.changeItemAddState(collectionId, state)
                    }
                } else {
                    viewModel.addPhotoToCollection(
                        collectionId = collection.id,
                        it,
                        position
                    ).observe(viewLifecycleOwner) { state ->
                        val collectionId = when (state) {
                            is AddState.Adding -> state.collectionId
                            is AddState.Added -> state.result.collection?.id
                            is AddState.NotAdd -> state.collectionId
                            is AddState.Removing -> state.collectionId
                            is AddState.Error -> state.collectionId
                        } ?: 0
                        Log.d("zhilin", "onViewCreated: ${state.javaClass.simpleName}")
                        adapter.changeItemAddState(collectionId, state)
                    }
                }
            }
        }
        viewBinding.collectionList.adapter = adapter
        viewBinding.collectionList.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
        viewBinding.createIcon.setOnClickListener {
            viewBinding.addCollectionLayout.visibility = View.INVISIBLE
            viewBinding.createCollectionLayout.visibility = View.VISIBLE
        }
        viewBinding.cancelButton.setOnClickListener {
            viewBinding.addCollectionLayout.visibility = View.VISIBLE
            viewBinding.createCollectionLayout.visibility = View.INVISIBLE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: 2021/3/29 load more
        observer()
        viewModel.loadMoreUserCollections()
    }

    private fun observer() {
        // 监听登录用户图集列表
        viewModel.userCollections.observe(viewLifecycleOwner) { collections ->
            adapter.submitList(collections)
        }
        // 图片所在用户图集 id 列表
        viewModel.currentUserCollections.observe(viewLifecycleOwner) { currentUserCollections ->
            adapter.submitCurrentUserCollections(currentUserCollections)
        }
    }

    companion object {
        private const val ARGUMENT_PHOTO_ID = "argument_photo_id"

        fun newInstance(photoId: String, vm: PhotoDetailViewModel) = AddCollectionBottomSheet()
            .apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_PHOTO_ID, photoId)
                }
                viewModel = vm
            }
    }

}