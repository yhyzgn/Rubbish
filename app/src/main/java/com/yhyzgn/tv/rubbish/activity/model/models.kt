package com.yhyzgn.tv.rubbish.activity.model

data class Category(
    val id: String,
    val name: String
)

data class Media(
    val id: String,
    val title: String,
    val posterUrl: String,
    val playPageUrl: String?, // landing page / direct media url
    val sourceId: String
)
