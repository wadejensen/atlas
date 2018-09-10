package com.wadejensen.atlas.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(val streetNum: Int, val streetName: String, val suburb: String, val postcode: Int)
