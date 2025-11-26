package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()

        val totalData = db.totalDao().getTotal(ID)
        if (!totalData.isEmpty()) {
            val lastDate = totalData.first().total.date
            Toast.makeText(this, lastDate, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this, Observer { total ->
            if (total != null) updateText(total)
        })

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            db.totalDao().insert(
                Total(
                    id = ID,
                    total = TotalObject(
                        value = 0,
                        date = Date().toString()
                    )
                )
            )
        } else {
            viewModel.setTotal(total.first().total.value)
        }
    }

    override fun onPause() {
        super.onPause()

        db.totalDao().update(
            Total(
                id = ID,
                total = TotalObject(
                    value = viewModel.total.value ?: 0,
                    date = Date().toString()
                )
            )
        )
    }

    companion object {
        const val ID: Long = 1
    }
}
