package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/03/24
 *    Desc   : 首页bean
 */
data class FHomeBean(
    val hotCityList: List<String>,
    val indexLoopInfoList: List<IndexLoopInfo>
)

data class IndexLoopInfo(
    val img: String,
    val name: String,
    val overview: String
)

data class FHomeSceneBean(
    val mustPlaySceneList: List<MustPlayScene>
)

data class MustPlayScene(
    val name: String,
    val recommend: String,
    val sceneId: Int,
    val smallImg: String,
    val star: Int
)