package com.example.quote.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier // <--- IMPORTANT IMPORT
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.text.Text

class QuoteWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // ERROR WAS HERE: Use GlanceModifier, not Modifier
            Column(modifier = GlanceModifier.padding(16.dp)) {
                Text(text = "Quote of the Day")
                Text(text = "Open app to see today's wisdom!")
            }
        }
    }
}