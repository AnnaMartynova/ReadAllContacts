package com.example.readallcontacts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    var listView: ListView? = null
    var myCustomAdapter: MyCustomAdapter? = null
    var contactsInfoList: ArrayList<UserDto>? = null
    var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.listView)
        requestContactPermission()
    }

        @SuppressLint("Range")
        fun getContacts() {
            contactsInfoList = ArrayList()

            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )

            while (cursor!!.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val userDto = UserDto()

                userDto.displayName = name
                userDto.phoneNumber = phoneNumber

                val photo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                if (photo != null){
                    userDto.imageId = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photo))
                }


                contactsInfoList!!.add(userDto)
            }
            cursor.close()

            myCustomAdapter = MyCustomAdapter(this, contactsInfoList!!)
            listView!!.adapter = myCustomAdapter



            searchView?.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                }

                @SuppressLint("Recycle")
                override fun onQueryTextChange(query: String?): Boolean {
//                    contactsInfoList = ArrayList()

                    val cr = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"} LIKE?",
                        Array(1){"%$query%"},
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

//                    val named = cr?.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//
//                    val userDtoo = UserDto()
//
//                    userDtoo.displayName = named
//
//                    contactsInfoList!!.add(userDtoo)
//                    cursor.close()
//
//                    myCustomAdapter = MyCustomAdapter(MainActivity(), contactsInfoList!!)
//                    listView!!.adapter = myCustomAdapter

                    return true
                }
            })


        }

    private fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Read contacts access needed")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                getContacts()
            }
        } else {
            getContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                } else {
                    Toast.makeText(
                        this,
                        "You have disabled a contacts permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    }
}

