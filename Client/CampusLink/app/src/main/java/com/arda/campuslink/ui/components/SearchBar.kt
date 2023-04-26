package com.arda.campuslink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.arda.campuslink.R
import com.arda.campuslink.util.LangStringUtil

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .width(280.dp)
            .height(40.dp)
            .clip(shape = RoundedCornerShape(2.dp))
            .background(color = MaterialTheme.colors.secondaryVariant)
    ) {
        Icon(
            Icons.Filled.Search,
            tint = Color.Black,
            modifier = Modifier
                .width(24.dp)
                .padding(start = 4.dp, top = 10.dp),
            contentDescription = "messages icon",
        )
        Text(
            text = LangStringUtil.getLangString(R.string.search),
            style = TextStyle(color = Color.Black),
            modifier = Modifier.padding(start = 8.dp, top = 10.dp)
        )
    }
}