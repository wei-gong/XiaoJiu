package com.qtec.todo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/10
 */
public class TodoModel {

    private List<Todo> cachedTodoList;

    private static class InstanceHolder{
        private static TodoModel instance = new TodoModel();
    }

    public static TodoModel getInstance(){
        return InstanceHolder.instance;
    }

    private TodoModel(){
        cachedTodoList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Todo todo = new Todo.Builder().timeTodo(System.currentTimeMillis())
                    .thingsTodo("提醒" + i)
                    .build();
            cachedTodoList.add(todo);
        }
    }

    public List<Todo> getTodoList(){
        return cachedTodoList;
    }

    public void deleteTodo(Todo todo){

    }

    public void addTodo(Todo todo){
        cachedTodoList.add(todo);
    }


}
