package com.arda.campuslink.util

import com.arda.mainapp.App

class LangStringUtil {
    companion object{
        fun getLangString(id : Int): String {
            return App.context.resources.getString(id)
        }
    }
}