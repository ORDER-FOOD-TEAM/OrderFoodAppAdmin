package com.example.orderfoodappadmin

import android.content.Context
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

class CustomDialog(val context: Context) {
    var dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.dialog_loading_login)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

}