/**
 * Course: MAD204 - Mobile App Development
 * Lab 3 - Persistent Notes App
 * Author: Your Name (Student ID: XXXXXXX)
 * Date: October 2025
 * Description:
 * MainActivity handles displaying, adding, deleting, and persisting notes using RecyclerView and SharedPreferences.
 */

package com.example.lab3notesapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var noteInput: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private val notes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteInput = findViewById(R.id.noteInput)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.recyclerView)

        // Load saved notes
        loadNotes()

        adapter = MyAdapter(notes) { position -> deleteNote(position) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val note = noteInput.text.toString().trim()
            if (note.isNotEmpty()) {
                notes.add(note)
                adapter.notifyItemInserted(notes.size - 1)
                noteInput.text.clear()
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter a note first!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** Delete note with Snackbar UNDO */
    private fun deleteNote(position: Int) {
        val deletedNote = notes[position]
        notes.removeAt(position)
        adapter.notifyItemRemoved(position)

        Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                notes.add(position, deletedNote)
                adapter.notifyItemInserted(position)
            }.show()
    }

    /** Save notes list to SharedPreferences */
    override fun onPause() {
        super.onPause()
        saveNotes()
    }

    private fun saveNotes() {
        val sharedPref = getSharedPreferences("notes_pref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(notes)
        editor.putString("notes_list", json)
        editor.apply()
    }

    private fun loadNotes() {
        val sharedPref = getSharedPreferences("notes_pref", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("notes_list", null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        if (json != null) {
            val savedNotes: MutableList<String> = gson.fromJson(json, type)
            notes.addAll(savedNotes)
        }
    }
}
