package danielle.projects.gameshowspinoff.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danielle.projects.gameshowspinoff.model.ColorBarStateItem
import danielle.projects.gameshowspinoff.model.Prize
import kotlinx.coroutines.launch


@Composable
fun MoneyLadderComponent(ladderState: MutableList<ColorBarStateItem>?, prizeCheckpoints: MutableMap<Int, Prize>, moneyCheckpoints: Map<Int, Double>, currentPosition: Int) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .fillMaxWidth()
            .height(700.dp)
    ) {
        items(items = ladderState!!) { item ->
            val bonusPrize = prizeCheckpoints.getOrDefault(item.barPosition, null)
            val bonusPrizeTitle = bonusPrize?.prizeTitle ?: ""
            ColorBarComponent(
                barPosition = item.barPosition,
                bonusPrize = bonusPrizeTitle,
                moneyCheckpointValue = moneyCheckpoints.getOrDefault(item.barPosition, null),
                initialColorBarState = item.colorBarState,
                onChange = {
                    if (item.barPosition == currentPosition) {
                        var scrollOffset = -100
                        if (item.barPosition > 10) {
                            scrollOffset = -500
                        }
                        scope.launch {
                        listState.animateScrollToItem(item.barPosition, scrollOffset) }

                    }
                }
            )
        }
    }
}
