package com.ersinberkealemdaroglu.artbookkotlinviewpager2.onboarding.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.MainActivity
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.R
import kotlinx.android.synthetic.main.fragment_third_screen.view.*


class FinishedScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third_screen,container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.finished.setOnClickListener {
            requireActivity().run {
                onBoardingFinished()
                startActivity(Intent(context,MainActivity::class.java))
                finish()
            }
        }
    }

    private fun onBoardingFinished(){
        //fragment da shared Pref..
        val preferences = this.requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("Finished", true)
        editor.apply()

    }


}