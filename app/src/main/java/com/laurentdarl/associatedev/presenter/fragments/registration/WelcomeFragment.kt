package com.laurentdarl.associatedev.presenter.fragments.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.data.adapters.ViewPagerAdapter
import com.laurentdarl.associatedev.databinding.FragmentWelcomeBinding
import com.laurentdarl.associatedev.presenter.fragments.registration.signin.SigninFragment
import com.laurentdarl.associatedev.presenter.fragments.registration.signin.SigninFragment.SharedPref.LOGGED_IN
import com.laurentdarl.associatedev.presenter.fragments.registration.signup.SignupFragment

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private lateinit var pagerAdapter: ViewPagerAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater)

        val fragmentList = arrayListOf(
            SigninFragment(),
            SignupFragment()
        )
        pagerAdapter = ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        binding.pager.adapter = pagerAdapter

//        if (isUserLoggedIn()) {
//            val viewPager = activity?.findViewById<ViewPager2>(R.id.pager)
//            viewPager?.currentItem = 0
//        }else {
//            navigateToHome()
//        }

        TabLayoutMediator(binding.tabLayout, binding.pager) {tab,position ->

            when(position) {
                0 -> {
                    tab.text = "Signin"
                }
                1 -> {
                    tab.text = "Signup"
                }
            }
        }.attach()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun navigateToHome() {
        val actions = WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment()
        findNavController().navigate(actions)
    }

    private fun isUserLoggedIn(): Boolean {
        val sharePref = requireActivity().getSharedPreferences(LOGGED_IN, Context.MODE_PRIVATE)
        return sharePref.getBoolean("Finished", false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}