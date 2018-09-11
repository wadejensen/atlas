package com.wadejensen.atlas.flatmates.model

/**
 *  Auto-generated code for modelling the flatmates.com.au map markers API response
 */
data class MapMarkersResponseBody(
    val matches: Array<FlatmatesListing>,
    val non_matches: Array<FlatmatesListing>
)
