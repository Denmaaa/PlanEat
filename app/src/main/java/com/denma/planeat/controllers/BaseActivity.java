package com.denma.planeat.controllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    // --------------------
    // LIFE CYCLE
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getActivityLayout());
        ButterKnife.bind(this);
    }

    // --------------------
    // GETTERS
    // --------------------

    protected abstract int getActivityLayout();

}
