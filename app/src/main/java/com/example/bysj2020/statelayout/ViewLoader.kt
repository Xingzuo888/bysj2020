package com.example.bysj2020.statelayout

import android.view.View

/**
 *    Author : wxz
 *    Time   : 2020/02/15
 *    Desc   :
 */
abstract class ViewLoader {
    fun getView(): View {
        return createView()
    }

    abstract fun createView(): View
}