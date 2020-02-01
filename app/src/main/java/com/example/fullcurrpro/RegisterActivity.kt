package com.example.fullcurrpro

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_register_acivity.*


class RegisterActivity : AppCompatActivity(), View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_acivity)

        setPointer()
    }

    private fun setPointer() {

        this.reg_btnCancel.setOnClickListener(this)
        this.reg_btnRegister.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.reg_btnCancel -> {
                finish()
            }
            R.id.reg_btnRegister -> {
                registering()
            }
        }
    }

    private fun registering() {
        var userName = reg_userName
        var userPass = reg_pass
        var userPass2 = reg_pass2

        var uName = userName.getText().toString()
        var uPass = userPass.getText().toString()
        var uPass2 = userPass2.getText().toString()

        if(checkUserFields(uName,uPass,uPass2)){
            val user = BackendlessUser()
            user.setProperty("name", uName)
            user.setProperty("password", uPass)
            Backendless.initApp(applicationContext, "A0EC6A1E-1043-4D60-88BC-9F71CC280A14", "F5936754-EF32-4CE1-9EF0-92C71AFB4139")

            Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
                override fun handleResponse(registeredUser: BackendlessUser?) {
                    Toast.makeText(applicationContext,"User registered",Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun handleFault(fault: BackendlessFault) { // an error has occurred, the error code can be retrieved with fault.getCode()
                    Toast.makeText(applicationContext,fault.message,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun checkUserFields(uName:String,uPass:String,uPass2:String):Boolean {
        if (uName.length < 3 || uPass.length < 3){
            Toast.makeText(applicationContext,"name or password too short",Toast.LENGTH_SHORT).show()
            return false
        }
        if (!uPass.equals(uPass2)){
            Toast.makeText(applicationContext,"Password not match!!",Toast.LENGTH_SHORT).show()
            return false
       }
        return true
    }
}
