package com.decagon.android.sq007.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.databinding.FragmentContactBinding
import com.decagon.android.sq007.databinding.RecyclerViewContactBinding
import java.util.jar.Manifest

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment(),OnItemViewClickListener {
    // TODO: Get the object of the layout.
    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel
    private var adapter = ContactAdapter(this)

    private val _contact = MutableLiveData<Contact>()

    val contact: LiveData<Contact> get() = _contact

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the views of this fragment layout and inflate the layout for this fragment
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        //initializing the viewmodelclass
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        setUpPermissions(android.Manifest.permission.READ_CONTACTS, "contacts", 1)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //binding the recyclerviewcontact adapter to the contact adapter
        binding.recyclerViewContact.adapter = adapter

    //show the addcontact fragment
        binding.createButton.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager, "")
        }

    //observer if a contact has been added
        viewModel.contact.observe(
            viewLifecycleOwner,
            Observer {
                adapter.addContact(it)
            }
        )
        viewModel.getRealTimeUpdate()

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContact)

}
         //inside onViewCreated: This is to setup your permission

    private fun setUpPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(requireContext(), "$name Permission Granted", Toast.LENGTH_LONG).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> requireActivity().requestPermissions(arrayOf(permission), requestCode)
            }
        }
    }
    fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Permission required")
            setMessage("Permission to access your $name is required to use this app")
            setPositiveButton("Allow") { dialog, which ->
                requireActivity().requestPermissions(arrayOf(permission), requestCode)
            }
        }
        builder.create().show()
    }

    /**
     * MAKING CALLBACKS ON MOVEMENT ON ANY CONTACTS, SWIPING LEFT OR RIGHT
     * */
    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition
            var currentContact = adapter.contacts[position]

            when (direction) {
                /**
                 * SWIPE RIGHT TO UPDATE A CONTACT
                 * */
                ItemTouchHelper.RIGHT -> {
                    UpdateContactDialogFragment(currentContact).show(childFragmentManager, "")
                }
                /**
                 * SWIPE LEFT TO DELETE A CONTACT
                 * */
                ItemTouchHelper.LEFT -> {
                    AlertDialog.Builder(requireContext()).also {
                        it.setTitle("Are you sure you want to delete this contact?")
                        it.setNegativeButton("NO") { dialog, which ->
                            viewModel.addContact(currentContact)
                            binding.recyclerViewContact.adapter?.notifyItemChanged(position)
                            Toast.makeText(context, "contact is the same", Toast.LENGTH_SHORT).show()
                        }
                        it.setPositiveButton("Yes") { dialog, which ->
                            viewModel.deleteContact(currentContact)
                            binding.recyclerViewContact.adapter?.notifyItemRemoved(position)
                            Toast.makeText(context, "contact has been deleted", Toast.LENGTH_SHORT).show()
                        }
                    }.create().show()
                }
            }

            binding.recyclerViewContact.adapter?.notifyDataSetChanged()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemViewClicked(binding: RecyclerViewContactBinding) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra("NAME",binding.textName.text.toString() )
        intent.putExtra("PHONE_NUMBER",binding.textPhone.text.toString())
        startActivity(intent)
    }
}
