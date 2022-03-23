package com.laurentdarl.associatedev.presenter.fragments.registration.signin

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.data.models.UserDetails
import com.laurentdarl.associatedev.data.persistence.preferences.UserPreferences
import com.laurentdarl.associatedev.databinding.FragmentSigninBinding
import com.laurentdarl.associatedev.presenter.fragments.registration.WelcomeFragmentDirections
import com.laurentdarl.associatedev.presenter.fragments.registration.signin.SigninFragment.SharedPref.EMAIL
import com.laurentdarl.associatedev.presenter.fragments.registration.signin.SigninFragment.SharedPref.LOGGED_IN
import com.laurentdarl.associatedev.presenter.fragments.registration.signin.SigninFragment.SharedPref.PASSWORD
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SigninFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dataStore: UserPreferences

    object SharedPref {
//        const val PREF_NAME = "SharedPref"
//        const val REMEMBER_ME = true
        const val EMAIL = "Email"
        const val PASSWORD = "Password"
        const val LOGGED_IN = "onLoggedIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninBinding.inflate(layoutInflater)
        sharedPreferences = requireActivity().getSharedPreferences(LOGGED_IN, MODE_PRIVATE)
        dataStore = UserPreferences(requireContext())

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim {it <= ' '}
            val password = binding.etLoginPassword.text.toString().trim {it <= ' '}

            loginToApp(email, password)
            userLoggedIn()
//            rememberUserLoginDetails()
            saveDataWithDataStore("", email, password)
        }

        binding.tvSignup.setOnClickListener {
            signupScreen()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signupScreen() {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.pager)
        viewPager?.currentItem = 1
    }

    private fun loginToApp(email: String, password: String) {
        verifyUser(email, password)
    }

    private fun verifyUser(email: String, password: String) {
        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(requireContext(), "Please insert a valid email address", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(requireContext(), "Please insert a valid email address", Toast.LENGTH_SHORT).show()
            }
            else -> {
                navigateToHome()
            }
        }

    }

    private fun navigateToHome() {
        val actions = WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment()
        findNavController().navigate(actions)
    }

    private fun userLoggedIn() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    private fun rememberUserLoginDetails() {
        val email = binding.etLoginEmail.text.toString()
        val password = binding.etLoginPassword.text.toString().trim {it <= ' '}
        val checked: Boolean = binding.checkboxRemember.isChecked

        val editor = sharedPreferences.edit()
        editor.putString(EMAIL, email)
        editor.putString(PASSWORD, password)
        editor.putBoolean("isChecked", checked)
        editor.apply()
    }

    private fun saveDataWithDataStore(name: String, email: String, password: String) {
        GlobalScope.launch {
            val user = UserDetails(
                name,
                email,
                password
            )
            dataStore.saveData(user)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}