package com.example.firstecommerceproject.ui.screens.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme

/**
 * A header component for the Home screen that displays a welcome message and a search action.
 *
 * @param modifier Modifier for the root Row container.
 * @param name The name of the user to be displayed in the welcome message.
 */
@Composable
fun HeaderView(modifier: Modifier = Modifier, name: String) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            // Secondary greeting text using variant color for visual hierarchy
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Primary user name text with bold weight and brand color
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Search icon button - placeholder for search functionality
        IconButton(
            onClick = { /* TODO: Implement Search functionality in future iteration */ },
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search products",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Previews for HeaderView in both Light and Dark modes.
 */
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewHeaderView() {
    FirstEcommerceProjectTheme {
        Surface {
            HeaderView(name = "Happy Customer")
        }
    }
}
