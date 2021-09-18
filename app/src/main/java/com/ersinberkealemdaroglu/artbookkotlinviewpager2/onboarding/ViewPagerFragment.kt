package com.ersinberkealemdaroglu.artbookkotlinviewpager2.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.R
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.onboarding.screens.FirstScreen
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.onboarding.screens.ThirdScreen
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.onboarding.screens.SecondScreen
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.onboarding.screens.FinishedScreen
import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_view_pager, container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen(),
            FinishedScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.viewPager.adapter = adapter
        //Indicator
        val indicator = indicator
        indicator.setViewPager(viewPager)

    }
}