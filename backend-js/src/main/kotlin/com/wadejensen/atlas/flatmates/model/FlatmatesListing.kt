package com.wadejensen.atlas.flatmates.model

import kotlinx.serialization.Serializable

/**
 * An individual real estate listing on flatmates.com.au
 * @param id Unique identifier
 * @param latitude
 * @param longitude
 * @param rent Price to occupy. May contain an upper and lower value as an array of length = 2
 * @param listing_link url suffix for listing webpage. Full url = "flatmates.com.au/$listing_link"
 * @param type The kind of listing eg. room, whole house, team up. See [[ListingType]].
 * @param head Heading text of the listing advertisement
 * @param subheading Byline of advertisement
 * @param photo Url to listing main preview image
 */
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
