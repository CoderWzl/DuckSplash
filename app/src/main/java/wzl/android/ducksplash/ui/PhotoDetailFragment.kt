package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import wzl.android.ducksplash.IMAGE_LARGE_SUFFIX
import wzl.android.ducksplash.IMAGE_THUMB_SUFFIX
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.FragmentPhotoDetailBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import wzl.android.ducksplash.util.reserveStatusBar

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

    private fun setupPhoto(photo: PhotoModel?) {
        photo?.let {
            val imageUrl = it.urls.raw + IMAGE_LARGE_SUFFIX
            val thumbUrl = it.urls.raw + IMAGE_THUMB_SUFFIX
            viewBinding.imageView.loadPhotoUrlWithThumbnail(imageUrl, thumbUrl, it.color)
        }
    }

}