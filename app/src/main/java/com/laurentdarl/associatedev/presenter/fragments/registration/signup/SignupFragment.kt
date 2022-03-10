package com.laurentdarl.associatedev.presenter.fragments.registration.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater)

        binding.btnSignup.setOnClickListener {
            Toast.makeText(requireContext(), "Signup completed", Toast.LENGTH_SHORT).show()
            loginScreen()
        }

        binding.tvSignin.setOnClickListener {
            loginScreen()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun loginScreen() {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.pager)
        viewPager?.currentItem = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}