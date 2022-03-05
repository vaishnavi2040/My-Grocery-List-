package com.example.mygrocerylist.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerylist.AddNewTask;
import com.example.mygrocerylist.MainActivity;
import com.example.mygrocerylist.Model.itemModel;
import com.example.mygrocerylist.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.MyViewHolder> {
   private List<itemModel> itemList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public itemAdapter(MainActivity mainActivity,List<itemModel> itemList){
        this.itemList=itemList;
        activity=mainActivity;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.task_layout, parent, false);
         firestore =FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }
    public void deleteTask(int position){
        itemModel toDoModel = itemList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        itemList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public void editTask(int position){
        itemModel toDoModel = itemList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task" , toDoModel.getTask());
       // bundle.putString("due" , toDoModel.getDue());
        bundle.putString("id" , toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        itemModel toDoModel = itemList.get(position);
        holder.checkBox.setText(toDoModel.getTask());


        holder.checkBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 1);
                }else{
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 0);
                }
            }
        });
    }
    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
     CheckBox checkBox;
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      checkBox=itemView.findViewById(R.id.todoCheckBox);
    }}}

