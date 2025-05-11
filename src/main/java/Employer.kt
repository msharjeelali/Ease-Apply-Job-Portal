import android.accessibilityservice.GestureDescription

data class Employer(
    val type: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val address: String? = null,
    val description: String? = null
){
    // Empty constructor required for Firebase
    constructor() : this(null, null, null, null, null, null, null)
}