package com.decagon.android.sq007.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.decagon.android.sq007.R
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.databinding.FragmentAddContactDialogBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddContactDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddContactDialogFragment : DialogFragment() {
    // TODO: binding object
    private var _binding: FragmentAddContactDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //fragment style
        setStyle(STYLE_NO_TITLE, android.R.style.Widget_Toolbar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //observing the live data
        viewModel.result.observe(
            viewLifecycleOwner,
            Observer {
                val message = if (it == null) {
                    getString(R.string.added_contact)
                } else {
                    getString(R.string.error, it.message)
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        )

        //onclicklistener on the button
        val btn = binding.button
        btn.setOnClickListener {
            //getting the inputs of the fields
            val fullName = binding.editTextName.text.toString()
            val contactNumber = binding.editTextPhoneNumber.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val address = binding.editTextAddress.text.toString().trim()

            val contact = Contact()
            contact.firstName = fullName
            contact.phoneNumber = contactNumber
            contact.email = email
            contact.address = address

            //adding our contacts to the viewmodel
            viewModel.addContact(contact)

            //name and number validation
            if (fullName.isEmpty()) {
                binding.editTextName.error = "This field is required"
                return@setOnClickListener
            }
            if (contactNumber.isEmpty()) {
                binding.editTextPhoneNumber.error = "This field is required"
                return@setOnClickListener
            }
        }
    }
}
