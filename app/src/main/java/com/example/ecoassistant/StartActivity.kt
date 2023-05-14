package com.example.ecoassistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // Пользователь уже авторизован, перенаправляем на главный экран
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val isFirstLaunch = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstLaunch", true)

            if (isFirstLaunch) {
                // Это первый запуск приложения, отображаем экран выбора регистрации или авторизации
                setContentView(R.layout.activity_start)
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstLaunch", false).apply()
                val signInButton: Button = findViewById(R.id.SignInButton)

                signInButton.setOnClickListener {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                }

                val signUpButton: Button = findViewById(R.id.SignUpButton)
                signUpButton.setOnClickListener {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                }
            } else {
                // Первый запуск уже был, но пользователь не авторизован, поэтому перенаправляем на экран авторизации
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }
    }
}


