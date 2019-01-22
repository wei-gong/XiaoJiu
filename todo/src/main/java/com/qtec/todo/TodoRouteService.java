package com.qtec.todo;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qtec.router.provider.ITodoProvider;

/**
 * @author gongw
 * @date 2019/1/18
 */
@Route(path = ITodoProvider.PATH_TODO_SERVICE, name = "todo")
public class TodoRouteService implements ITodoProvider {

    private Context context;

    @Override
    public void goTodoListActivity(Activity activity) {
        Toast.makeText(context, "123", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goTodoInfoActivity(Activity activity) {

    }

    @Override
    public void addTodo(String json) {

//        TodoModel.getInstance().addTodo(todo);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
