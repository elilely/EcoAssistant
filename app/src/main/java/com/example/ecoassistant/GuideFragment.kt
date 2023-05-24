package com.example.ecoassistant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoassistant.databinding.FragmentGuideBinding
import com.google.firebase.database.*
import java.util.Locale
import kotlin.collections.ArrayList

class GuideFragment : Fragment() {

    private var binding1: FragmentGuideBinding? = null
    private val binding2 get() = binding1!!

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var categoriesList = ArrayList<DataClass>()
    private var completeList = ArrayList<DataClass>()
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding1 = FragmentGuideBinding.inflate(inflater, container, false)
        return binding2.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding1 = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(context, 1)
        binding2.recyclerView.layoutManager = gridLayoutManager

        categoriesList = ArrayList()
        completeList = ArrayList()
        categoriesAdapter = CategoriesAdapter(this@GuideFragment, categoriesList)
        binding2.recyclerView.adapter = categoriesAdapter
        recyclerView = binding2.recyclerView
        databaseReference = FirebaseDatabase.getInstance().getReference("Materials")

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriesList.clear()
                completeList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    if (dataClass != null) {
                        val textDer = dataClass.der?.split(";")?.joinToString("\n")
                        val newTextDer = textDer?.replace(Regex("^", RegexOption.MULTILINE), "• ")
                        dataClass.der = newTextDer

                        val textNeder = dataClass.neder?.split(";")?.joinToString("\n")
                        val newTextNeder =
                            textNeder?.replace(Regex("^", RegexOption.MULTILINE), "• ")
                        dataClass.neder = newTextNeder

                        val textSagatavo = dataClass.sagatavo?.split(";")?.joinToString("\n")
                        val newTextSagatavo =
                            textSagatavo?.replace(Regex("^", RegexOption.MULTILINE), "• ")
                        dataClass.sagatavo = newTextSagatavo

                        categoriesList.add(dataClass)
                        completeList.add(dataClass)
                    }
                }
                categoriesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        categoriesAdapter.setOnItemClickListener(object : CategoriesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                var dataClass = DataClass()
                dataClass = completeList[position]
                val intent = Intent(activity, MaterialActivity::class.java)
                intent.putExtra("image", dataClass.image)
                intent.putExtra("name", dataClass.name)
                intent.putExtra("der", dataClass.der)
                intent.putExtra("neder", dataClass.neder)
                intent.putExtra("sagatavo", dataClass.sagatavo)
                intent.putExtra("parstrade", dataClass.parstrade)
                startActivity(intent)
            }
        })

        binding2.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<DataClass>()
            for (i in categoriesList) {
                if (i.name?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(context, "Dati nav atrasti", Toast.LENGTH_SHORT).show()
            } else {
                categoriesAdapter.setFilteredList(filteredList)
            }
        }
    }
}


