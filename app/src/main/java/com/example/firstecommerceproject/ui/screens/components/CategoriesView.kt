package com.example.firstecommerceproject.ui.screens.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.firstecommerceproject.domain.models.Category

/**
 * A horizontal scrollable list of categories.
 *
 * @param modifier Modifier for the root LazyRow container.
 * @param categoriesList The list of Category objects to display.
 * @param onCategoryClick Callback when a category item is selected, passing the category ID.
 */
@Composable
fun CategoriesView(
    modifier: Modifier = Modifier,
    categoriesList: List<Category>,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = categoriesList,
            key = { it.id } // Keyed for better list performance and stability
        ) { category ->
            CategoryItem(
                category = category,
                onCategoryClick = onCategoryClick
            )
        }
    }
}

/**
 * Individual category item displaying a circular icon and a label.
 */
@Composable
fun CategoryItem(
    category: Category,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(72.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // Removes the ripple to maintain the circular visual style
                onClick = { onCategoryClick(category.id) }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular background for the category icon
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
            tonalElevation = 2.dp
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Label with ellipsis for long names
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Previews for CategoriesView in different modes.
 */
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewCategoriesView() {
    MaterialTheme {
        Surface {
            CategoriesView(
                categoriesList = listOf(
                    Category("1", "Electronics", ""),
                    Category("2", "Fashion", ""),
                    Category("3", "Home", ""),
                    Category("4", "Beauty", ""),
                    Category("5", "Toys", "")
                ),
                onCategoryClick = {}
            )
        }
    }
}
