package com.example.sayone.firebasedatabasetrial

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val mDatabaseReference = FirebaseDatabase.getInstance().getReference()
    var mUserList:ArrayList<String> = arrayListOf("value")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_btn.setOnClickListener(){
            register()
        }
    }

    override fun onStart() {
        super.onStart()
        getAllUser()
    }

    fun register(){

        //checking if any of the text box is empty or not
        if (!( (emailtxt.text.isEmpty())
            or (nametxt.text.isEmpty())
            or (phonetxt.text.isEmpty())
            or (passwordtxt.text.isEmpty()))){

            var user = User()
            user.mName= nametxt.text.toString()
            user.mEmail=emailtxt.text.toString()
            user.mPhone=phonetxt.text.toString()
            user.mPass=passwordtxt.text.toString()

            // creating a table called user , pushing 1st key and retriving it back as id ( which we
            // can use as primary key for the new user we are inserting
            var id = mDatabaseReference.push().key

            //inside the user table we are creating a new child with the id and inserting USER as its child
            mDatabaseReference.child("user").child(id).setValue(user).addOnCompleteListener(){
                    if (it.isSuccessful)
                        Toast.makeText(this,"values insterted",Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(this,"values not insterted",Toast.LENGTH_LONG).show()
            }


        }
        else{
            Toast.makeText(this,"pls enter all the values",Toast.LENGTH_LONG).show()
        }
    }

    fun getAllUser(){

        // checking if any changes has been made in the child node user
       mDatabaseReference.child("user").addValueEventListener(object:ValueEventListener{

           override fun onDataChange(snapshot: DataSnapshot?) {

               mUserList.clear()
               if (snapshot != null) {
                   for (dataSnapshot:DataSnapshot in snapshot.children){
                        val user:User = dataSnapshot.getValue(User::class.java)!!
                        mUserList.add(user.mName!!)
                   }

               }

               for (i in mUserList.indices){
                   Log.d("users value", mUserList.get(i))
               }
           }

           override fun onCancelled(firebaseError: DatabaseError?) {

           }


       })


    }

}


