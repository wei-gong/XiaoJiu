package com.qtec.todo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.qtec.common.base.BaseAdapter
import com.qtec.common.base.BaseViewHolder
import com.qtec.todo.R
import com.qtec.todo.Todo
import com.qtec.todo.TodoModel
import kotlinx.android.synthetic.main.activity_todo_list.*
import java.text.SimpleDateFormat
import java.util.*

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
        todoRecyclerView.adapter = TodoAdapter(this@ActivityTodoList, R.layout.adapter_todo_item, TodoModel.getInstance().todoList)
    }

    inner class TodoAdapter(context: Context?, layoutRes: Int, datas: MutableList<Todo>?) : BaseAdapter<Todo>(context, layoutRes, datas) {
        @SuppressLint("SimpleDateFormat")
        override fun convertView(viewHolder: BaseViewHolder?, t: Todo?) {
            viewHolder?.getView<TextView>(R.id.tvTitle)?.text = t?.thingTodo
            viewHolder?.getView<TextView>(R.id.tvDate)?.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(t?.timeTodo?.let { Date(it) })
        }
    }

}