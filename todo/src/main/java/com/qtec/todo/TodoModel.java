package com.qtec.todo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/10
 */
public class TodoModel {

    private static class InstanceHolder{
        private static TodoModel instance = new TodoModel();
    }

    public static TodoModel getInstance(){
        return InstanceHolder.instance;
    }

    public List<Todo> getTodoList(){
        List<Todo> todoList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Todo todo = new Todo.Builder().timeTodo(System.currentTimeMillis())
                                            .thingsTodo("提醒" + i)
                                            .build();
            todoList.add(todo);
        }
        return todoList;
    }

    public void deleteTodo(Todo todo){

    }


}
