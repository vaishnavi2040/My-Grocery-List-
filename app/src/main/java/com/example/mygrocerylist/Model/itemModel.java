package com.example.mygrocerylist.Model;
public class itemModel extends com.example.mygrocerylist.Model.TaskId {
    private int status;
    private  String task;



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public  String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
