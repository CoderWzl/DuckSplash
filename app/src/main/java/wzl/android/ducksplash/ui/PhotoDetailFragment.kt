package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.IMAGE_LARGE_SUFFIX
import wzl.android.ducksplash.IMAGE_THUMB_SUFFIX
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.TagListAdapter
import wzl.android.ducksplash.databinding.FragmentPhotoDetailBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import wzl.android.ducksplash.util.reserveStatusBar
import wzl.android.ducksplash.viewmodel.NavMainViewModel
import wzl.android.ducksplash.viewmodel.PhotoDetailViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {

    private val args by navArgs<PhotoDetailFragmentArgs>()
    private lateinit var viewBinding: FragmentPhotoDetailBinding
    private lateinit var photoId: String
    private val viewModel by viewModels<PhotoDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.photo == null && args.photoId == null) {
            findNavController().popBackStack()
        }
        photoId = args.photoId ?: args.photo?.id!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentPhotoDetailBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.toolBar.reserveStatusBar()
        viewBinding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupPhoto(args.photo)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observer()
        viewModel.getPhoto(photoId)
    }

    private fun observer() {
        viewModel.photo.observe(viewLifecycleOwner) {
            setupDetail(it)
            viewBinding.loadingContainer.isVisible = false
            viewBinding.detailContainer.isVisible = true
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            viewBinding.loadingContainer.isVisible = it
            viewBinding.detailContainer.isVisible = !it
            viewBinding.progressBar.isVisible = it
            viewBinding.errorTip.isVisible = false
        }
        viewModel.error.observe(viewLifecycleOwner) {
            viewBinding.loadingContainer.isVisible = true
            viewBinding.detailContainer.isVisible = false
            viewBinding.progressBar.isVisible = false
            viewBinding.errorTip.isVisible = true
            viewBinding.errorTip.text = it
        }
    }

    private fun setupPhoto(photo: PhotoModel?) {
        photo?.let {
            val imageUrl = it.urls.raw + IMAGE_LARGE_SUFFIX
            val thumbUrl = it.urls.raw + IMAGE_THUMB_SUFFIX
            viewBinding.imageView.loadPhotoUrlWithThumbnail(imageUrl, thumbUrl, it.color)
        }
    }

    private fun setupDetail(photo: PhotoModel?) {
        val vm by navGraphViewModels<NavMainViewModel>(R.id.nav_main)
        vm.sendPhotoIso(photo?.exif?.iso.toString())
        photo?.let {
            viewBinding.apply {
                manufacturer.text = it.exif?.make
                model.text = it.exif?.model
                focalLength.text = it.exif?.focalLength
                exposure.text = it.exif?.iso.toString()
                size.text = "${photo.width} x ${photo.height}"
                color.text = photo.color
                shutterTime.text = photo.exif?.exposureTime
                aperture.text = photo.exif?.aperture
                download.text = photo.downloads.toString()
                favorite.text = photo.likes.toString()
                view.text = photo.views.toString()
                val headUrl = photo.user.profileImage.large
                userHead.loadCirclePhotoUrl(headUrl)
                userName.text = "${photo.user.firstName} ${photo.user.lastName?:""}"
                tagList.layoutManager = LinearLayoutManager(
                    requireContext(),
                    RecyclerView.HORIZONTAL,
                    false
                )
                tagList.adapter = TagListAdapter().also {
                    it.submitList(photo.tags)
                }
            }
        }
    }

}