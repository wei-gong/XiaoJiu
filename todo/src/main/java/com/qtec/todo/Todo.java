package com.qtec.todo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/10
 */
public class Todo {

    private String creator;
    private long createTime;
    private List<String> receivers;
    private long timeTodo;
    private String thingTodo;
    private boolean finished;
    private String state;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public void addReceive(String receiver){
        if(receivers == null){
            receivers = new ArrayList<>();
        }
        receivers.add(receiver);
    }

    public void removeReceiver(String receiver){
        if(receivers != null){
            receivers.remove(receiver);
        }
    }

    public long getTimeTodo() {
        return timeTodo;
    }

    public void setTimeTodo(long timeTodo) {
        this.timeTodo = timeTodo;
    }

    public String getThingTodo() {
        return thingTodo;
    }

    public void setThingTodo(String thingTodo) {
        this.thingTodo = thingTodo;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static class Builder {
        private Todo todo = new Todo();

        public Builder creator(String creator){
            todo.setCreator(creator);
            return this;
        }

        public Builder createTime(long createTime){
            todo.setCreateTime(createTime);
            return this;
        }

        public Builder receivers(List<String> creators){
            todo.setReceivers(creators);
            return this;
        }

        public Builder timeTodo(long timeTodo){
            todo.setTimeTodo(timeTodo);
            return this;
        }

        public Builder thingsTodo(String thingTodo){
            todo.setThingTodo(thingTodo);
            return this;
        }

        public Builder finished(boolean finished){
            todo.setFinished(finished);
            return this;
        }

        public Builder state(String state){
            todo.setState(state);
            return this;
        }

        public Todo build(){
            return todo;
        }
    }


}
