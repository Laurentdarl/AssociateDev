package com.laurentdarl.associatedev.presenter.fragments.app.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.data.preferences.UserPreferences
import com.laurentdarl.associatedev.databinding.FragmentMainBinding
import com.laurentdarl.associatedev.domain.models.UserDetails
import com.laurentdarl.associatedev.presenter.fragments.app.home.MainFragment.SharedPref.LOGGED_IN
import com.laurentdarl.associatedev.presenter.fragments.registration.signin.SigninFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
//    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dataStore: UserPreferences


    object SharedPref {
        const val PREF_NAME = "SharedPref"
        const val EMAIL = "Email"
        const val PASSWORD = "Password"
        const val REMEMBER_ME = true
        const val LOGGED_IN = "onLoggedIn"
        const val SAVED_USER_DETAILS = "SavedUserDetails"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        dataStore = UserPreferences(requireContext())

//        sharedPreferences = requireActivity().getSharedPreferences(
//            LOGGED_IN,
//            MODE_PRIVATE
//        )
        getDataFromDataStore()
//
//        val email = sharedPreferences.getString(SigninFragment.SharedPref.EMAIL, null)
//        val password = sharedPreferences.getString(SigninFragment.SharedPref.PASSWORD, null)
//        if (email != null && password != null) {
//            binding.tvEmail.text = email
//            binding.tvPassword.text = password
//            Toast.makeText(requireContext(), "Some Data Saved", Toast.LENGTH_SHORT).show()
//        }

        binding.btnMain.setOnClickListener {

            findNavController().navigate(MainFragmentDirections.actionMainFragmentToWelcomeFragment())
//            val editor = sharedPreferences.edit()
//            editor.clear()
//            editor.apply()
//            getDataFromDataStore()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getDataFromDataStore() {
        GlobalScope.launch {
        val userDataflow: Flow<UserDetails> = dataStore.getFromDataStore()
        val userData = userDataflow.first()
            GlobalScope.launch(Dispatchers.Main) {
                binding.tvEmail.text = userData.email
                binding.tvPassword.text = userData.password
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}