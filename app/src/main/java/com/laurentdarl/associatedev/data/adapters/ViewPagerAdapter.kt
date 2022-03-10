package com.laurentdarl.associatedev.data.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.laurentdarl.associatedev.presenter.fragments.registration.signin.SigninFragment
import com.laurentdarl.associatedev.presenter.fragments.registration.signup.SignupFragment

class ViewPagerAdapter (list: ArrayList<Fragment>, fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
//        return when(position) {
//            0 -> SigninFragment()
//            1 -> SignupFragment()
//            else -> SigninFragment()
//        }
    }
}