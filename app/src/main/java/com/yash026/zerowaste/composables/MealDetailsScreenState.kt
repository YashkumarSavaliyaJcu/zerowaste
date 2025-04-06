package com.yash026.zerowaste.composables

import com.yash026.zerowaste.model.MealDetailsModel


data class MealDetailsScreenState(
    val meal: MealDetailsModel = MealDetailsModel(),

    val isSharingOptionsDialogVisible: Boolean = false,
    val isBookmarked: Boolean = false, // Add bookmark state

    val onBackButtonClick: () -> Unit = { },
    val onShareButtonClick: () -> Unit = { },
    val onSaveButtonClick: () -> Unit = { },
    val onCategoryClick: (String) -> Unit = { },
    val onRegionClick: (String) -> Unit = { },
    val onYoutubeClick: (String) -> Unit = { },
    val onSourceClick: (String) -> Unit = { },
    val onSharingOptionsDialogDismiss: () -> Unit = { },
)