package wzl.android.ducksplash.ui.add

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import wzl.android.ducksplash.viewmodel.MainSharedViewModel
import wzl.android.ducksplash.viewmodel.PhotoDetailViewModel

/**
 *Created on 2021/3/17
 *@author zhilin
 */
class AddCollectionBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: MainSharedViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("zhilin", "onCreateView: $viewModel")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}