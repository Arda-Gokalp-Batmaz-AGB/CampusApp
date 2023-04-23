package com.arda.campuslink.util

import com.arda.campuslink.App

class LangStringUtil {
    companion object{
        fun getLangString(id : Int): String {
            return App.context.resources.getString(id)
        }
    }
}