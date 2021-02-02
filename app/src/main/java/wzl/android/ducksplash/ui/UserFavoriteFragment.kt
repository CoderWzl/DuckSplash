package wzl.android.ducksplash.ui

import android.os.Bundle
import android.view.View

/**
 *Created on 2/1/21
 *@author zhilin
 */
class UserFavoriteFragment: UserContentListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.testText.text = "user favorite"
    }

}