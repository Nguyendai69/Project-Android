package com.example.musicapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.R;
import com.example.musicapp.db.DatabaseHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText sname, semail, passwd;
    Button btn;
    TextView linkLogin;
    private DatabaseHolder userData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        sname = findViewById(R.id.txtName);
        semail = findViewById(R.id.txtSEmail);
        passwd = findViewById(R.id.txtSPwd);
        btn = findViewById(R.id.button2);
        userData = new DatabaseHolder(this);
        linkLogin = findViewById(R.id.lnkid);

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String name = sname.getText().toString().trim();
               String email = semail.getText().toString().trim();
               String password = passwd.getText().toString();
               Log.d("RegisterActivity", "Bắt đầu đăng ký với: Tên=" + name + ", Email=" + email);
               if(name.isEmpty()){
                   Toast.makeText(RegisterActivity.this,"Xin nhập lại tên",Toast.LENGTH_SHORT).show();
               }
               if(email.isEmpty()){
                   Toast.makeText(RegisterActivity.this,"Xin nhập lại mail",Toast.LENGTH_SHORT).show();
               }
               if(password.isEmpty()){
                   Toast.makeText(RegisterActivity.this,"Xin nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
               }
               if (!emailValidator(email)) {
                   Toast.makeText(RegisterActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                   return;
               }
               Log.d("RegisterActivity", "Dữ liệu hợp lệ, kiểm tra email tồn tại.");
               if(userData.checkUserExist(email)){
                   Toast.makeText(RegisterActivity.this,"Email đã tồn tại",Toast.LENGTH_SHORT).show();
                   Log.d("RegisterActivity","Email"+email+" đã tồn tại.");
               }else{
                   Log.d("RegisterActivity","Email"+email+" chưa tồn tại. Bắt đầu đăng ký");
                   long result = userData.addUser(email, name, password);
                   Log.d("RegisterActivity","Kết quả "+result);
                   if(result != -1){
                       Toast.makeText(RegisterActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                       startActivity(intent);
                       finish();
                   }else{
                       Toast.makeText(RegisterActivity.this,"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                       Log.e("RegisterActivity","lỗi chèn dữ liệu");
                   }
               }

           }

       });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Không cần đóng database ở đây nếu DatabaseHelper tự quản lý kết nối
    }
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
