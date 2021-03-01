package com.example.locker.features

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.locker.MainActivity

import com.example.locker.R
import com.example.locker.core.RetrofitFactory
import com.example.locker.databinding.FragmentAccountBinding
import com.example.locker.model.response.LockerInfo
import com.example.locker.model.response.UserInfoResponseModel
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.internal.userAgent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_account, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getUserInfo()
    }

    private fun getUserInfo() {
        RetrofitFactory.getInstance()?.getUserServices()?.getUserInfo(MainActivity.token)
            ?.enqueue(object :
                Callback<UserInfoResponseModel> {
                override fun onFailure(call: Call<UserInfoResponseModel>, t: Throwable) {
                    Log.d("eeeeeerrrrrrrr", "ooorrrr")
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<UserInfoResponseModel>,
                    response: Response<UserInfoResponseModel>
                ) {
                    if (response.code() == 200) {
                        binding.userData = UserInfoResponseModel(
                            response.body()?.id,
                            response.body()?.username,
                            response.body()?.nickName,
                            datePostprocess(response.body()?.signUpAt),
                            datePostprocess(response.body()?.lastLoginAt),
                            response.body()?.credit,
                            LockerInfo(
                                response.body()?.currentLocker?.id,
                                response.body()?.currentLocker?.status,
                                response.body()?.currentLocker?.ownerInfo,
                                response.body()?.currentLocker?.maxEndurance,
                                datePostprocess(response.body()?.currentLocker?.lastModified)
                                )
                        )
                    } else {

                        Toast.makeText(
                            requireContext(),
                            "server error :) user info",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

}

fun datePostprocess(str: String?): String {
    if (str == null) return ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var time = LocalDateTime.parse(str)
        return time.toLocalDate().toString() + " " + time.toLocalTime().toString();
    } else {
        return ""
    };
}

