package com.example.locker.features

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.locker.databinding.FragmentHomeBinding
import com.example.locker.model.response.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    lateinit var timer: Timer

    var lockerIsUnlock = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getStatus()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initValue()
        getUserInfo()
        initView()
    }

    private fun initValue() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                if (lockerIsUnlock)
                    getStatus()
                mainHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun initView() {
        btn_return.visibility = View.GONE
        btn_return.setOnClickListener { returnLocker() }
        btn_unlock.setOnClickListener {
            if (binding.hasLocker == true)
                openLocker()
            else
                getLocker()
        }
    }

    private fun getStatus() {
        RetrofitFactory.getInstance()?.getUserServices()?.getUserStatus(MainActivity.token)
            ?.enqueue(object :
                Callback<StatusResponseModel> {
                override fun onFailure(call: Call<StatusResponseModel>, t: Throwable) {
                    Log.d("eeeeeerrrrrrrr", "ooorrrr")
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<StatusResponseModel>,
                    response: Response<StatusResponseModel>
                ) {
                    if (response.code() == 200) {
                        binding.status = response.body()?.status.equals("OPEN", true)
                        lockerIsUnlock = response.body()?.status == "OPEN"
                    } else
                        Toast.makeText(
                            requireContext(),
                            "server error :) get locker status",
                            Toast.LENGTH_LONG
                        ).show()
                }
            })
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
                        binding.hasLocker = !(response.body()?.currentLocker == null)
                        response.body()?.currentLocker?.let {
                            binding.id = it.id.toString()
                            btn_return.visibility = View.VISIBLE
                        }
                        response.body()?.credit?.let {

                            binding.credit = it
                            if (it < 100)
                                tv_cost.setTextColor(resources.getColor(R.color.colorPinkUsed))

                        }
                        if (response.body()?.currentLocker == null)
                            btn_return.visibility = View.GONE
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

    private fun openLocker() {
        RetrofitFactory.getInstance()?.getUserServices()?.openLocker(MainActivity.token)
            ?.enqueue(object :
                Callback<OpenResponseModel> {
                override fun onFailure(call: Call<OpenResponseModel>, t: Throwable) {
                    Log.d("eeeeeerrrrrrrr", "ooorrrr")
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<OpenResponseModel>,
                    response: Response<OpenResponseModel>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.status == true) {
                            getStatus()
                        } else {

                            Toast.makeText(
                                requireContext(),
                                "could not open the locker please retry",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else if (response.code() == 406) {
                        if (response.body()?.reason == "negative credit")
                            Toast.makeText(
                                requireContext(),
                                "your balance is negative please charge your account first! ",
                                Toast.LENGTH_LONG
                            ).show()
                        else
                            Toast.makeText(
                                requireContext(),
                                response.body()?.reason,
                                Toast.LENGTH_LONG
                            ).show()
                    } else
                        Toast.makeText(
                            requireContext(),
                            "server error :) open locker",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
            )
    }

    private fun returnLocker() {
        RetrofitFactory.getInstance()?.getUserServices()?.returnLocker(MainActivity.token)
            ?.enqueue(object :
                Callback<ReturnResponseModel> {
                override fun onFailure(call: Call<ReturnResponseModel>, t: Throwable) {
                    Log.d("eeeeeerrrrrrrr", "ooorrrr")
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<ReturnResponseModel>,
                    response: Response<ReturnResponseModel>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.status == true)
                            getUserInfo()
                        else {
                            getUserInfo()
                            Toast.makeText(
                                requireContext(),
                                "could not return the locker please retry",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else
                        Toast.makeText(
                            requireContext(),
                            "server error :) return locker",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
            )
    }

    private fun getLocker() {
        RetrofitFactory.getInstance()?.getUserServices()?.takeLocker(MainActivity.token)
            ?.enqueue(object :
                Callback<TakeResponseModel> {
                override fun onFailure(call: Call<TakeResponseModel>, t: Throwable) {
                    Log.d("eeeeeerrrrrrrr", "ooorrrr")
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<TakeResponseModel>,
                    response: Response<TakeResponseModel>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.lockerId == null) {
                            if (response.body()?.reason == "credit not enough")
                                Toast.makeText(
                                    requireContext(),
                                    "Your balance is negative please charge your account first! ",
                                    Toast.LENGTH_LONG
                                ).show()
                            Toast.makeText(
                                requireContext(),
                                response.body()?.reason,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            getUserInfo()
                            getStatus()
                        }
                    } else if (response.code() == 406) {
                        if (response.body()?.reason == "negative credit")
                            Toast.makeText(
                                requireContext(),
                                "your balance is negative please charge your account first! ",
                                Toast.LENGTH_LONG
                            ).show()
                        else
                            Toast.makeText(
                                requireContext(),
                                response.body()?.reason,
                                Toast.LENGTH_LONG
                            ).show()
                    } else
                        Toast.makeText(
                            requireContext(),
                            "Server error :) take locker",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
            )
    }


}
