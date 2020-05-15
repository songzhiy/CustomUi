package com.szy.kotlincustomui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.szy.kotlincustomui.customview.CustomViewActivity
import com.szy.kotlincustomui.lightview.LightCustomViewActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_custom_view).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_light_view).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_custom_view -> startActivity(Intent(this, CustomViewActivity::class.java))
            R.id.tv_light_view -> startActivity(Intent(this, LightCustomViewActivity::class.java))
        }
    }
}
