package com.example.locker.features

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController

import com.example.locker.R
import kotlinx.android.synthetic.main.fragment_base.*

class BaseFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)

        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

    }
    private fun initView() {
        bnv_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> {
                    findNavController(requireActivity(),R.id.container2).popBackStack()
                    findNavController(requireActivity(),R.id.container2).navigate(R.id.homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_acount ->{
                    findNavController(requireActivity(),R.id.container2).popBackStack()
                    findNavController(requireActivity(),R.id.container2).navigate(R.id.accountFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else ->{
                    findNavController(requireActivity(),R.id.container2).popBackStack()
                    findNavController(requireActivity(),R.id.container2).navigate(R.id.global_home)
                    return@setOnNavigationItemSelectedListener false
                }
            }

        }
    }


}
