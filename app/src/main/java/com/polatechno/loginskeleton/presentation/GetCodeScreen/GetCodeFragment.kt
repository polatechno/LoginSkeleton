package com.polatechno.loginskeleton.presentation.GetCodeScreen


import android.os.Bundle
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
import com.polatechno.loginskeleton.databinding.FragmentLoginBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GetCodeFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val viewModel: GetCodeScreenModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var phoneNumber = "";
        val listener =
            MaskedTextChangedListener(
                getString(R.string.phone_format_russia), binding.phoneNumberInput,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(
                        maskFilled: Boolean,
                        extractedValue: String,
                        formattedValue: String,
                        tailPlaceholder: String
                    ) {
                        binding.buttonNext.isEnabled = maskFilled
                        phoneNumber = extractedValue;
                        LogManager.print(phoneNumber)
                    }
                })
        binding.phoneNumberInput.addTextChangedListener(listener)
        binding.phoneNumberInput.hint = listener.placeholder();
        binding.phoneNumberInput.onFocusChangeListener = listener
        binding.phoneNumberInput.addTextChangedListener(listener)
        binding.buttonNext.setOnClickListener {
            hideKeyboard()
            if (phoneNumber.isBlank()) {
                binding.buttonNext.isEnabled = false
                binding.phoneNumberInput.requestFocus()
                return@setOnClickListener
            }
            phoneNumber = getString(R.string.phone_code_russia) + phoneNumber
            viewModel.getCode(phoneNumber)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect() { state ->
                if (state.error.isNotBlank()) {
                    LogManager.print("ERROR: " + state.error)
                    binding.phoneNumberContainer.error = state.error
                }

                binding.pbProcessing.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                state.codeResult?.let { result ->
                    binding.apiResult.text = result.toString()
                    openConfirmFragment(
                        phoneNumber,
                        result.code, result.status
                    )
                }
            }
        }
    }

    private fun openConfirmFragment(phoneNumber: String, code: String, status: String) {
        val bundle = Bundle()
        bundle.putString(Constants.PARAM_PHONE_NUMBER, phoneNumber)
        bundle.putString(Constants.PARAM_CODE, code)
        bundle.putString(Constants.PARAM_STATUS, status)
        findNavController().navigate(R.id.actionFromLoginToConfirm, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}