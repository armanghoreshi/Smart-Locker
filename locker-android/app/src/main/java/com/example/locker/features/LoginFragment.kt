package com.example.locker.features

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.locker.MainActivity

import com.example.locker.R
import com.example.locker.core.RetrofitFactory
import com.example.locker.model.body.LoginBodyModel
import com.example.locker.model.response.LoginResponseModel
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    fun initView() {
//        Toast.makeText(requireContext(),"hi",Toast.LENGTH_LONG).show()
        btn_login.setOnClickListener {

            sendRequest()
        }
        tv_register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun sendRequest() {
        RetrofitFactory.getInstance()?.getUserServices()?.loginUser(
            LoginBodyModel(
                et_username_login.text.toString(),
                et_password_login.text.toString()
            )
        )?.enqueue(object :
            Callback<LoginResponseModel> {
            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                Log.d("eeeeeerrrrrrrr", "ooorrrr")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<LoginResponseModel>,
                response: Response<LoginResponseModel>
            ) {
                if (response.code() == 200) {

                    val loginResponseModel = response.body() as LoginResponseModel
                    if (loginResponseModel.status == "OK") {
                        MainActivity.token = loginResponseModel.token
                        findNavController().navigate(R.id.baseFragment)
                    } else if (loginResponseModel.status == "WRONG_PASSWORD")
                        Toast.makeText(
                            requireContext(),
                            "password is incorrect",
                            Toast.LENGTH_LONG
                        ).show()
                    else if(loginResponseModel.status == "NOT_EXIST")
                        Toast.makeText(
                            requireContext(),
                            "username does not exists",
                            Toast.LENGTH_LONG
                        ).show()
                }
                else{
                    Toast.makeText(
                        requireContext(),
                        "something went wrong with the server",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
