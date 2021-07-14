package com.example.fitness.util

import android.text.Editable
import android.widget.EditText

interface TextWatcherInterface {
    fun afterTextChanged(editText: EditText, s: Editable)
}