package com.jussa.locaautos.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.jussa.locaautos.R

//private const val ARG_STR_USUARIO = "strUsuario"

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnOpenListAutos).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnHomeSair).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnOpenListAutos -> {
                navController.navigate(R.id.action_homeFragment_to_listAutoFragment)
            }
            R.id.btnHomeSair -> {
                FirebaseAuth.getInstance().signOut()
                //Mata todas as telas e sai do aplicativo
                ActivityCompat.finishAffinity(this.requireActivity())
                navController.navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }//when
    }//onClick

}