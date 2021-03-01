package com.example.locker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var lastDestination = 0

    companion object{
        var token : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        initBottomNavigation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setupNavigation()
//        initView()


    }

//
//    private fun initView(){
//        bnv_navigation.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.menu_home -> {
//                    findNavController(R.id.container).navigate(R.id.global_home)
//                    return@setOnNavigationItemSelectedListener true
//                }
//                R.id.menu_acount ->{
//                    findNavController(R.id.container).navigate(R.id.global_account)
//                    return@setOnNavigationItemSelectedListener true
//                }
//                else ->{
//                    return@setOnNavigationItemSelectedListener false
//                }
//            }
//        }
//    }



//    fun setupNavigation(
//        resetHome: Boolean = false,
//        startDestination: Int = 0
//    ) {
//        val navHostFragment = container as NavHostFragment
//        navController = navHostFragment.navController
//
//
//        val topLevelDestinations = HashSet<Int>()
//        topLevelDestinations.add(R.id.homeFragment)
//        topLevelDestinations.add(R.id.assessmentFragment)
//        topLevelDestinations.add(R.id.accountFragment)
//    }


}
