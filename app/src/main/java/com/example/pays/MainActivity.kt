package com.example.pays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pays.model.Country
import com.example.pays.ui.theme.PaysTheme
import com.example.pays.R
import com.example.pays.data.DataSource
import com.example.pays.ui.theme.WelcomeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaysTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "welcome") {

                    composable("welcome") {
                        WelcomeScreen(onExploreClick = {
                            navController.navigate("pays_list")
                        })
                    }

                    composable("pays_list") {
                        PaysApp()
                    }
                }
            }
        }
    }
}


@Composable
fun WelcomeScreen(onExploreClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.globe),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Karibu",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Découvrez les nations du monde",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onExploreClick,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Voir les pays", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}



@Composable
fun PaysApp() {
    Scaffold(
        topBar = { PaysTopAppBar() }
    ) { it ->
        LazyColumn(contentPadding = it) {
            items(DataSource.countries) { country ->
                CountryItem(
                    country = country,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun CountryItem(country: Country, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountryIcon(country.drapeauResId)
                CountryInformation(country.nomResId, country.code)
                Spacer(Modifier.weight(1f))
                CountryItemButton(expanded = expanded, onClick = { expanded = !expanded })
            }
            if (expanded) {
                CountryDetail(
                    capitalResId = country.capitaleResId,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CountryIcon(@DrawableRes flagResId: Int) {
    Image(
        painter = painterResource(flagResId),
        contentDescription = null,
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CountryInformation(@StringRes nameResId: Int, code: String) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(
            text = stringResource(nameResId),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Code: $code",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CountryDetail(@StringRes capitalResId: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Capitale: " + stringResource(capitalResId),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun CountryItemButton(expanded: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaysTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.image_size))
                            .padding(dimensionResource(id = R.dimen.padding_small)),
                        painter = painterResource(R.drawable.globe),
                        contentDescription = null
                    )
                Text(text = "M'bokas ", style = MaterialTheme.typography.displayLarge)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PaysTheme() {
        WelcomeScreen(onExploreClick = {})
    }
}