package io.github.moh_mohsin.ahoyweatherapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.State
import io.github.moh_mohsin.ahoyweatherapp.data.model.Settings
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale
import io.github.moh_mohsin.ahoyweatherapp.databinding.SettingsFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.DarkColors
import io.github.moh_mohsin.ahoyweatherapp.ui.LightColors
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.settings_fragment) {
//class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by viewModels<SettingsViewModel>()
    val binding by viewBinding(SettingsFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            Timber.d( "state: $state")
            when (state) {
                is State.Data -> {
                    binding.composeView.setContent {
                        Settings(state.data, viewModel)
                    }
                }
                else -> {}
            }
        }

    }


//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        setPreferencesFromResource(R.xml.root_preferences, rootKey)
//    }
//
//    override fun onPreferenceTreeClick(preference: Preference): Boolean {
//
//        val tempScaleKey = requireContext().resources.getString(R.string.temp_scale_key)
//        when (preference.key) {
//            tempScaleKey -> {
//                viewModel.cleanWeatherCache()
//            }
//            else -> {
//            }
//        }
//        return super.onPreferenceTreeClick(preference)
//    }
}

@Composable
fun Settings(settings: Settings, viewModel: SettingsViewModel) {
    MaterialTheme(if (isSystemInDarkTheme()) DarkColors else LightColors) {
        val openDialog = remember {
            mutableStateOf(false)
        }
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
//                    .padding(16.dp)
            ) {
                val value = when (settings.tempScale) {
                    TempScale.METRIC -> stringResource(id = R.string.temp_celsius)
                    TempScale.IMPERIAL -> stringResource(id = R.string.temp_fahrenheit)
                }
                SettingTile(title = stringResource(R.string.temp_scale_title), value = value) {
                    openDialog.value = true
                }
                if (openDialog.value) {
                    TempDialog(tempScale = settings.tempScale, onDismiss = {
                        openDialog.value = false
                    }) { tempScale ->
                        openDialog.value = false
                        viewModel.updateSettings(settings.copy(tempScale = tempScale))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsTilePreview() {
    SettingTile(title = "Temperature Scale", value = "Celsius") {
        //TODO: show dialog
    }
}

@Composable
fun SettingTile(title: String, value: String, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value)
    }
}

@Preview(showBackground = true)
@Composable
fun tempDialogPreview() {
    TempDialog(tempScale = TempScale.IMPERIAL, onDismiss = {}) {}
}

@Composable
fun TempDialog(tempScale: TempScale, onDismiss: () -> Unit, onSelect: (TempScale) -> Unit) {
    AlertDialog(
        modifier = Modifier.padding(4.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(id = R.string.temp_scale_title))
        },
        text = {


            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tempScale == TempScale.METRIC,
                        onClick = { onSelect(TempScale.METRIC) },
                    )
                    Text(text = stringResource(id = R.string.temp_celsius))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tempScale == TempScale.IMPERIAL,
                        onClick = { onSelect(TempScale.IMPERIAL) },
                    )
                    Text(text = stringResource(id = R.string.temp_fahrenheit))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
        properties = DialogProperties(),
    )
}