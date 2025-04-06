package com.yash026.zerowaste.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yash026.zerowaste.model.Meal

@Composable
fun MealItem(
    meal: Meal,

    onItemClick: (mealId: String) -> Unit,

    ) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                onItemClick(meal.idMeal)
            }

    ) {
        Box {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = meal.strMeal,
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}
