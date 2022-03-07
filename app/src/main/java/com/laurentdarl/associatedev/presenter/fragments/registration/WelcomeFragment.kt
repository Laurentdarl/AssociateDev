package com.laurentdarl.associatedev.presenter.fragments.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.laurentdarl.associatedev.data.adapters.ViewPagerAdapter
import com.laurentdarl.associatedev.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private lateinit var pagerAdapter: ViewPagerAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater)

        pagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.pager.adapter = pagerAdapter


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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}