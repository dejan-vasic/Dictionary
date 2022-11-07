package com.we.dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.security.Key
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: WordAdapter
    private val data = ArrayList<Word>()
    private lateinit var noSearchResultsFoundText: TextView
    private lateinit var recyclerView: RecyclerView

    var clickCount = 0
    var clickCount2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val database = Firebase.database
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, World!")
        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val searchEditText = findViewById<AppCompatEditText>(R.id.search_edit_text)
        val imageView = findViewById<ImageView>(R.id.imageView2)
        val imageView2 = findViewById<ImageView>(R.id.imageView3)
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        recyclerView = findViewById(R.id.recyclerView)
        noSearchResultsFoundText = findViewById(R.id.no_search_results_found_text)


        recyclerView.layoutManager = LinearLayoutManager(this)

        if (text != null) {
            data.add((Word(text.toString())))
            attachAdapter(data)
            toggleRecyclerView(data)
        }

        floatingActionButton.setOnClickListener {
            data.add((Word(searchEditText.text.toString())))
            attachAdapter(data)
            toggleRecyclerView(data)
        }

        val watcher = searchEditText.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase(Locale.getDefault())
            filterWithQuery(query)
        }

        imageView.setOnClickListener {
            val filteredList = ArrayList<Word>()
            for (currentData in data) {
                if (!currentData.isWritten) {
                    filteredList.add(currentData)
                }
            }
            if (clickCount == 1) {
                attachAdapter(data)
                toggleRecyclerView(data)
                clickCount = 0
            } else {
                attachAdapter(filteredList)
                toggleRecyclerView(filteredList)
                clickCount++
            }
        }

//        var text = ""
        imageView2.setOnClickListener {
            if (clickCount2 == 1) {
                for (i in data.indices) {
                    data[i].isEditable = false
                    adapter.notifyItemChanged(i)
//                    searchEditText.setText(text)
//                    searchEditText.addTextChangedListener(watcher)
                }
                clickCount2 = 0
            } else {
                for (i in data.indices) {
                    data[i].isEditable = true
                    adapter.notifyItemChanged(i)
                }
//                searchEditText.removeTextChangedListener(watcher)
//                text = searchEditText.text.toString()
//                searchEditText.setText("")
                clickCount2++
            }

        }

//        searchEditText.setOnKeyListener { view, keyCode, keyEvent ->
//            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//
//                data.add(Word(searchEditText.text.toString(), ""))
//            }
//            false
//        }
    }

//    override fun onStop() {
//        super.onStop()
//        data.clear()
//    }

    private fun attachAdapter(data: ArrayList<Word>) {
        adapter = WordAdapter(data)
        adapter.setContext(this)
        recyclerView.adapter = adapter
    }

    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList: ArrayList<Word> = onQueryChanged(query)
            attachAdapter(filteredList)
            toggleRecyclerView(filteredList)
        } else if (query.isEmpty()) {
            attachAdapter(data)
            toggleRecyclerView(data)
        }
    }

    private fun onQueryChanged(filterQuery: String): ArrayList<Word> {
        val filteredList = ArrayList<Word>()
        for (currentData in data) {
            if (currentData.text.lowercase(Locale.getDefault()).contains(filterQuery)) {
                filteredList.add(currentData)
            }
        }
        return filteredList
    }

    private fun toggleRecyclerView(data: ArrayList<Word>) {
        if (data.isEmpty()) {
            recyclerView.visibility = View.INVISIBLE
            noSearchResultsFoundText.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noSearchResultsFoundText.visibility = View.INVISIBLE
        }
    }
}