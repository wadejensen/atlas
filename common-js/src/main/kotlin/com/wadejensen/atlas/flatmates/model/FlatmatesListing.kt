package com.wadejensen.atlas.flatmates.model

/**
 * An individual real estate listing on flatmates.com.au
 * @param head Heading text of the listing advertisement
 * @param subheading Byline of advertisement
 * @param photo Url to listing main preview image
 * @param listing_link url suffix for listing webpage. Full url = "flatmates.com.au/$listing_link"
 * @param latitude
 * @param longitude
 * @param rent Price to occupy. May contain an upper and lower value as an array of length = 2
 * @param id Unique identifier
 * @param type The kind of listing eg. room, whole house, team up. See [[ListingType]].
 */
data class FlatmatesListing(
    val head: String,
    val subheading: String,
    val photo: String,
    val listing_link: String,
    val latitude: Double,
    val longitude: Double,
    val rent: Array<Int>,
    val id: Int,
    val type: String)
