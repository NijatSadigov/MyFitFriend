package com.example.myfitfriend.presentation.dietarylogs.Foods

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfitfriend.data.remote.reponses.FoodResponse
import com.example.myfitfriend.util.Screen
import com.example.myfitfriend.presentation.dietarylogs.DietaryLogsBottomBar


@Composable
fun ShowFoodsScreen(navController: NavController, viewModel: ShowFoodsScreenViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = true) {
        viewModel.onGetFoods()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Food") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Add actions if needed
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        bottomBar = { DietaryLogsBottomBar(navController) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(8.dp)) {
            SearchBar(searchText = viewModel.searchBar.value, onSearchChanged = viewModel::onSearchBarChange)
            Spacer(Modifier.height(10.dp))
            FoodsList(navController, viewModel.filteredFoods.value)
        }
    }
}

@Composable
fun SearchBar(searchText: String, onSearchChanged: (String) -> Unit) {
    var textState by remember { mutableStateOf(TextFieldValue(searchText)) }
    BasicTextField(
        value = textState,
        onValueChange = {
            textState = it
            onSearchChanged(it.text)
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.body1,
        decorationBox = { innerTextField ->
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
                Spacer(Modifier.width(8.dp))
                Box(Modifier.weight(1f)) { innerTextField() }
            }
        }
    )
}
@Composable
fun FoodsList(navController: NavController, foods: List<FoodResponse>) {
    if (foods.isEmpty()) {
        Text("No foods found", style = MaterialTheme.typography.body1)
    } else {
        LazyColumn {
            items(foods.size) { index ->
                val food = foods[index]
                FoodCard(food) {
                    // Action on food card click
                        println("foodid from showfoods ${food.foodId}")
                    navController.navigate(Screen.AddDietaryLogScreen.route + "?foodId=${food.foodId}")
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: FoodResponse, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clickable(onClick = onClick),
        elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(food.foodName, style = MaterialTheme.typography.h6)
            Text("Calories: ${food.cal}", style = MaterialTheme.typography.body1)
            Text("Protein: ${food.protein}g", style = MaterialTheme.typography.body1)
            Text("Carbs: ${food.carb}g", style = MaterialTheme.typography.body1)
            Text("Fats: ${food.fat}g", style = MaterialTheme.typography.body1)
        }
    }
}
