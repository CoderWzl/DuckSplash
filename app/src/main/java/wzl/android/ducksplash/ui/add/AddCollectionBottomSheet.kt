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

}