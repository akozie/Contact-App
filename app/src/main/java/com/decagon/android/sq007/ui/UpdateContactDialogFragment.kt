package com.decagon.android.sq007.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.databinding.FragmentUpdateContactDialogBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddContactDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateContactDialogFragment(private var contact: Contact) : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentUpdateContactDialogBinding? = null
    private val binding get() = _binding!!
    private var number: String? = ""
    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Widget_Toolbar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateContactDialogBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.editTextName.setText(contact.firstName)
        binding.editTextPhoneNumber.setText(contact.phoneNumber)

        /**
         * SENDING A MAIL
         * */
        val email = binding.email
        email.setOnClickListener {
            val textEmail = binding.editTextEmail.text.toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + Uri.encode(textEmail)))
            startActivity(intent)
        }



        /**
         * UPDATING THE UPDATE BUTTON USING THE SETONCLICKLISTENER
         * */
        val btn = binding.buttonUpdate
        btn.setOnClickListener {
            /**
             * GETTING THE IDs AND THE THE TEXT
             * */
            val fullName = binding.editTextName.text.toString()
            val contactNumber = binding.editTextPhoneNumber.text.toString()
            val email = binding.editTextEmail.text.toString()
            val address = binding.editTextAddress.text.toString()

            contact.firstName = fullName
            contact.phoneNumber = contactNumber
            contact.email = email
            contact.address = address

            viewModel.updateContact(contact)
            dismiss()
            Toast.makeText(context, "Contact has been updated", Toast.LENGTH_SHORT).show()
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
