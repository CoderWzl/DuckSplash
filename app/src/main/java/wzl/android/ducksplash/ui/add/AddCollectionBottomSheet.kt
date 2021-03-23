package wzl.android.ducksplash.ui.add

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import wzl.android.ducksplash.adapter.AddCollectionPagingAdapter
import wzl.android.ducksplash.adapter.AddState
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.databinding.BottomSheetAddCollectionBinding
import wzl.android.ducksplash.viewmodel.MainSharedViewModel
import javax.inject.Inject

/**
 *Created on 2021/3/17
 *@author zhilin
 */
@AndroidEntryPoint
class AddCollectionBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: MainSharedViewModel by activityViewModels()
    private lateinit var viewBinding: BottomSheetAddCollectionBinding
    @Inject lateinit var adapter: AddCollectionPagingAdapter
    @Inject lateinit var tokenProvider: TokenProtoProvider
    private var photoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString(ARGUMENT_PHOTO_ID)?.let {
            photoId = it
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
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
        adapter.onItemClickListener = { collection ->
            Log.d("zhilin", "onViewCreated: $photoId")
            photoId?.let {
                viewModel.addPhotoToCollection(
                    collectionId = collection.id,
                    it).observe(viewLifecycleOwner) { state ->
                    val collectionId = when(state) {
                        is AddState.Adding -> state.collectionId
                        is AddState.Added -> state.result.collection?.id
                        is AddState.NotAdd -> state.collectionId
                        is AddState.Removing -> state.collectionId
                    }?:0
                    adapter.changeItemAddState(collectionId, state)
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
        lifecycleScope.launch {
            val userName = tokenProvider.loginPreferences.firstOrNull()?.userName
            viewModel.getUserCollections(userName).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        private const val ARGUMENT_PHOTO_ID = "argument_photo_id"

        fun newInstance(photoId: String) = AddCollectionBottomSheet()
            .apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_PHOTO_ID, photoId)
                }
            }
    }

}