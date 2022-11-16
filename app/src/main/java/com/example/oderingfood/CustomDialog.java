package com.example.oderingfood;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialog extends Dialog {
    interface EmployeeListener {
        public void fullNameEntered(String fullName);
        public void IDEntered(String ID);
        public void phoneEntered(String phone);
        public void emailEntered(String email);
        public void addressEntered(String address);
        public void salaryEntered(String salary);
        public void debtEntered(String debt);
    }

    public Context context;

    private EditText editTextFullName;
    private EditText editTextID;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextAddress;
    private EditText editTextSalary;
    private EditText editTextDebt;
    private Button buttonOK;
    private Button buttonCancel;

    private CustomDialog.EmployeeListener listener;

    public CustomDialog(Context context, CustomDialog.EmployeeListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_employee);

        this.editTextFullName = (EditText) findViewById(R.id.df_name);
        this.editTextID = (EditText) findViewById(R.id.df_id);
        this.editTextPhone = (EditText) findViewById(R.id.df_phone);
        this.editTextEmail = (EditText) findViewById(R.id.df_email);
        this.editTextAddress = (EditText) findViewById(R.id.df_address);
        this.editTextSalary = (EditText) findViewById(R.id.df_salary);
        this.editTextDebt = (EditText) findViewById(R.id.df_debt);
        this.buttonOK = (Button) findViewById(R.id.df_btnOk);
        this.buttonCancel  = (Button) findViewById(R.id.df_btnCancel);

        this.buttonOK .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOKClick();
            }
        });
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClick();
            }
        });
    }

    // User click "OK" button.
    private void buttonOKClick()  {
        String fullName = this.editTextFullName.getText().toString();

        if(fullName== null || fullName.isEmpty())  {
            Toast.makeText(this.context, "Please enter your name", Toast.LENGTH_LONG).show();
            return;
        }

        String id = this.editTextID.getText().toString();

        if(id == null || id.isEmpty())  {
            Toast.makeText(this.context, "Please enter id", Toast.LENGTH_LONG).show();
            return;
        }
        this.dismiss(); // Close Dialog

        String phone = this.editTextPhone.getText().toString();

        if(phone== null || phone.isEmpty())  {
            Toast.makeText(this.context, "Please enter phone", Toast.LENGTH_LONG).show();
            return;
        }
        String email = this.editTextEmail.getText().toString();

        if(email== null || email.isEmpty())  {
            Toast.makeText(this.context, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        String address = this.editTextAddress.getText().toString();

        if(address== null || address.isEmpty())  {
            Toast.makeText(this.context, "Please enter address", Toast.LENGTH_LONG).show();
            return;
        }
        String salary = this.editTextSalary.getText().toString();

        if(salary== null || salary.isEmpty())  {
            Toast.makeText(this.context, "Please enter salary", Toast.LENGTH_LONG).show();
            return;
        }
        String debt = this.editTextDebt.getText().toString();

        if(debt== null || debt.isEmpty())  {
            Toast.makeText(this.context, "Please enter debt", Toast.LENGTH_LONG).show();
            return;
        }
        if(this.listener!= null)  {
            this.listener.fullNameEntered(fullName);
            this.listener.IDEntered(id);
            this.listener.phoneEntered(phone);
            this.listener.emailEntered(email);
            this.listener.addressEntered(address);
            this.listener.salaryEntered(salary);
            this.listener.debtEntered(debt);
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }
}
