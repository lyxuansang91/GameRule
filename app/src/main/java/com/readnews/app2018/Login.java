package com.readnews.app2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.readnews.app2018.common.Utils;
import com.readnews.app2018.event.LoginEvent;
import com.readnews.com.readnews.app2018.R;

import de.greenrobot.event.EventBus;


public class Login extends AppCompatActivity {


    EditText edtPhone;
    Button btnSubmit;


    private void initUI() {
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edtPhone.setHint(MyApplication.getInstance().sharedPreferences.getString("phone", ""));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtPhone.getText().toString();
                if (Utils.isVNPhoneNumber(phone)) {
                    MyApplication.getInstance().editor.putString("phone", phone);
                    MyApplication.getInstance().editor.commit();
                    EventBus.getDefault().post(new LoginEvent(phone));
                    finish();
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
        initUI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
