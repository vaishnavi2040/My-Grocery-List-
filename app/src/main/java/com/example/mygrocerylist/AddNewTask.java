package com.example.mygrocerylist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddNewTask extends BottomSheetDialogFragment {
 //  public static final String TAG="ActionBottomDialog";

   private EditText mTaskEdit;
   private Button mSaveBtn;

   private FirebaseFirestore firestore;
   private Context context;
   public static final String TAG="AddNewTask";
    private String id = "";
public static AddNewTask newInstance(){
    return new AddNewTask();

}
@Override
    public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NORMAL,R.style.DialogStyle);
}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);
       // Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(SOFT_INPUT_MODE_CHANGED);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTaskEdit= getView().findViewById(R.id.newTask);
        mSaveBtn = getView().findViewById(R.id.newtask_button);
firestore=FirebaseFirestore.getInstance();

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("id");
          //  dueDateUpdate = bundle.getString("due");

            mTaskEdit.setText(task);
           // setDueDate.setText(dueDateUpdate);

            if (task.length() > 0){
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }
        }



        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             if(charSequence.toString().equals("")){
                 mSaveBtn.setEnabled(false);
                 mSaveBtn.setBackgroundColor(Color.GRAY);

           }
             else
             {
               mSaveBtn.setEnabled(true);
                mSaveBtn.setBackgroundColor(getResources().getColor(R.color.white));
             }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String task =mTaskEdit.getText().toString();
             if(finalIsUpdate) {
                 firestore.collection("task").document(id).update("task" , task);
                 Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show();

             }
             else{
             if(task.isEmpty()){
                 Toast.makeText(context, "Empty item ", Toast.LENGTH_SHORT).show();

             }
             else
             {
                 Map<String, Object> taskMap=new HashMap<>();
                 taskMap.put("task",task);
                 taskMap.put("status",0);

                 taskMap.put("time", FieldValue.serverTimestamp());
                 firestore.collection("task").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                     @Override
                     public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
if(task.isSuccessful()){
    Toast.makeText(context,"item Saved",Toast.LENGTH_SHORT).show();}
else{
    Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
}
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull @NotNull Exception e) {
                         Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();

                     }
                 });

             }    } dismiss();

            }
        });

}
@Override
public void onAttach(@NonNull Context context)
{
    super.onAttach(context);
    this.context=context;
}
@Override
public void onDismiss(@NonNull DialogInterface dialog){
   super.onDismiss(dialog);
    Activity activity = getActivity();
    if(activity instanceof DialogCloseListener)
        ((DialogCloseListener)activity).onDialogClose(dialog);}



}
