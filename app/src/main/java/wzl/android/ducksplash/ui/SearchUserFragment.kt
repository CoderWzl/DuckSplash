package wzl.android.ducksplash.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wzl.android.ducksplash.R
import wzl.android.ducksplash.viewmodel.SearchUserViewModel

class SearchUserFragment : Fragment() {

    companion object {
        fun newInstance() = SearchUserFragment()
    }

    private lateinit var viewModel: SearchUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchUserViewModel::class.java)
        // TODO: Use the ViewModel
    }

}