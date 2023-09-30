package com.example.testmbrush

data class Result(
    val name: String,
    val url: String,
    val picurl: String,
    val artistname: String,
    val avatarurl: String,
    val nickname: String,
    val content: String,
)

data class Resp(
    val code: String,
    val data: Result
)