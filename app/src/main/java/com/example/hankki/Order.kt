package com.example.hankki

data class Order (
    var orderNum : Int? = null,
    var id : String? = null,
    var menu : String? = null,
    var finish : Boolean?= null,
    var amount : Int? = null
)