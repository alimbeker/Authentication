package relog.android.authentication.ui.theme.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import relog.android.authentication.R
import relog.android.authentication.databinding.FragmentLoginBinding
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

            btnGoToRegister.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                if (findNavController().currentDestination?.id == R.id.loginFragment) {
                    findNavController().navigate(action)
                } else {
                    findNavController().popBackStack(R.id.loginFragment, false)
                }
            }
        }
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AuthViewModel.UiEvent.ShowSnackbar -> {
                        binding.loginProgressBar.isVisible = false
                        binding.btnLogin.isEnabled = true
                        snackBar(event.message)
                    }
                    is AuthViewModel.UiEvent.NavigateToMain -> {
                        binding.loginProgressBar.isVisible = false
                        binding.btnLogin.isEnabled = true
                        Intent(requireContext(), MainActivity::class.java).also {
                            startActivity(it)
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
