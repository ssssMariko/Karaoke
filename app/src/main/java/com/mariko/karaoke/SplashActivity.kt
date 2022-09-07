package com.mariko.karaoke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.tencent.liteav.basic.UserModelManager

private const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: intent -> ${intent?.data}")
        setIntent(intent)
        navigation()
    }

    private fun navigation() {
        val userModelManager = UserModelManager.getInstance()
        if (TextUtils.isEmpty(userModelManager.userModel.userId)){
           val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}