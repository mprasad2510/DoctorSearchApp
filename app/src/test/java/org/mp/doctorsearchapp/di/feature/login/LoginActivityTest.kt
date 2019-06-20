package org.mp.doctorsearchapp.di.feature.login

import android.widget.Button
import org.junit.Test
import org.junit.runner.RunWith
import org.mp.doctorsearchapp.BuildConfig
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import android.content.Intent
import junit.framework.Assert.assertEquals
import org.mp.doctorsearchapp.di.feature.search.HomeActivity
import org.robolectric.Shadows.shadowOf


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, maxSdk = 28,manifest = "src/main/AndroidManifest.xml",
packageName = "org.mp.doctorsearchapp",minSdk = 19)
class LoginActivityTest
{

    @Test
    fun itShouldAllowToLogin()
    {
        val activity: LoginActivity = Robolectric.setupActivity(LoginActivity::class.java)
        val loginButton : Button = activity.findViewById(org.mp.doctorsearchapp.R.id.login)
        loginButton.performClick()
        val expectedIntent = Intent(activity, HomeActivity::class.java)
        val actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity()
        assertEquals(expectedIntent.component, actual.getComponent())

}
    }