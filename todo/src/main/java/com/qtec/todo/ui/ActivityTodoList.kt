package com.qtec.todo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qtec.todo.R
import com.qtec.todo.Todo
import com.qtec.todo.TodoModel
import kotlinx.android.synthetic.main.activity_todo_list.*

/**
 *
 * @author gongw
 * @date 2019/1/10
 */
class ActivityTodoList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)
        todoRecyclerView.addItemDecoration(DividerItemDecoration(this@ActivityTodoList, DividerItemDecoration.VERTICAL))
        todoRecyclerView.layoutManager = LinearLayoutManager(this@ActivityTodoList, LinearLayoutManager.VERTICAL, false)
        todoRecyclerView.adapter = TodoAdapter(TodoModel.getInstance().todoList)
    }

    inner class TodoAdapter(private val datas : List<Todo>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val view = LayoutInflater.from(this@ActivityTodoList).inflate(R.layout.adapter_todo_item, p0, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = datas[position].thingTodo
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tvTitle)
            val date: TextView = itemView.findViewById(R.id.tvDate)
            val week: TextView = itemView.findViewById(R.id.tvWeek)
            val time: TextView = itemView.findViewById(R.id.tvTime)
        }
    }
}