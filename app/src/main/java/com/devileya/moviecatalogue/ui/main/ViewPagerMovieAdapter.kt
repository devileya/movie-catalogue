package com.devileya.moviecatalogue.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devileya.moviecatalogue.R

class ViewPagerMovieAdapter(fragmentManager: FragmentManager,
                            private val ctx: Context): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var title: List<String> = listOf(ctx.resources.getString(R.string.movies), ctx.resources.getString(R.string.tv_shows))

    override fun getItem(position: Int): Fragment
    {
        return when (position) {
            0 -> MainFragment.newInstance(ctx.resources.getString(R.string.movies))
            else -> MainFragment.newInstance(ctx.resources.getString(R.string.tv_shows))
        }
    }

    override fun getCount(): Int {
        return title.size

    }
    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}