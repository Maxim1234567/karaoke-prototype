package ru.araok.presentation.registration

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.R
import ru.araok.data.Repository
import ru.araok.data.dto.UserDto
import ru.araok.databinding.FragmentRegistrationBinding
import ru.araok.maskPhoneToNumberPhone
import ru.araok.presentation.ViewModelFactory
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class RegistrationFragment: Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RegistrationViewModel by viewModels { viewModelFactory }
    
    private var year = Calendar.getInstance().get(Calendar.YEAR)
    private var month = Calendar.getInstance().get(Calendar.MONTH)
    private var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.birthDate.text = "Дата рождения $day.$month.$year"
        
        val datePicker = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
            this.year = year
            this.month = month
            this.day = day

            binding.birthDate.text = "Дата рождения $day.$month.$year"
        }, year, month, day)

        binding.birthDate.setOnClickListener { 
            datePicker.show()
        }

        binding.registration.setOnClickListener {
            if(binding.password.text.toString() != binding.repeatPassword.text.toString()) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = UserDto(
                name = binding.name.text.toString(),
                phone = maskPhoneToNumberPhone(binding.phone.text?.toString()!!),
                password = binding.password.text.toString(),
                birthDate = LocalDate.of(year, month, day),
                role = "USER"
            )

            viewModel.registerClient(user)
        }

        viewModel.registration.onEach {
            if(it.token.accessToken != null && it.token.refreshToken != null) {
                Repository.saveUserId(requireContext(), it.user.id)
                Repository.saveAccessToken(requireContext(), it.token.accessToken!!)
                Repository.saveRefreshToken(requireContext(), it.token.refreshToken!!)
                findNavController().navigate(R.id.registration_to_profile)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }
}