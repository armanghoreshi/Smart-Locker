package com.example.locker.features

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.locker.MainActivity

import com.example.locker.R
import com.example.locker.core.RetrofitFactory
import com.example.locker.model.body.SignUpBodymodel
import com.example.locker.model.response.SignUpResponseModel
import com.example.locker.model.response.StatusResponseModel
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }


    private fun initView(){
        btn_register.setOnClickListener {
            sendRequest()
        }
    }



    private fun sendRequest(){
        RetrofitFactory.getInstance()?.getUserServices()?.signUPUser(SignUpBodymodel(et_nickname_register.text.toString(), et_username_register.text.toString(), et_password_register.text.toString(), et_email_register.text.toString()))?.enqueue(object :
            Callback<SignUpResponseModel> {
            override fun onFailure(call: Call<SignUpResponseModel>, t: Throwable) {
                Log.d("eeeeeerrrrrrrr", "ooorrrr")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<SignUpResponseModel>, response: Response<SignUpResponseModel>) {
                if(response.code() == 200){
                    MainActivity.token = (response.body() as SignUpResponseModel).token
                    findNavController().navigate(R.id.action_registerFragment_to_baseFragment)
                }
            }
        })
    }
}
