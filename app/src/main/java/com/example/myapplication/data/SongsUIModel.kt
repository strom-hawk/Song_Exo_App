package com.example.myapplication.data

data class SongsUIModel(
    val artist: String = "",
    val title: String = "",
    val url: String = ""
)

data class SongsList(
    val songsList : List<SongsUIModel> = listOf()
)

fun getSongsList(): MutableList<SongsUIModel> {
    val songsList = mutableListOf<SongsUIModel>()
    val firstSong = SongsUIModel(
        artist = "Linkin Park",
        title = "Numb",
        url = "http://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Kangaroo_MusiQue_-_The_Neverwritten_Role_Playing_Game.mp3"
    )

    val secondSong = SongsUIModel(
        artist = "Maroon 5",
        title = "Moves Like Jagger",
        url = "http://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3"
    )

    val thirdSong = SongsUIModel(
        artist = "Imagine Dragons",
        title = "Believer",
        url = "http://codeskulptor-demos.commondatastorage.googleapis.com/GalaxyInvaders/theme_01.mp3"
    )

    songsList.add(firstSong)
    songsList.add(secondSong)
    songsList.add(thirdSong)

    return songsList
}