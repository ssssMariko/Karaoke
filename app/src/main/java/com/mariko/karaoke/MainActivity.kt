package com.mariko.karaoke

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.mariko.karaoke.databinding.ActivityMainBinding
import com.tencent.imsdk.v2.V2TIMGroupInfoResult
import com.tencent.liteav.basic.UserModelManager
import com.tencent.liteav.debug.GenerateTestUserSig
import com.tencent.liteav.tuikaraoke.model.TRTCKaraokeRoom
import com.tencent.liteav.tuikaraoke.model.TRTCKaraokeRoomManager
import com.tencent.liteav.tuikaraoke.ui.room.KaraokeRoomAudienceActivity
import com.tencent.liteav.tuikaraoke.ui.room.KaraokeRoomCreateDialog
import com.tencent.trtc.TRTCCloudDef

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /*房间号*/
    private lateinit var mEditRoomId: EditText

    private lateinit var mTextEnterRoom: TextView
    private lateinit var mTRTCKaraokeRoom: TRTCKaraokeRoom

    private val userModel = UserModelManager.getInstance().userModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
        // 添加本地的音频资源
        LocalDataHelp.initLocalData(this)
    }


    private fun initView() {
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }
        mEditRoomId = binding.etRoomId
        mEditRoomId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mTextEnterRoom.isEnabled = !TextUtils.isEmpty(mEditRoomId.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        mTextEnterRoom = binding.tvEnter
        mTextEnterRoom.setOnClickListener {
            enterRoom(mEditRoomId.text.toString())
        }

        val buttonCreateRoom = binding.rlCreateRoom
        buttonCreateRoom.setOnClickListener {
            createRoom()
        }
    }

    /**
     * 初始化实例并登录
     */
    private fun initData() {
        // 1.初始化 获取 TRTCKaraokeRoom 单例对象
        mTRTCKaraokeRoom = TRTCKaraokeRoom.sharedInstance(this)
        // 2.登录
        mTRTCKaraokeRoom.login(
            GenerateTestUserSig.SDKAPPID,
            userModel.userId,  //当前用户的 ID，字符串类型，只允许包含英文字母（a-z 和 A-Z）、数字（0-9）、连词符（-）和下划线（_）
            userModel.userSig   //签名
        ) { code, _ ->
            if (code == 0) {//登录成功回调，成功时 code 为0。

                //修改个人信息
                mTRTCKaraokeRoom.setSelfProfile(
                    userModel.userName,
                    userModel.userAvatar
                ) { code, _ ->
                    if (code == 0) {
                        Log.d(TAG, "修改个人信息成功")
                    }
                }
            }
        }
    }

    /**
     * 进入房间（听众调用）
     * @param roomIdStr String
     */
    private fun enterRoom(roomIdStr: String) {
        TRTCKaraokeRoomManager.getInstance()
            .getGroupInfo(roomIdStr, object : TRTCKaraokeRoomManager.GetGroupInfoCallback {
                override fun onSuccess(result: V2TIMGroupInfoResult?) {
                    if (isRoomExist(result)) {
                        realEnterRoom(roomIdStr)
                    } else {
                        ToastUtils.showLong(R.string.room_not_exist)
                    }
                }

                override fun onFailed(code: Int, msg: String?) {
                    ToastUtils.showLong(msg)
                }
            })
    }

    /**
     * 创建房间（房主调用），若房间不存在，系统将自动创建一个新房间。
     */
    private fun createRoom() {
        val dialog = KaraokeRoomCreateDialog(this)
        dialog.showRoomCreateDialog(
            userModel.userId,
            userModel.userId,
            userModel.userAvatar,
            TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT,
            true
        )
    }


    fun isRoomExist(result: V2TIMGroupInfoResult?): Boolean {
        return if (result == null) {
            false
        } else {
            result.resultCode == 0
        }
    }

    fun realEnterRoom(roomIdStr: String) {
        val userId = userModel.userId
        val roomId = roomIdStr.toInt() ?: 10000
        KaraokeRoomAudienceActivity.enterRoom(
            this,
            roomId,
            userId,
            TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT
        )
    }

}