package wzl.android.ducksplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wzl.android.ducksplash.R

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }
}