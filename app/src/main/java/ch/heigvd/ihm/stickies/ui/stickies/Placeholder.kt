package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.R

@Composable
fun Placeholder(
    title: String,
    asset: VectorAsset,
    modifier: Modifier = Modifier,
) {
    ProvideEmphasis(AmbientEmphasisLevels.current.disabled) {
        Column(modifier) {
            Row {
                Icon(asset, Modifier.preferredSize(24.dp))
                Text(title)
            }
            Box(
                modifier = Modifier
                    .border(1.dp, AmbientContentColor.current)
                    .preferredSize(256.dp),
                alignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(asset, Modifier.preferredSize(64.dp))
                    Text(title)
                }
            }
        }
    }
}

@Composable
@Preview
private fun PlaceholderPreview() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            Placeholder(
                title = "Inbox",
                asset = vectorResource(R.drawable.ic_category_inbox),
            )
        }
    }
}