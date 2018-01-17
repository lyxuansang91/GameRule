package com.readnews.app2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.readnews.com.readnews.app2018.R;


public class Login extends AppCompatActivity {


    EditText edtPhone;
    Button btnSubmit;

    public boolean isVNPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }

        //String regex = "^(0|84)(9\\d|16[2-9]|12\\d|86|88|89|186|188|199)(\\d{7})$";

        if (phone.startsWith("0"))
            phone = phone.replaceFirst("0", "");
        else if (phone.startsWith("84"))
            phone = phone.replaceFirst("84", "");

        String regex = "^(9\\d|16[2-9]|12\\d|86|88|89|186|188|199)(\\d{7})$";

        phone = phone.replace("+", "");
        if (phone.matches(regex)) {
            return true;
        }
        return false;
    }

    private void initUI() {
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtPhone.getText().toString();
                if (isVNPhoneNumber(phone)) {
                    MyApplication.getInstance().editor.putString("phone", phone);
                    MyApplication.getInstance().editor.commit();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String phone = MyApplication.getInstance().sharedPreferences.getString("phone", "");
        if(!phone.isEmpty() && isVNPhoneNumber(phone)) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
        initUI();
    }
}
