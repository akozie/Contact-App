package com.decagon.android.sq007.data

import com.google.firebase.database.Exclude

data class Contact(
    @get:Exclude
    var id: String? = null,
    @get:Exclude
    var isDeleted: Boolean = false,
    var firstName: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var address: String? = null

) {
    override fun equals(other: Any?): Boolean {
        return if (other is Contact) {
            other.id == id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + isDeleted.hashCode()
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (phoneNumber?.hashCode() ?: 0)
        return result
    }
}
