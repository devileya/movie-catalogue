package com.devileya.moviecatalogue.ui.main.favorite

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devileya.moviecatalogue.R

/**
 * Created by Arif Fadly Siregar 2019-11-08.
 */
class ViewPagerFavoriteAdapter(fragmentManager: FragmentManager, private val ctx: Context
): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var title: List<String> = listOf(ctx.resources.getString(R.string.movies), ctx.resources.getString(
        R.string.tv_shows))

    override fun getItem(position: Int): Fragment
    {
        return when (position) {
            0 -> FavoriteFragment.newInstance(ctx.resources.getString(R.string.movies))
            else -> FavoriteFragment.newInstance(ctx.resources.getString(R.string.tv_shows))
        }
    }

    override fun getCount(): Int {
        return title.size

    }
    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}