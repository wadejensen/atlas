package com.wadejensen.atlas.model

/**
 * Canno
 */
data class Listing(
    val id: String,
    val lat: Double,
    val lon: Double,
    val price: Double,
    val listingType: String, // TODO: make an enum
    val listingUrl: String,
    val imageUrl: String,
    val source: String,
    val title: String?,
    val subheading: String?,
    val address: String?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val carspaces: Int?)
