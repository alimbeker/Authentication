package relog.android.authentication.ui.theme.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import relog.android.authentication.databinding.FragmentRegisterBinding
import relog.android.authentication.others.EventObserver
import relog.android.authentication.others.snackBar
import relog.android.authentication.ui.theme.auth.AuthViewModel
import relog.android.authentication.ui.theme.main.MainActivity

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        subscribeToObservers()

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                binding.etEmail.text.toString().trim(),
                binding.etName.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )
        }

        binding.btnGoToLogin.setOnClickListener {
            if (findNavController().previousBackStackEntry != null) {
                findNavController().popBackStack()
            } else {
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                )
            }
        }

    }

    private fun subscribeToObservers() {
        viewModel.registerStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.registerProgressBar.isVisible = false
                binding.btnRegister.isEnabled = true
                snackBar(it)
            },
            onLoading = {
                binding.registerProgressBar.isVisible = true
                binding.btnRegister.isEnabled = false
            }
        ) {
            binding.registerProgressBar.isVisible = false
            binding.btnRegister.isEnabled = true
            Intent(requireContext(), MainActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
