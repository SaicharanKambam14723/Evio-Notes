package com.example.evionotes.ui.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(
            modifier = Modifier.padding(24.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dark Mode")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = onToggleDarkTheme,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        var isTandCExpanded by remember { mutableStateOf(false) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))

            // Example setting toggle (can keep your other settings here)
            // ...

            // Terms & Conditions dropdown header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isTandCExpanded = !isTandCExpanded }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Terms & Conditions", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (isTandCExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                    contentDescription = if (isTandCExpanded) "Collapse" else "Expand"
                )
            }

            HorizontalDivider()

            // Expandable content with smooth animation
            if (isTandCExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 300.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                        .animateContentSize()
                ) {
                    Text(
                        text = """
                    Welcome to EvioNotes!
                    
                    1. Acceptance of Terms  
                    By using EvioNotes, you agree to comply with and be bound by these Terms & Conditions. If you do not agree, please do not use the app.
                    
                    2. User Content  
                    You retain ownership of any notes, images, or data you create within the app. You grant EvioNotes a license to store and process your content to provide functionality.
                    
                    3. Privacy  
                    EvioNotes respects your privacy. Personal data is not shared with third parties without your consent. For more details see our Privacy Policy.
                    
                    4. Usage Restrictions  
                    You agree not to misuse EvioNotes for illegal activities, harassment, or infringement on others’ rights.
                    
                    5. Intellectual Property  
                    All app design, trademarks, and software are the property of EvioNotes. Unauthorized copying or distribution is prohibited.
                    
                    6. Disclaimer of Warranties  
                    EvioNotes is provided as-is without guarantees of accuracy or reliability. Use at your own risk.
                    
                    7. Limitation of Liability  
                    EvioNotes is not liable for loss or damage arising from app use.
                    
                    8. Changes to Terms  
                    We may update terms occasionally; continued use implies acceptance.
                    
                    9. Contact  
                    Any questions regarding these terms can be emailed to support@evionotes.com.
                    
                    Thank you for using EvioNotes!
                    """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}