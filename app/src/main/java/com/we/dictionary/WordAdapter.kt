package com.we.dictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class WordAdapter(private val data: ArrayList<Word>): RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    private lateinit var mainActivity: MainActivity

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val editText: TextView = itemView.findViewById(R.id.edit_text)
        val pen: ImageView = itemView.findViewById(R.id.pen)
        val bin: ImageView = itemView.findViewById(R.id.bin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wordData = data[position]
        val word = wordData.text
        holder.textView.text = word
        holder.checkBox.isChecked = wordData.isWritten == true
        holder.checkBox.setOnClickListener {
            wordData.isWritten = holder.checkBox.isChecked
            if (holder.checkBox.isChecked) {
                mainActivity.clickCount = 0
            } else {
                mainActivity.clickCount = 1
            }
        }

        if (wordData.isEditable) {
            holder.pen.visibility = View.VISIBLE
            holder.bin.visibility = View.VISIBLE
            var clickCount = 0
            holder.pen.setOnClickListener {
                if (clickCount == 1) {
                    holder.editText.visibility = View.GONE
                    holder.textView.text = holder.editText.text
                    holder.textView.visibility = View.VISIBLE
                    wordData.text = holder.editText.text.toString()
                    clickCount = 0
                } else {
                    holder.textView.visibility = View.GONE
                    holder.editText.text = holder.textView.text
                    holder.editText.visibility = View.VISIBLE
                    clickCount++
                }
            }
            holder.bin.setOnClickListener {
                mainActivity.removeFromDatabase(position)
                data.removeAt(position)
                this.notifyItemRemoved(position)
                this.notifyItemRangeChanged(position, data.size)
                mainActivity.addToDatabase(data)
            }
        }
//        val imageView2 = mainActivity.findViewById<ImageView>(R.id.imageView3)
//        imageView2.setOnClickListener {
//            holder.imageView.visibility = View.VISIBLE
//        }
//        if (mainActivity.clickCount2 == 1) {
//            holder.imageView.visibility = View.VISIBLE
//        }

//        holder.textView.setOnClickListener {
//            if (mainActivity.clickCount2 == 1) {
//                holder.textView.visibility = View.GONE
//                holder.editText.visibility = View.VISIBLE
//            } else {
//                holder.textView.visibility = View.VISIBLE
//                holder.editText.visibility = View.GONE
//            }
//        }
    }

    override fun getItemCount() = data.size

    fun setContext(mainActivity: MainActivity) {
        this.mainActivity = mainActivity
    }
}