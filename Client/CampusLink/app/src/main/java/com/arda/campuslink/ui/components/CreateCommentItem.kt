package com.arda.campuslink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.arda.campuslink.R
import com.arda.campuslink.ui.screens.commentscreen.CommentUiState
import com.arda.campuslink.ui.screens.commentscreen.CommentViewModel
import com.arda.campuslink.ui.screens.publishscreen.PublishUiState
import com.arda.campuslink.ui.screens.publishscreen.PublishViewModel
import com.arda.campuslink.util.LangStringUtil
import com.google.accompanist.insets.LocalWindowInsets

@Composable
fun CreateCommentItem(commentViewmodel: CommentViewModel, state: CommentUiState) {
    postTextArea(commentViewmodel,state)
}
@Composable
fun postTextArea(commentViewmodel: CommentViewModel, state: CommentUiState) {
    val insets = LocalWindowInsets.current

    val imeBottom = with(LocalDensity.current) { insets.ime.bottom.toDp() }
    Card(
        modifier = Modifier.systemBarsPadding().padding(bottom = imeBottom).navigationBarsPadding().systemBarsPadding(),
        backgroundColor = MaterialTheme.colors.secondaryVariant
    ) {
        Row( modifier = Modifier.fillMaxWidth())
        {
            TextField(
             value = state.description,
                label = {
                    Text(LangStringUtil.getLangString(R.string.comment_hint))
                },
                onValueChange = { commentViewmodel.updateDescription(it) })
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { commentViewmodel.createNewComment() },
                shape = RoundedCornerShape(15.dp),
            ) {
                Text(LangStringUtil.getLangString(R.string.comment))
            }
        }

    }
}
