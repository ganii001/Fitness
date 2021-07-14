package com.example.fitness.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object MultiTextWatcher {
    private var textWatcherInterfaces: TextWatcherInterface? = null

    fun setCallback(textWatcherInterfaces: TextWatcherInterface?): MultiTextWatcher? {
        this.textWatcherInterfaces = textWatcherInterfaces
        return this
    }

    fun registerEditText(editText: EditText): MultiTextWatcher? {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                textWatcherInterfaces!!.afterTextChanged(editText, s)
            }
        })
        return this
    }
}