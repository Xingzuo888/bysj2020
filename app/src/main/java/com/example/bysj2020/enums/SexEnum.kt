package com.example.bysj2020.enums

/**
 *    Author : wxz
 *    Time   : 2020/03/21
 *    Desc   : 性别
 */
enum class SexEnum(val type: Int, val typeName: String) {
    FEMALE(0, "女"), MALE(1, "男"), UNKNOWN(2, "保密")
}