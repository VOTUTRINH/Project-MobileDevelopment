package com.example.oderingfood;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class a2g18Activity extends Activity {
    Activity activity;
    private Button buttonOpenDialog;
    private TextView txtName;
    private TextView txtID;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtAddress;
    private TextView txtSalary;
    private TextView txtDebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2g18activity_main);
//
        this.buttonOpenDialog = (Button) this.findViewById(R.id.afo_txtEdit);

        this.buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOpenDialogClicked();
            }
        });
    }

    private void buttonOpenDialogClicked() {

        TextView name = this.findViewById(R.id.afo_txtName);
        TextView id = this.findViewById(R.id.afo_txtId);
        TextView phone1 = this.findViewById(R.id.afo_txtPhone);
        TextView email1 = this.findViewById(R.id.afo_txtEmail);
        TextView address1 = this.findViewById(R.id.afo_txtAddress);
        TextView salary1 = this.findViewById(R.id.afo_txtSalary);
//        TextView debt1 = this.findViewById(R.id.afo_txtDebt);
        CustomDialog.EmployeeListener listener = new CustomDialog.EmployeeListener() {
            @Override
            public void fullNameEntered(String fullName) {
                name.setText("Họ và tên: " + fullName);
            }

            public void IDEntered(String ID) {
                id.setText("ID: " + ID);
            }

            public void phoneEntered(String phone) {
                phone1.setText("Số điện thoại: " + phone);
            }

            public void emailEntered(String email) {
                email1.setText("Email: " + email);
            }

            public void addressEntered(String address) {
                address1.setText("Địa chỉ: " + address);
            }

            public void salaryEntered(String salary) {
                salary1.setText("Lương theo giờ: " + salary);
            }

//            public void debtEntered(String debt) {
//                debt1.setText("Tổng tiền lương còn nợ: " + debt);
//            }
        };
        final CustomDialog dialog = new CustomDialog(this, listener);

        dialog.show();
    }
}
