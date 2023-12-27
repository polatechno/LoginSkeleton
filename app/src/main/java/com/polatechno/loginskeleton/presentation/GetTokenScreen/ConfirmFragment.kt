package com.polatechno.loginskeleton.presentation.GetTokenScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polatechno.loginskeleton.R
import com.polatechno.loginskeleton.common.Constants
import com.polatechno.loginskeleton.common.LogManager
import com.polatechno.loginskeleton.common.hideKeyboard
import com.polatechno.loginskeleton.databinding.FragmentConfirmBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ConfirmFragment : Fragment() {

    private var _binding: FragmentConfirmBinding? = null

    private val viewModel: ConfirmScreenViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentConfirmBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var phoneNumber = ""
        var code = ""
        var status = ""
        arguments?.let {
            phoneNumber = it.getString(Constants.PARAM_PHONE_NUMBER) ?: ""
            code = it.getString(Constants.PARAM_CODE) ?: ""
            status = it.getString(Constants.PARAM_STATUS) ?: ""
            LogManager.print("$phoneNumber $code $status")
        }

        val listener =
            MaskedTextChangedListener("[000000]", binding.confirmCodeInput,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(
                        maskFilled: Boolean,
                        extractedValue: String,
                        formattedValue: String,
                        tailPlaceholder: String
                    ) {
                        binding.buttonConfirm.isEnabled = maskFilled
                    }
                })

        binding.confirmCodeInput.addTextChangedListener(listener)
        binding.confirmCodeInput.hint = listener.placeholder();
        binding.confirmCodeInput.onFocusChangeListener = listener
        binding.confirmCodeInput.addTextChangedListener(listener)

        binding.apiResult.text = "$phoneNumber $code $status"

        binding.buttonConfirm.setOnClickListener {
            hideKeyboard()
            if (binding.confirmCodeInput.text.isNullOrBlank()) {
                binding.buttonConfirm.isEnabled = false
                binding.confirmCodeInput.requestFocus()
                return@setOnClickListener
            }
            viewModel.getToken(phoneNumber, binding.confirmCodeInput.text.toString())
        }

        binding.buttonResend.setOnClickListener {
            hideKeyboard()
            binding.confirmCodeInput.text = null
            binding.confirmCodeContainer.error = null
            viewModel.regenerateCode(phoneNumber)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateConfirmScreen.collect() { state ->

                if (state.error.isNotBlank()) {
                    LogManager.print("ERROR: " + state.error)
                    binding.confirmCodeContainer.error = state.error
                }

                binding.pbProcessing.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                state.token?.let { token ->
                    binding.apiResult.text = token
                    //navigate to welcome
                    openWelcomeFragment(phoneNumber, token)
                }


                binding.buttonResend.visibility =
                    if (state.canRegenerate) View.VISIBLE else View.GONE


                state.regeneratedCode?.let { regeneratedCode ->
                    binding.apiResult.text = "Regenerated code: $regeneratedCode"
                }
            }
        }
    }


    private fun openWelcomeFragment(phoneNumber: String, token: String) {
        val bundle = Bundle()
        bundle.putString(Constants.PARAM_PHONE_NUMBER, phoneNumber)
        bundle.putString(Constants.PARAM_TOKEN, token)
        findNavController().navigate(R.id.actionFromConfirmToWelcome, bundle)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}