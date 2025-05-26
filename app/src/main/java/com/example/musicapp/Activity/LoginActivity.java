package com.example.musicapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.musicapp.db.DatabaseHolder;
import com.example.musicapp.R;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHolder userData;;
    Button Login_but;
    EditText ip_email, ip_password;
    TextView link;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Login_but = findViewById(R.id.button);
        ip_email = findViewById(R.id.txtEmail);
        ip_password = findViewById(R.id.txtPwd);
        link = findViewById(R.id.lnkDk);

        SharedPreferences preferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        if(preferences.getString("loggedInUserEmail",null)!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();;
        }
        userData = new DatabaseHolder(this);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
        Login_but.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = ip_email.getText().toString().trim();
                String password = ip_password.getText().toString();

                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Vui lòng nhap lại email và mật khẩu",Toast.LENGTH_SHORT).show();;
                }
                if(userData.checkUser(email,password)){
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loggedInUserEmail", email);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Không cần đóng database ở đây nếu DatabaseHelper tự quản lý kết nối
    }
}
