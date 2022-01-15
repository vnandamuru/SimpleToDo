package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //REMOVE ITEMS FROM LISTS
                listOfTasks.removeAt(position)
                //Notify the adapter that data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()


        //Lookup recycler view layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create an adapter to pass the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //setup the button and the input field
        findViewById<Button>(R.id.button).setOnClickListener {
            //Code here is executed when user clicks button
            //Log.i("Kittu","Clicked a button")
            val userInputtedTask = inputTextField.text.toString()

            listOfTasks.add(userInputtedTask)

            //Notify the adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            inputTextField.setText("")

            saveItems()
        }

    }

    //Save the data that user has input
    //Save data by writing and reading into a file


    //Create a method to get the file we need
    fun getDataFile(): File {
        return File(filesDir,"data.txt")
    }

    //Load items by reading every line in data file
    fun loadItems() {
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(
                getDataFile(),
                Charset.defaultCharset().toString()
            ) as MutableList<String>
        }
        catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        }
        catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }
}