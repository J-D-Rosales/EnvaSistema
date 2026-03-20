package com.example.envasistema

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify

class EnvaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("EnvaApplication", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("EnvaApplication", "Could not initialize Amplify", error)
        }
    }
}
