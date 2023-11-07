package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.data.SongsUIModel
import com.example.myapplication.data.getSongsList
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import javax.sql.DataSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val homeView = "1"
                    val musicPlayerView = "2"

                    val songList = getSongsList()
                    val currentView = remember {mutableStateOf(value = homeView)}
                    val currentSongIndex = remember {mutableStateOf(value = 0)}

                    Column {

                        if(currentView.value == homeView) {
                            HomeView(songList) { clickedSongIndex ->
                                currentSongIndex.value = clickedSongIndex
                                currentView.value = musicPlayerView
                            }
                        } else {
                            MusicPlayerView(
                                songList,
                                currentSongIndex.value
                            ) {
                                if(currentView.value == musicPlayerView) {
                                    currentView.value = homeView
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeView(
    songList: List<SongsUIModel>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn {
        songList.forEachIndexed { index, _currentSong ->
            item {
                SongView(
                    song = _currentSong,
                    index = index,
                    onItemClick
                )
            }
        }
    }
}

@Composable
fun SongView(
    song: SongsUIModel,
    index: Int,
    onItemClick: (Int) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(horizontal = 16.dp)
            .clickable { onItemClick(index) },
        text = song.title
    )
}

@Composable
fun MusicPlayerView(
    songList: List<SongsUIModel>,
    selectedIndex : Int,
    backIconClicked: () -> Unit
) {
    val exoPlayerClient = GetExoPlayer(songList)
    exoPlayerClient.seekToDefaultPosition(selectedIndex)

    Column {
        Row {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        exoPlayerClient.stop()
                        backIconClicked()
                    },
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "back icon"
            )
        }

        AndroidView(factory = { context ->
            PlayerView(context).apply {
                player = exoPlayerClient
            }
        })
    }
}

@Composable
fun GetExoPlayer(
    songList: List<SongsUIModel>,
): ExoPlayer {
    val localContext = LocalContext.current
    val exoPlayerClient = remember(localContext) {
        ExoPlayer.Builder(localContext).build().apply {
            val dsf = DefaultDataSourceFactory(localContext, Util.getUserAgent(localContext, localContext.packageName))
            val song1 = ProgressiveMediaSource.Factory(dsf).createMediaSource(Uri.parse(songList[0].url))
            val song2 = ProgressiveMediaSource.Factory(dsf).createMediaSource(Uri.parse(songList[1].url))
            val song3 = ProgressiveMediaSource.Factory(dsf).createMediaSource(Uri.parse(songList[2].url))

            prepare(ConcatenatingMediaSource(song1, song2, song3))
        }
    }

    return exoPlayerClient
}
