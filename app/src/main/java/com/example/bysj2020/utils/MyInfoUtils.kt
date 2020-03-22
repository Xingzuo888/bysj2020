package com.example.bysj2020.utils

import com.example.bysj2020.enums.SexEnum

/**
 *    Author : wxz
 *    Time   : 2020/03/21
 *    Desc   : 个人信息 枚举信息列表
 */
object MyInfoUtils {
    val sexList = listOf(SexEnum.FEMALE, SexEnum.MALE, SexEnum.UNKNOWN)
    val myInfoSex = listOf(SexEnum.FEMALE.typeName, SexEnum.MALE.typeName, SexEnum.UNKNOWN.typeName)
}