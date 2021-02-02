package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wzl.android.ducksplash.NavMainDirections
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.FooterLoadStateAdapter
import wzl.android.ducksplash.adapter.PhotoDiffCallback
import wzl.android.ducksplash.adapter.PhotoPagingAdapter
import wzl.android.ducksplash.databinding.FragmentCollectionDetailBinding
import wzl.android.ducksplash.util.navigateSafe
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.CollectionDetailViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CollectionDetailFragment : Fragment() {

    private val viewModel by viewModels<CollectionDetailViewModel>()

    private lateinit var viewBinding: FragmentCollectionDetailBinding

    private val args: CollectionDetailFragmentArgs by navArgs()
    @Inject lateinit var mAdapter: PhotoPagingAdapter
    private var expanded = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCollectionDetailBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.onPhotoClickListener = {
            findNavController().navigateSafe(NavMainDirections.actionGlobalToPhotoDetailFragment(it))
        }
        mAdapter.onUserClickListener = {
            findNavController().navigateSafe(
                NavMainDirections.actionGlobalUserFragment(it)
            )
        }
        with(viewBinding) {
            root.reserveStatusBar()
            recyclerView.adapter = mAdapter.withLoadStateFooter(
                    footer = FooterLoadStateAdapter {
                        mAdapter.retry()
                    }
            )
            toolBar.title = args.title
            toolBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolBar.post {
                moreInfoContainer.apply {
                    setPadding(
                            paddingLeft,
                            toolBar.height + paddingTop,
                            paddingRight,
                            paddingBottom
                    )
                }
            }
            description.text = args.description
            otherDetails.text = requireContext().getString(R.string.photo_summary_user_info, args.totalPhotos, args.fullName)

        }
        viewBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            expanded = verticalOffset == 0
        })
        viewBinding.appBarLayout.setExpanded(expanded)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.getCollectionPhoto(args.collectionId).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}