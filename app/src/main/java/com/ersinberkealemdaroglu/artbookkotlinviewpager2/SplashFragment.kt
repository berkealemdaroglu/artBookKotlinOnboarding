package com.ersinberkealemdaroglu.artbookkotlinviewpager2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed({

            if (onBoardingFinished()){
                requireActivity().run{
                    startActivity(Intent(context,MainActivity::class.java))
                    finish()
                }

            }else{
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }


        }, 3000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun onBoardingFinished() : Boolean{
        //fragment shared Pref..
        val preferences = this.requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return preferences.getBoolean("Finished", false)

    }

}