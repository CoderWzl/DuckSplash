package wzl.android.ducksplash.ui.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.AddCollectionAdapter
import wzl.android.ducksplash.adapter.AddState
import wzl.android.ducksplash.api.ApiState
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.databinding.BottomSheetAddCollectionBinding
import wzl.android.ducksplash.util.toast
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
            showCreateLayout()
        }
        viewBinding.cancelButton.setOnClickListener {
            showAddLayout()
            resetInput()
        }
        viewBinding.createButton.setOnClickListener {
            if (isInputValid()) {
                photoId?.let { photoId ->
                    viewModel.createCollection(
                        name = viewBinding.collectionNameTextInputLayout.editText?.text.toString(),
                        description = viewBinding.descriptionTextInputLayout.editText?.text.toString(),
                        isPrivate = viewBinding.collectionPrivateCheckBox.isChecked,
                        photoId = photoId
                    ).observe(viewLifecycleOwner) { state ->
                        when(state) {
                            is ApiState.Loading -> {
                                //no op
                            }
                            is ApiState.Success -> {
                                viewBinding.collectionList.scrollToPosition(0)
                                showAddLayout()
                                resetInput()
                            }
                            is ApiState.Error -> {
                                requireContext().toast(
                                    message = getString(R.string.something_went_wrong)
                                )
                            }
                        }
                    }
                }
            } else {
                showErrorMessage()
            }
        }
    }

    private fun resetInput() {
        viewBinding.apply {
            collectionNameTextInputLayout.editText?.setText("")
            collectionNameTextInputLayout.error = null
            descriptionTextInputLayout.editText?.setText("")
            collectionPrivateCheckBox.isChecked = false
        }
    }

    private fun showCreateLayout() {
        viewBinding.addCollectionLayout.visibility = View.INVISIBLE
        viewBinding.createCollectionLayout.visibility = View.VISIBLE
    }

    private fun showAddLayout() {
        viewBinding.addCollectionLayout.visibility = View.VISIBLE
        viewBinding.createCollectionLayout.visibility = View.INVISIBLE
    }

    private fun showErrorMessage() {
        if (viewBinding.collectionNameTextInputLayout.editText?.text.toString().isBlank()) {
            viewBinding.collectionNameTextInputLayout.error =
                getString(R.string.name_is_required)
            viewBinding.collectionNameTextInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
                if (viewBinding.collectionNameTextInputLayout.error.toString().isNotBlank() &&
                        text?.isBlank() != true) {
                    viewBinding.collectionNameTextInputLayout.error = null
                }
            }
        }
    }

    private fun isInputValid(): Boolean {
        val name = viewBinding.collectionNameTextInputLayout.editText?.text.toString()
        val description = viewBinding.descriptionTextInputLayout.editText?.text.toString()
        return  name.isNotBlank() && name.length <= 60 && description.length <= 250
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