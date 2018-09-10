package com.wadejensen.atlas.model

import kotlinx.serialization.Serializable

@Serializable
data class Person(val name: String, val age: Int, val address: Address)
