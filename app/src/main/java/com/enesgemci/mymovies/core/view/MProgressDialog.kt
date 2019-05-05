package com.enesgemci.mymovies.core.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.enesgemci.mymovies.R

class MProgressDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = layoutInflater.inflate(R.layout.layout_loading_view, null)
        setCanceledOnTouchOutside(false)
        setCancelable(false)

        setContentView(view)
    }
}
