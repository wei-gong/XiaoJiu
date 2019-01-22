package com.qtec.router.provider;



import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author gongw
 * @date 2019/1/18
 */
public interface ITodoProvider extends IProvider {

    String PATH_TODO_SERVICE = "/todo/service";

    void goTodoListActivity(Activity activity);

    void goTodoInfoActivity(Activity activity);

    void addTodo(String json);
}
