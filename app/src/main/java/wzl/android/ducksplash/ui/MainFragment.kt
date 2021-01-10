package wzl.android.ducksplash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import wzl.android.ducksplash.MainActivity
import wzl.android.ducksplash.PhotoListType
import wzl.android.ducksplash.R
import wzl.android.ducksplash.adapter.MainFragmentAdapter
import wzl.android.ducksplash.databinding.FragmentMainBinding
import wzl.android.ducksplash.model.ListTabModel
import wzl.android.ducksplash.util.reserveStatusBar

private const val ARG_EXTRA = "extra"
private const val TAG = "MainFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    private var extra: String? = null

    private val viewBinding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    private val tabModelList by lazy {
        listOf(
            ListTabModel(
                PhotoListType.PHOTO_LIST_NEW,
                "最新"
            ),
            ListTabModel(
                PhotoListType.PHOTO_LIST_COLLECTIONS,
                "图集"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            extra = it.getString(ARG_EXTRA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        viewBinding.let {
            it.mainContainer.reserveStatusBar()
            it.toolBar.setNavigationOnClickListener {
                (requireActivity() as MainActivity).showMenu()
            }
            it.searchButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
            }
            it.viewPager.adapter = MainFragmentAdapter(tabModelList, this)
            TabLayoutMediator(it.tabLayout, it.viewPager) { tab, position ->
                tab.text = tabModelList[position].title
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        viewBinding.viewPager.adapter = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param extra Parameter 1.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance(extra: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXTRA, extra)
                }
            }
    }
}