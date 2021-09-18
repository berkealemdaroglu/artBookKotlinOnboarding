package com.ersinberkealemdaroglu.artbookkotlinviewpager2.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.R
import kotlinx.android.synthetic.main.fragment_first_screen.view.*
import kotlinx.android.synthetic.main.fragment_first_screen.view.nextScreenID1
import kotlinx.android.synthetic.main.fragment_fours_screen.view.*

class FoursScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fours_screen, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        view.nextScreenID3.setOnClickListener() {
            viewPager?.currentItem = 1
        }
    }
}