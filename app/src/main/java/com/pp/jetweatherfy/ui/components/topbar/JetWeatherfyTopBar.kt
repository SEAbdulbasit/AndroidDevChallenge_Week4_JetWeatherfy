/*
 * Copyright 2021 Paulo Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pp.jetweatherfy.ui.components.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.ContentState
import com.pp.jetweatherfy.domain.ContentState.Detailed
import com.pp.jetweatherfy.domain.ContentState.Simple
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.theme.BigDimension
import com.pp.jetweatherfy.ui.theme.MediumDimension
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun JetWeatherfyTopBar(viewModel: ForecastViewModel, state: JetWeatherfyState, contentState: ContentState, onSetMyLocationClick: () -> Unit) {
    val cities by viewModel.cities.observeAsState(listOf())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding()
            .padding(MediumDimension),
        verticalArrangement = Arrangement.spacedBy(BigDimension),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title()
        Actions(
            viewModel = viewModel,
            state = state,
            contentState = contentState,
            cities = cities,
            onSetMyLocationClick = onSetMyLocationClick
        )
    }
}

@Composable
private fun Title() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.app_name),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h2
    )
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun Actions(
    viewModel: ForecastViewModel,
    state: JetWeatherfyState,
    contentState: ContentState,
    cities: List<String>,
    onSetMyLocationClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        AnimatedVisibility(visible = state == Running) {
            ViewTypeToggle(viewModel = viewModel, contentState = contentState)
        }
        AnimatedVisibility(visible = state == Running || state == Idle) {
            MyLocationButton(onSetMyLocationClick = onSetMyLocationClick)
        }
        JetWeatherfySearchBar(viewModel = viewModel, cities = cities, state = state)
    }
}

@Composable
private fun ViewTypeToggle(viewModel: ForecastViewModel, contentState: ContentState) {
    IconToggleButton(
        checked = contentState == Detailed,
        onCheckedChange = {
            viewModel.setContentState(
                if (contentState == Simple) Detailed else Simple
            )
        },
        modifier = Modifier.padding(top = MediumDimension)
    ) {
        val icon =
            if (contentState == Detailed) R.drawable.ic_list else R.drawable.detailed_view
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(R.string.toggle_view_type),
            modifier = Modifier.requiredSize(
                BigDimension
            )
        )
    }
}

@Composable
fun MyLocationButton(onSetMyLocationClick: () -> Unit) {
    IconButton(
        onClick = { onSetMyLocationClick() },
        modifier = Modifier.padding(top = MediumDimension)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_my_location),
            contentDescription = stringResource(R.string.get_my_location)
        )
    }
}
