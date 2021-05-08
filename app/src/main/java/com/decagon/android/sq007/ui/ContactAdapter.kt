package com.decagon.android.sq007.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.MainActivity
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.databinding.RecyclerViewContactBinding
import java.util.*

class ContactAdapter(val itemClickedListener: OnItemViewClickListener) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    /**
     * MUTABLE LIST OF MY CONTACTS
     * */
    var contacts = mutableListOf<Contact>()

    /**
     * CREATE VIEWS
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerViewContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * RETURNS SIZE OF CONTACTS
     * */
    override fun getItemCount(): Int {
        return contacts.size
    }

    /**
     * BIND CONTACTS WITH SPECIFIC POSITIONS
     * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textName.text = contacts[position].firstName
        holder.binding.textPhone.text = contacts[position].phoneNumber
    }

    /**
     * ADDING CONTACTS ON PHONE
     * */
    fun addContact(contact: Contact) {
        //if contacts does not exist in our mutable contact list
        if (!contacts.contains(contact)) {
            contacts.add(contact)
        } else {
            val index = contacts.indexOf(contact)
            if (contact.isDeleted) {
                contacts.removeAt(index)
            } else {
                contacts[index] = contact
            }
        }
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: RecyclerViewContactBinding) : RecyclerView.ViewHolder(binding.root),View.OnClickListener {

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(adapterPosition!=RecyclerView.NO_POSITION)
            itemClickedListener.onItemViewClicked(binding)

        }


    }
}
