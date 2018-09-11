package com.wadejensen.atlas.flatmates.model

/**
 * Body of the flatmates.com.au autocomplete API request.
 */
data class AutocompleteRequestBody(val location_suggest: LocationSuggest) {
    companion object {
        /**
         * @param userInput Arbitrary string to perform geographic autocomplete
         */
        fun create(userInput: String): AutocompleteRequestBody {
            return AutocompleteRequestBody(
                location_suggest = LocationSuggest(
                    text = userInput,
                    completion = Completion(
                        field = "suggest",
                        size = 5,  // Number of autosuggest responses to return
                        fuzzy = Fuzzy(
                            fuzziness = "AUTO"
                        ),
                        contexts = Contexts(
                            location_type = arrayOf<String>("suburb","city","university","tram_stop","train_station")
                        )
                    )
                )
            )
        }

        data class LocationSuggest(val text: String, val completion: Completion)
        data class Completion(val field: String, val size: Int, val fuzzy: Fuzzy, val contexts: Contexts)
        data class Contexts(val location_type: Array<String>)
        data class Fuzzy(val fuzziness: String)
    }
}

