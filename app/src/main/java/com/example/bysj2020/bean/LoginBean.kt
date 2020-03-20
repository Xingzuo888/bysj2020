package com.example.bysj2020.bean

/**
 *    Author : wxz
 *    Time   : 2020/03/12
 *    Desc   :
 */
data class LoginBean(
    val avatar: String,//头像
    val axUid: Int,//用户id
    val mobile: String,//手机号
    val nickname: String,//昵称
    val role: String,//角色
    val sex: Int,//性别（0女 1 男 3未知)
    val token: String
)