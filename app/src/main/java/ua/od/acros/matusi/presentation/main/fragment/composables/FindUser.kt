package ua.od.acros.matusi.presentation.main.fragment.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.od.acros.matusi.R

@Preview
@Composable
fun FindUser(
    modifier: Modifier = Modifier
) {

    val textColor = colorResource(id = R.color.black)

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.find_user_title),
                color = textColor
            )
        }
    }
}