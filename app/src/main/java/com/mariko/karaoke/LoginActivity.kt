package com.mariko.karaoke

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mariko.karaoke.databinding.ActivityLoginBinding
import com.tencent.liteav.basic.AvatarConstant
import com.tencent.liteav.basic.UserModel
import com.tencent.liteav.basic.UserModelManager
import com.tencent.liteav.debug.GenerateTestUserSig
import java.util.*

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mEditUserId: EditText
    private lateinit var mButtonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mEditUserId = binding.etUserId
        mButtonLogin = binding.tvLogin
        mButtonLogin.apply {
            setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        val userId = mEditUserId.text.toString().trim()
        val userModel = UserModel()
        userModel.apply {
            this.userId = userId
            userName = userId
            userSig = GenerateTestUserSig.genTestUserSig(userId)
            val index = Random().nextInt(AvatarConstant.USER_AVATAR_ARRAY.count())
            val coverUrl = AvatarConstant.USER_AVATAR_ARRAY[index]
            userAvatar = coverUrl
        }
        val manager = UserModelManager.getInstance()
        manager.userModel = userModel

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}