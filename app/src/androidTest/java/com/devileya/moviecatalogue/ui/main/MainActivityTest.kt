package com.devileya.moviecatalogue.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.utils.EspressoIdlingResource
import junit.framework.AssertionFailedError
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Arif Fadly Siregar 2019-11-06.
 */
class MainActivityTest{
    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoIdlingResource)
    }

    @Test
    fun movieListTest() {
        try {
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).check(matches(isDisplayed()))
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).perform(swipeUp())
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).perform(swipeDown())
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            onView(allOf(withId(R.id.rv_movie), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, click()))
        } catch (e: AssertionFailedError){
            onView(allOf(isDisplayed(), withId(R.id.tv_no_data))).check(matches(withText("No Data")))
        }
    }

    @Test
    fun tvShowListTest() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        onView(allOf(withText(ctx.getString(R.string.tv_shows)))).perform(click())
        try {
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).check(matches(isDisplayed()))
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).perform(swipeUp())
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).perform(swipeDown())
            onView(allOf(isDisplayed(), withId(R.id.rv_movie))).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            onView(allOf(withId(R.id.rv_movie), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, click()))
        } catch (e: AssertionFailedError){
            onView(allOf(isDisplayed(), withId(R.id.tv_no_data))).check(matches(withText("No Data")))
        }
    }
}