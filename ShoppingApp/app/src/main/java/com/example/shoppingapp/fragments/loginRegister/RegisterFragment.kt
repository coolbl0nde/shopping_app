package com.example.shoppingapp.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.data.User
import com.example.shoppingapp.databinding.FragmentRegisterBinding
import com.example.shoppingapp.util.RegisterValidation
import com.example.shoppingapp.util.Resource
import com.example.shoppingapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment: Fragment(R.layout.fragment_auth) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            doneRegisterButton.setOnClickListener {
                val user = User(
                    firstNameRegisterEt.text.toString().trim(),
                    lastNameRegisterEt.text.toString().trim(),
                    emailRegisterEt.text.toString().trim()
                )
                val password = passwordRegisterEt.text.toString().trim()

                viewModel.createAccountWithEmailAndPassword(user, password)
            }

            cancelRegisterButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.register.collect {
                    when(it){
                        is Resource.Loading -> {
                            //binding.doneRegisterButton.startAnimation()
                        }

                        is Resource.Success -> {
                            Log.d("test", it.data.toString())
                            //binding.doneRegisterButton.revertAnimation()
                        }

                        is Resource.Error -> {
                            Log.e(TAG, it.message.toString())
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect {
                    if (it.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.emailRegisterEt.apply {
                                requestFocus()
                                error = it.email.message
                            }
                        }
                    }

                    if (it.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.passwordRegisterEt.apply {
                                requestFocus()
                                error = it.password.message
                            }
                        }
                    }

                }
            }
        }

    }
}