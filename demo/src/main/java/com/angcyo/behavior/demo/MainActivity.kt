package com.angcyo.behavior.demo

import android.os.Bundle
import com.angcyo.activity.BaseAppCompatActivity
import com.angcyo.base.dslFHelper

class MainActivity : BaseAppCompatActivity() {

    override fun onCreateAfter(savedInstanceState: Bundle?) {
        super.onCreateAfter(savedInstanceState)
        dslFHelper {
            restore(MainFragment::class.java)
        }
    }
}
