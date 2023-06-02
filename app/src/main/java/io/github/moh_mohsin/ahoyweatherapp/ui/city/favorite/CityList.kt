package io.github.moh_mohsin.ahoyweatherapp.ui.city.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.ui.city.search.CityWithFavorite


@Composable
fun CityList(
    citiesWithFavorite: List<CityWithFavorite>,
    onAddFavorite: ((CityWithFavorite) -> Unit)? = null,
    onRemoveFavorite: ((CityWithFavorite) -> Unit)? = null,
    onClick: ((CityWithFavorite) -> Unit)? = null,
) {
    LazyColumn {
        items(citiesWithFavorite) { cityWithFavorite ->
            CityItem(
                name = cityWithFavorite.city.name,
                favorite = cityWithFavorite.favorite,
                onAddFavorite = {
                    onAddFavorite?.invoke(cityWithFavorite)
                },
                onRemoveFavorite = {
                    onRemoveFavorite?.invoke(cityWithFavorite)
                },
                onClick = {
                    onClick?.invoke(cityWithFavorite)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CityItemPreview() {
    CityItem(
        name = "Abu Dhabi (United Arab Emirates)",
        favorite = true,
        onAddFavorite = {},
        onRemoveFavorite = {},
        onClick = {},
    )
}

@Composable
fun CityItem(
    name: String,
    favorite: Boolean,
    onAddFavorite: (() -> Unit)?,
    onRemoveFavorite: (() -> Unit)?,
    onClick: (() -> Unit)?,
) {
    Column(modifier = Modifier
        .clickable(enabled = onClick != null) { onClick?.invoke() }
        .padding(start = 8.dp, top = 12.dp, end = 8.dp, bottom = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = name,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        enabled = if (favorite) onRemoveFavorite != null else onAddFavorite != null,
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(
                            bounded = false,
                        ),
                        onClick = { if (favorite) onRemoveFavorite?.invoke() else onAddFavorite?.invoke() },
                    )
                    .padding(horizontal = 8.dp),
                painter = painterResource(
                    id = if (favorite) R.drawable.ic_baseline_favorite
                    else R.drawable.ic_baseline_favorite_border
                ),
                contentDescription = stringResource(
                    id = R.string.content_desc_favorite_a_city
                ),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
    }
}
