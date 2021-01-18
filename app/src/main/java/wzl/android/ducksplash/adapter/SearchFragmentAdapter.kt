package wzl.android.ducksplash.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wzl.android.ducksplash.SearchType
import wzl.android.ducksplash.ui.SearchCollectionFragment
import wzl.android.ducksplash.ui.SearchPhotoFragment
import wzl.android.ducksplash.ui.SearchUserFragment
import wzl.android.ducksplash.viewmodel.SearchViewModel

/**
 *Created on 2021/1/18
 *@author zhilin
 */
class SearchFragmentAdapter(fragment: Fragment, private val viewModel: SearchViewModel) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = SearchType.values().size

    override fun createFragment(position: Int): Fragment {
        return when(getItemType(position)) {
            SearchType.PHOTO -> SearchPhotoFragment.newInstance(viewModel)
            SearchType.COLLECTION -> SearchCollectionFragment.newInstance(viewModel)
            SearchType.USER -> SearchUserFragment.newInstance(viewModel)
        }
    }

    private fun getItemType(position: Int): SearchType {
        return SearchType.values()[position]
    }
}