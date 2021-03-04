package com.tawktest.ui.views.activities.user_list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tawktest.R
import com.tawktest.app.factory.ViewModelFactory
import com.tawktest.app.utils.Params
import com.tawktest.app.utils.Units
import com.tawktest.app.utils.decoration.EqualSpacingItemDecoration
import com.tawktest.app.utils.decoration.EqualSpacingItemDecoration.Companion.VERTICAL
import com.tawktest.app.utils.pagination.EndlessRecyclerViewScrollListener
import com.tawktest.data.Resource
import com.tawktest.data.models.User
import com.tawktest.databinding.ActivityListBinding
import com.tawktest.ui.base.BaseActivity
import com.tawktest.ui.views.activities.user_details.UserDetailActivity
import dagger.android.AndroidInjection
import javax.inject.Inject


class ListActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityListBinding
    private lateinit var viewModel: ListViewModel
    private var users = mutableListOf<User>()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var userAdapter: UserAdapter
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        Looper.myLooper()?.let {
            handler = Handler(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        binding.rvUsers.addItemDecoration(
                EqualSpacingItemDecoration(
                        Units.dpToPx(
                                16.toFloat(),
                                this
                        ), VERTICAL
                )
        )
        userAdapter = UserAdapter(users) {
            val bundle = Bundle()
            bundle.putParcelable(Params.USER, it)
            navigateTo(UserDetailActivity::class.java, bundle = bundle)
        }
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (users.size > 0) {
                    fetchFromServer(users[users.size - 1].id)
                }
            }
        }
        binding.rvUsers.addOnScrollListener(scrollListener)
        binding.rvUsers.adapter = userAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                handler?.removeCallbacksAndMessages(null)
                handler?.postDelayed({
                    newText?.let {
                        userAdapter.filter.filter(newText)
                    }
                }, 400)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                handler?.removeCallbacksAndMessages(null)
                handler?.postDelayed({
                    query?.let {
                        userAdapter.filter.filter(query)
                    }
                }, 400)
                return true
            }
        })

        initObservers()
        binding.shimmerFrameLayout.startShimmerAnimation()
        viewModel.fetchLocalUsers()
        fetchFromServer(0)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchLocalUsers()
    }

    /**
     * Checking network connection and loading data from server
     * if device would be connected to network, it will fetch data from server
     * otherwise show network alert dialog
     *
     * @param id last loaded user id
     * */
    private fun fetchFromServer(id: Int) {
        if (isNetworkConnected()) {
            viewModel.fetchUsers(id, scrollListener.getPageCapacity(15))
        } else {
            networkAlert { fetchFromServer(id) }
        }
    }

    /**
     * Setting up observers to update ui on data changes
     * */
    private fun initObservers() {
        viewModel.getUsers().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.shimmerFrameLayout.stopShimmerAnimation()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.rvUsers.visibility = View.VISIBLE
                    val response = it.data as List<User>
                    users.clear()
                    users.addAll(response)
                    scrollListener.resetState()
                    userAdapter.notifyDataSetChanged()
                }
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}
