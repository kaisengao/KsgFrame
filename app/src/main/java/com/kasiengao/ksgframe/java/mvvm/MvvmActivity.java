package com.kasiengao.ksgframe.java.mvvm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.MvvmBinding;

public class MvvmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        User user = new User("123", "213213");
        binding.setUser(user);
    }
}
