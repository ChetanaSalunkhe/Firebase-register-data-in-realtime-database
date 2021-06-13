package com.chetana.firebaseauthdemo

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_to_database.*
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Comment

class AddToDatabaseActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    var btninsert: Button?= null
    var email:EditText?= null
    var password:EditText?= null
    var contact:EditText?= null
    var name:EditText?= null
    lateinit var list: MutableList<User>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_database)

        init()

        setListeners()
    }

    fun init() {

        name = findViewById(R.id.name) as EditText;
        contact = findViewById(R.id.contact) as EditText;
        email = findViewById(R.id.email) as EditText;
        password = findViewById(R.id.password) as EditText;
        btninsert = findViewById(R.id.btninsert) as Button;

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("User")
        list = mutableListOf()
    }

    fun setListeners(){
        btninsert?.setOnClickListener {
            signupUser()
        }

        btnread?.setOnClickListener {
            database.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0!!.exists()) {
                        list.clear()
                        for (h in p0.children) {
                            val i = h.getValue(User::class.java)
                            // list.add(i!!)
                            list.add(i!!)

                            // Toast.makeText(this,"Data : "+i!!.user,Toast.LENGTH_SHORT).show()
                        }

                        try{
                            var adapter = Adapter(AddToDatabaseActivity(), R.layout.custom_layout, list.toList())
                            listView.adapter = adapter
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }

                }

            });
        }

        /*btnread?.setOnClickListener {
            val childEventListener = object : ChildEventListener{
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val comment = snapshot.getValue()

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    val commentKey = dataSnapshot.key

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    val movedComment = dataSnapshot.getValue()
                    val commentKey = dataSnapshot.key

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                //    Toast.makeText(this, "Failed to load comments.", Toast.LENGTH_SHORT).show()
                }


            }

            database.addChildEventListener(childEventListener)


            //way two

           *//* database.child("users").child(userid).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }*//*

        }*/


    }

    private fun signupUser(){
        if(name?.text.toString().isEmpty()){
            name?.setError("Please enter name")
            name?.requestFocus()
            return
        }

        if(contact?.text.toString().isEmpty()){
            contact?.setError("Please enter contact")
            contact?.requestFocus()
            return
        }

        if(email?.text.toString().isEmpty()){
            email?.setError("Please enter email")
            email?.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email?.text.toString()).matches()){
            email?.error = "Please enter valid email"
            password?.requestFocus()
            return
        }

        if(password?.text.toString().isEmpty()){
            password?.error = "Please enter password"
            password?.requestFocus()
            return
        }

        //check for registeration
        auth.createUserWithEmailAndPassword(email?.text.toString(), password?.text.toString())
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){

                    val id = database.push().key
                    val user = User(name?.text.toString(),email?.text.toString(),contact?.text.toString(),password?.text.toString())
                    database.child("users").child(id.toString()).setValue(user)

                    Toast.makeText(this,"User Register Successful", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }

    }
}
