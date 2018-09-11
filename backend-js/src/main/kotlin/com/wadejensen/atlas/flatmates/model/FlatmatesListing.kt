package com.wadejensen.atlas.flatmates.model

import kotlinx.serialization.Serializable

@Serializable
data class FlatmatesListing(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val rent: Array<Double>,
    val type: String,
    val listing_link: String,
    val head: String,
    val subheading: String,
    val photo: String)
