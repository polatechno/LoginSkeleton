package com.polatechno.loginskeleton.presentation.SuccessScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polatechno.loginskeleton.R
import com.polatechno.loginskeleton.common.Constants
import com.polatechno.loginskeleton.common.LogManager
import com.polatechno.loginskeleton.databinding.FragmentConfirmBinding
import com.polatechno.loginskeleton.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var phoneNumber = ""
        var token = ""
        arguments?.let {
            phoneNumber = it.getString(Constants.PARAM_PHONE_NUMBER) ?: ""
            token = it.getString(Constants.PARAM_TOKEN) ?: ""
            LogManager.print("$phoneNumber $token ")
        }

        binding.welcomeMessage.text = "Здравствуйте, “${phoneNumber}”, Ваш токен “${token}"


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}