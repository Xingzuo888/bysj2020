package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/03/27
 *    Desc   : 目的地bean
 */
data class FAddressBean(
    val hotelImgTopInfos: List<HotelImgTopInfo>,
    val sceneImgTopInfoList: List<SceneImgTopInfo>
)

data class HotelImgTopInfo(
    val hotelId: Int,
    val img: String,
    val name: String
)

data class SceneImgTopInfo(
    val name: String,
    val recommend: String,
    val sceneId: Int,
    val smallImg: String,
    val star: Int
)