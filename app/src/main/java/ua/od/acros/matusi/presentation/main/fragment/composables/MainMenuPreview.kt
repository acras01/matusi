package ua.od.acros.matusi.presentation.main.fragment.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.od.acros.matusi.R

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainMenuPreview() {
    val brandColor = colorResource(id = R.color.brand_color_primary)
    val whiteColor = colorResource(id = R.color.white)

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .background(brandColor)
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.ic_main_check_status),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.check_request_status),
                style = MaterialTheme.typography.headlineSmall,
                color = whiteColor
            )
        }
        Row(
            modifier = Modifier
                .background(brandColor)
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.ic_main_settings),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.manage_groups),
                style = MaterialTheme.typography.headlineSmall,
                color = whiteColor
            )
        }
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(
                // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                initialAlpha = 0.4f
            ),
            exit = fadeOut(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.nav_header_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = brandColor
            )
        }
        Row(
            modifier = Modifier
                .background(brandColor)
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.ic_main_find_user),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.find_user),
                style = MaterialTheme.typography.headlineSmall,
                color = whiteColor
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Image(
                painterResource(R.drawable.banner_daniele_levis_pelusi),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}