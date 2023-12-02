package ru.araok.presentation.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.data.Repository
import ru.araok.data.dto.JwtRequestDto
import ru.araok.databinding.FragmentAuthorizationBinding
import ru.araok.presentation.ViewModelFactory
import javax.inject.Inject
import androidx.navigation.fragment.findNavController
import ru.araok.R
import ru.araok.maskPhoneToNumberPhone

@AndroidEntryPoint
class AuthorizationFragment: Fragment() {
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: AuthorizationViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authorization.setOnClickListener {
            val phoneNumber = maskPhoneToNumberPhone(binding.phone.text?.toString()!!)

            val jwtRequest = JwtRequestDto(
                phone = phoneNumber,
                password = binding.password.text.toString()
            )

            viewModel.loginClient(jwtRequest)
        }

        viewModel.login.onEach {
            if(it.accessToken != null && it.refreshToken != null) {
                Repository.saveAccessToken(requireContext(), it.accessToken)
                Repository.saveRefreshToken(requireContext(), it.refreshToken)
                findNavController().navigate(R.id.authorization_to_profile)
            } else {
                Toast.makeText(requireContext(), "Неправильный номер телефона или пароль!", Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = AuthorizationFragment()
    }
}