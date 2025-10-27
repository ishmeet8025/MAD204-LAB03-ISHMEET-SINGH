/**
Implemented RecyclerView and custom adapter for displaying notes.
 * Course: MAD204 - Mobile App Development
 * Lab 3 - Persistent Notes App
 * Author:  Ishmeet Singh (Student ID: A00202436)
 * Description:
 * RecyclerView Adapter for displaying a list of notes.
 */

package com.example.lab3notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val notes: MutableList<String>,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteText: TextView = itemView.findViewById(R.id.noteText)
        init {
            itemView.setOnLongClickListener {
                onLongClick(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.noteText.text = notes[position]
    }

    override fun getItemCount(): Int = notes.size
}
