package com.yash026.zerowaste.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.inconceptlabs.designsystem.components.buttons.IconButton
import com.inconceptlabs.designsystem.theme.AppTheme
import com.inconceptlabs.designsystem.theme.LocalContentColor
import com.inconceptlabs.designsystem.theme.attributes.CornerType
import com.inconceptlabs.designsystem.theme.attributes.Size
import com.yash026.zerowaste.R
import com.yash026.zerowaste.composables.CenteredLoadingIndicator
import com.yash026.zerowaste.composables.MealDetailsScreenState
import com.yash026.zerowaste.listitem.IngredientItem
import com.yash026.zerowaste.model.IngredientModel
import com.yash026.zerowaste.model.MealDetail
import com.yash026.zerowaste.model.MealDetailsModel
import com.yash026.zerowaste.ui.theme.Red900
import com.yash026.zerowaste.ui.theme.shimmerBrush
import com.yash026.zerowaste.viewmodels.MealDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String, navController: NavHostController, viewModel: MealDetailViewModel = viewModel()

) {

    LaunchedEffect(Unit) {
        viewModel.fetchMeal(id)
    }

    val meal = viewModel.meal

    val isLoading = viewModel.isLoading
    val error = viewModel.error

    when {
        isLoading -> {
            CenteredLoadingIndicator()
        }

        error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
        }

        meal != null -> {
            ContentPreview(
                navController = navController,
                meal = meal,
            )
        }

        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Meal details could not be loaded.")
            }
        }
    }

}

@Composable
private fun ContentPreview(
    navController: NavHostController,
    meal: MealDetail,
) {
    val screenState = remember(meal) {
        MealDetailsScreenState(
            meal = MealDetailsModel(
                id = meal.idMeal,
                name = meal.strMeal,
                category = meal.strCategory,
                region = meal.strArea,
                instructions = meal.strInstructions,
                thumbnail = meal.strMealThumb,
                youtubeUrl = meal.strYoutube,
                sourceUrl = meal.strSource,
                ingredients = meal.let { getIngredientList(it) },
            ),
        )
    }


    Content(
        screenState = screenState, navController = navController
    )
}

fun getIngredientList(meal: MealDetail): List<IngredientModel> {
    val ingredients = listOf(
        meal.strIngredient1,
        meal.strIngredient2,
        meal.strIngredient3,
        meal.strIngredient4,
        meal.strIngredient5,
        meal.strIngredient6,
        meal.strIngredient7,
        meal.strIngredient8,
        meal.strIngredient9,
        meal.strIngredient10,
        meal.strIngredient11,
        meal.strIngredient12,
        meal.strIngredient13,
        meal.strIngredient14,
        meal.strIngredient15,
        meal.strIngredient16,
        meal.strIngredient17,
        meal.strIngredient18,
        meal.strIngredient19,
        meal.strIngredient20
    )

    val measures = listOf(
        meal.strMeasure1,
        meal.strMeasure2,
        meal.strMeasure3,
        meal.strMeasure4,
        meal.strMeasure5,
        meal.strMeasure6,
        meal.strMeasure7,
        meal.strMeasure8,
        meal.strMeasure9,
        meal.strMeasure10,
        meal.strMeasure11,
        meal.strMeasure12,
        meal.strMeasure13,
        meal.strMeasure14,
        meal.strMeasure15,
        meal.strMeasure16,
        meal.strMeasure17,
        meal.strMeasure18,
        meal.strMeasure19,
        meal.strMeasure20
    )

    return ingredients.zip(measures) { ingredient, measure ->
        if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
            IngredientModel(name = ingredient, quantity = measure)
        } else null
    }.filterNotNull() // Remove null values
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    screenState: MealDetailsScreenState,
    navController: NavHostController,
) = with(screenState) {

    val meal = screenState.meal

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    "Recipe Detail", color = Color.Black // Optional: change text color
                )
            }, navigationIcon = {
                Icon(Icons.Rounded.ArrowBack, "", modifier = Modifier.padding(start = 15.dp).clickable {
                    navController.navigateUp()
                })
            },

            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFA8FF97) // 🍊 Orange background
            )
        )

    }, content = { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = 24.dp
            ), modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {

            mealName(meal = meal)

            instructions(meal = meal)

            mealThumbnail(meal = meal)

            ingredients(meal = meal)
        }
    })
}


//@Composable
//private fun Content(
//    screenState: MealDetailsScreenState, navController: NavHostController,
//) = with(screenState) {
//
//    val meal = screenState.meal
//
//
//
//    LazyColumn(
//        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
//        modifier = Modifier
//            .fillMaxSize()
//            .systemBarsPadding()
//    ) {
//        mealThumbnail(meal = meal)
//
//        mealName(meal = meal)
//
//        instructions(meal = meal)
//
//        ingredients(meal = meal)
//
//    }
//
//}


private fun LazyListScope.ingredients(meal: MealDetailsModel) {
    if (meal.ingredients.isEmpty()) return

    item(key = "IngredientsTitle") {
        com.inconceptlabs.designsystem.components.core.Text(
            text = stringResource(id = R.string.meal_details_ingredients),
            style = AppTheme.typography.S1,
            modifier = Modifier
                .animateItem()
                .padding(
                    top = 32.dp, bottom = 8.dp
                )
        )
    }

    items(
        key = { it }, items = meal.ingredients
    ) {
        IngredientItem(
            item = it, modifier = Modifier.animateItem()
        )
    }
}

private fun LazyListScope.instructions(meal: MealDetailsModel) {
    if (meal.instructions.isBlank()) return

    item(key = "InstructionsTitle") {
        com.inconceptlabs.designsystem.components.core.Text(
            text = stringResource(R.string.meal_details_cooking_process),
            style = AppTheme.typography.S1,
            modifier = Modifier
                .animateItem()
                .padding(top = 32.dp)
        )
    }

    item(key = meal.instructions) {
        com.inconceptlabs.designsystem.components.core.Text(
            text = meal.instructions,
            style = AppTheme.typography.P4,
            modifier = Modifier
                .animateItem()
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}


private fun LazyListScope.mealName(meal: MealDetailsModel) {
    item(key = meal.name) {
        Text(
            text = meal.name,
            fontSize = 22.sp,
        )
    }
}


private fun LazyListScope.mealThumbnail(meal: MealDetailsModel) {
    if (meal.thumbnail.isBlank()) return

    var isImageLoading = true

    item(key = meal.thumbnail) {
        Image(
            painter = rememberAsyncImagePainter(meal.thumbnail),
            contentDescription = "MealImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(shimmerBrush(isImageLoading))
        )
    }
}


private fun LazyListScope.toolbar(
    onBackButtonClick: () -> Unit,
) {


    item(key = "Toolbar") {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                icon = painterResource(id = R.drawable.ic_back),
                size = Size.S,
                cornerType = CornerType.CIRCULAR,
                onClick = onBackButtonClick
            )

            Text(
                text = stringResource(R.string.meal_details_title),
                style = AppTheme.typography.S1,
                modifier = Modifier.weight(1f)
            )

            var shouldShowShareButton by remember { mutableStateOf(false) }

// Toggle visibility when needed
            shouldShowShareButton = false  // Hide


            CompositionLocalProvider(
                LocalContentColor provides Red900
            ) {}
        }
    }
}

