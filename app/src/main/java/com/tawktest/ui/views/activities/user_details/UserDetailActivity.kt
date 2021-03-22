package com.tawktest.ui.views.activities.user_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.tawktest.R
import com.tawktest.app.factory.ViewModelFactory
import com.tawktest.app.utils.KeyboardUtils
import com.tawktest.app.utils.Params
import com.tawktest.data.Resource
import com.tawktest.data.models.User
import com.tawktest.databinding.ActivityUserDetailBinding
import com.tawktest.databinding.CustomActionBarBinding
import com.tawktest.ui.base.BaseActivity
import dagger.android.AndroidInjection
import javax.inject.Inject



class UserDetailActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: ActivityUserDetailBinding
    lateinit var actionBinding: CustomActionBarBinding
    lateinit var viewModel: UserDetailsViewModel
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
        setupActionBar()
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserDetailsViewModel::class.java)

        user = intent.extras?.getParcelable(Params.USER)
        user?.let {
            actionBinding.tvTitle.text = it.login
            binding.ivAvatar.load(it.avatarUrl)
            viewModel.fetchLocalUser(it.login)
            fetchFromServer(it)
        }

        initObservers()

        binding.content.btnSave.setOnClickListener {
            user?.let {
                it.notes = binding.content.etNotes.text.toString()
                viewModel.updateUser(it)
                binding.content.etNotes.setText("")
                KeyboardUtils.hideKeyboard(this, binding.content.etNotes)
                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Setting up observers to update ui on data changes
     * */
    private fun initObservers() {
        viewModel.getUser().observe(this) { res ->
            when (res.status) {
                Resource.Status.SUCCESS -> {
                    user = res.data as User
                    user?.let { it ->
                        val followers = "${getString(R.string.followers)} ${it.followers}"
                        binding.content.tvFollowers.text = followers

                        val following = "${getString(R.string.following)} ${it.following}"
                        binding.content.tvFollowing.text = following

                        if (!it.notes.isNullOrEmpty()) {
                            binding.content.flNotes.visibility = View.VISIBLE
                            binding.content.tvNotes.text = it.notes
                        } else {
                            binding.content.flNotes.visibility = View.GONE
                        }
                    }
                }
                Resource.Status.LOADING -> {
                    // ToDo: Handle loading
                }
                Resource.Status.ERROR -> {
                    // ToDo: Handle errors
                }
            }
        }
    }

    /**
     * Checking network connection and loading data from server
     * if device would be connected to network, it will fetch data from server
     * otherwise show network alert dialog
     *
     * @param user
     * */
    private fun fetchFromServer(user: User) {
        if (isNetworkConnected()) {
            viewModel.fetchUser(user)
        } else {
            networkAlert { fetchFromServer(user) }
        }
    }

    /**
     * Setting up action bar and custom views
     * */
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        actionBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.custom_action_bar,
                null,
                false
        )
        supportActionBar?.customView = actionBinding.root
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}