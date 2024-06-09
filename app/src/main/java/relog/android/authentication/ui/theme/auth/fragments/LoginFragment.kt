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
import relog.android.authentication.databinding.FragmentLoginBinding
import relog.android.authentication.others.EventObserver
import relog.android.authentication.others.snackBar
import relog.android.authentication.ui.theme.auth.AuthViewModel
import relog.android.authentication.ui.theme.main.MainActivity

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        subscribeToObservers()

        binding.apply {
            btnLogin.setOnClickListener {
                viewModel.login(
                    etEmail.text.toString().trim(),
                    etPassword.text.toString().trim()
                )
            }
        }

        binding.apply {
            btnGoToRegister.setOnClickListener {
                if (findNavController().previousBackStackEntry != null) {
                    findNavController().popBackStack()
                } else {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                    )
                }
            }
        }

    }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.loginProgressBar.isVisible = false
                binding.btnLogin.isEnabled = true
                snackBar(it)
            },
            onLoading = {
                binding.loginProgressBar.isVisible = true
                binding.btnLogin.isEnabled = false
            }
        ) {
            binding.loginProgressBar.isVisible = false
            binding.btnLogin.isEnabled = true
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
