package com.jussa.locaautos.ui.login

//import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.jussa.locaautos.R

class LoginFragment : Fragment(), View.OnClickListener   {
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle
    private lateinit var connect: FirebaseAuth
    private lateinit var vTxtEmailLogin: EditText
    private lateinit var vTxtPassLogin: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        vTxtEmailLogin = view.findViewById(R.id.txtUserLogin)
        vTxtPassLogin = view.findViewById(R.id.txtPasswordLogin)

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnRegister).setOnClickListener(this)
    }

    //@RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(v: View?){
        //progressBar.setTransitionVisibility(View.VISIBLE)
        when(v!!.id){
            R.id.btnRegister -> {
                navController.navigate(R.id.action_loginFragment_to_cadastroFragment)
            }
            R.id.btnLogin -> {
                try {
                    connect = FirebaseAuth.getInstance()
                    connect.signInWithEmailAndPassword(vTxtEmailLogin.text.toString(), vTxtPassLogin.text.toString())
                        .addOnSuccessListener {
                            //Toast.makeText(this.context, x.toString(), Toast.LENGTH_SHORT).show()
                            bundle = bundleOf (
                                "argEmail" to vTxtEmailLogin.text.toString(),
                                "argPassword" to  vTxtPassLogin.text.toString()
                            )
                            //navController.navigate(R.id.action_loginFragment_to_listAutoFragment, bundle)
                            navController.navigate(R.id.action_loginFragment_to_listAutoFragment)
                        } //onSuccess
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro ao efetuar o login: \n ${it.cause.toString()}", Toast.LENGTH_LONG).show()
                        } //onFailure
                        }catch(e: Exception) {
                    Toast.makeText(requireContext(), "Erro da aplicação!", Toast.LENGTH_SHORT).show()
                }

            }
        }
        //progressBar.setProgress()
    }

} /* CLASS */