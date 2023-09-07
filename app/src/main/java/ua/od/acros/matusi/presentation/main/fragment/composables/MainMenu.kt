package ua.od.acros.matusi.presentation.main.fragment.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.od.acros.matusi.R

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    onFindGroupClicked: () -> Unit
) {
    val whiteColor = colorResource(id = R.color.white)
    val brandColor = colorResource(id = R.color.brand_color_primary)

    var showGroups by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier
                .clickable {

                }
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.check_request_status),
                style = MaterialTheme.typography.headlineSmall,
                color = whiteColor
            )
        }
        Row(
            modifier = modifier
                .clickable {
                    showGroups = !showGroups
                }
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.manage_groups),
                style = MaterialTheme.typography.headlineSmall,
                color = whiteColor
            )
        }
        AnimatedVisibility(
            visible = showGroups,
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.nav_header_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = brandColor
            )
        }
        Row(
            modifier = modifier
                .clickable {
                    onFindGroupClicked()
                }
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.find_user),
                style = MaterialTheme.typography.headlineSmall,
                color = whiteColor
            )
        }
        Spacer(modifier = modifier.weight(1f))
        Box(
            modifier = modifier
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