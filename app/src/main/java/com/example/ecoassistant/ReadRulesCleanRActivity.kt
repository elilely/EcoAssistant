package com.example.ecoassistant

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.*

class ReadRulesCleanRActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listView: ListView
    private var listData = ArrayList<String>()
    private var listCompleteData = ArrayList<DataClassLZP>()
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_rules)

        listView = findViewById(R.id.listView)
        listData = ArrayList()
        listCompleteData = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listData)
        listView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("CleanR")

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear()
                listCompleteData.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClassLZP::class.java)
                    if (dataClass != null) {

                        val textDrikst = dataClass.drikst?.split(";")?.joinToString("\n")
                        val newTextDer = textDrikst?.replace(Regex("^", RegexOption.MULTILINE), "• ")
                        dataClass.drikst = newTextDer

                        val textNedrikst = dataClass.nedrikst?.split(";")?.joinToString("\n")
                        val newTextNedrikst = textNedrikst?.replace(Regex("^", RegexOption.MULTILINE), "• ")
                        dataClass.nedrikst = newTextNedrikst

                        listData.add(dataClass.name!!)
                        listCompleteData.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var dataClass = DataClassLZP()
                dataClass = listCompleteData[position]
                val intent = Intent(this@ReadRulesCleanRActivity, ShowRulesActivity::class.java)
                intent.putExtra("name", dataClass.name)
                intent.putExtra("drikst", dataClass.drikst)
                intent.putExtra("nedrikst", dataClass.nedrikst)
                intent.putExtra("konteiners", dataClass.konteiners)
                intent.putExtra("noderigi", dataClass.noderigi)
                val bundle = Bundle()
                bundle.putString("maksaTitle", "Noderīgi: ")
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
    }
}

