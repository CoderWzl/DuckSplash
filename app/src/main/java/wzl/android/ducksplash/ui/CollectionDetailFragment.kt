package wzl.android.ducksplash.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.navArgs
import wzl.android.ducksplash.adapter.PhotoListAdapter
import wzl.android.ducksplash.databinding.FragmentCollectionDetailBinding
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.CollectionDetailViewModel

class CollectionDetailFragment : Fragment() {

    private lateinit var viewModel: CollectionDetailViewModel

    private val viewBinding: FragmentCollectionDetailBinding by lazy {
        FragmentCollectionDetailBinding.inflate(layoutInflater)
    }

    private val args: CollectionDetailFragmentArgs by navArgs()
    private val mAdapter = PhotoListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            coordinatorLayout.reserveStatusBar()
            recyclerView.adapter = mAdapter
            toolBar.title = args.title
            //collapsingLayout.title = args.title
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CollectionDetailViewModel::class.java)
        viewModel.loadPhotoListWithCollectionId(args.collectionId)
        viewModel.photoList.observe(this as LifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

}