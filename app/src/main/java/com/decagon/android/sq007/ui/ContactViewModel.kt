package com.decagon.android.sq007.ui
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.data.NODE_CONTACTS
import com.google.firebase.database.*
import java.lang.Exception

class ContactViewModel : ViewModel() {

    /**
     * Getting reference OF OUR FIREBASE DATABASE
     * */
    private var dbContacts = FirebaseDatabase.getInstance().getReference(NODE_CONTACTS)

    /**
     * LIVE DATA, checking if our data has been submitted successfully or not
     * */
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    //for displaying data, observing the mutablelivedata
    private val _contact = MutableLiveData<Contact>()

    val contact: LiveData<Contact> get() = _contact

    /**
     * METHOD TO ADD CONTACTS AND SETTING VALUE(S) TO THE DATABASE
     * */
    fun addContact(contact: Contact) {
        //generating a key in firebase for this object and storing the id in our object
        contact.id = dbContacts.push().key

        //saving the contact using the id to the firebase
        dbContacts.child(contact.id!!).setValue(contact).addOnCompleteListener {
            if (it.isSuccessful) {
                //setting the value to the mutable live data
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    //adding a child event listener to our database
    private var childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            _contact.value = contact!!
        }

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            _contact.value = contact!!
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            contact?.isDeleted = true
            _contact.value = contact!!
        }
    }

    //update if a contact has been added to our firebase
    fun getRealTimeUpdate() {
        dbContacts.addChildEventListener(childEventListener)
    }

    /**
     * UPDATING OUR CONTACT AND DATABASE
     * */
    fun updateContact(contact: Contact) {
        dbContacts.child(contact.id!!).setValue(contact).addOnCompleteListener {
            if (it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    /**
     * DELETING ANY CONTACT AND UPDATING THE DATABSE
     * */
    fun deleteContact(contact: Contact) {
        dbContacts.child(contact.id!!).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        dbContacts.removeEventListener(childEventListener)
    }
}
