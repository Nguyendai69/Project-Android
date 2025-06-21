package com.example.musicapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.musicapp.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameEditText = findViewById(R.id.txtEmail);
        EditText passwordEditText = findViewById(R.id.txtPwd);
        Button loginButton = findViewById(R.id.button);
        TextView registerLink = findViewById(R.id.lnkDk);
        // Đổi màu cho dòng chữ đăng ký để nổi bật
        registerLink.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        registerLink.setPaintFlags(registerLink.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            // Demo: accept any non-empty username/password
            if (!username.isEmpty() && !password.isEmpty()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
