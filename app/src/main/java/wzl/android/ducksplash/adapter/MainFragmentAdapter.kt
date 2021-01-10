package wzl.android.ducksplash.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wzl.android.ducksplash.model.ListTabModel
import wzl.android.ducksplash.ui.PhotoListFragment

/**
 *Created on 1/10/21
 *@author zhilin
 */
class MainFragmentAdapter(var tabModel: List<ListTabModel>, fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabModel.size

    override fun createFragment(position: Int): Fragment =
        PhotoListFragment.newInstance(tabModel[position].photoListType)

}