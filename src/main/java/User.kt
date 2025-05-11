import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// User data class
data class User(
    val type: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val education: MutableList<Education>? = null,
    val social: MutableList<SocialLink>? = null,
    val profile: MutableList<Profile>? = null
) {
    // Empty constructor required for Firebase
    constructor() : this(null, null, null, null, null, null, null)
}

// Education data class
data class Education(
    val degree: String? = null,
    val university: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
){
    // Empty constructor required for Firebase
    constructor() : this(null, null, null, null)
}

// Social data class
data class SocialLink(
    val name: String? = null,
    val link: String? = null
){
    // Empty constructor required for Firebase
    constructor() : this(null, null)
}

// Profile data class
@Parcelize
data class Profile (
    val title: String? = null,
    val summary: String? = null,
    val experience: MutableList<Experience>? = null,
    val certification: MutableList<Certification>? = null,
    val project: MutableList<Project>? = null,
    val courses: MutableList<String>? = null
):Parcelable {
    // Empty constructor required for Firebase
    constructor() : this(null, null, null, null, null, null)
}

// Experience data class
@Parcelize
data class Experience(
    val title: String? = null,
    val company: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val description: String? = null
): Parcelable{
    // Empty constructor required for Firebase
    constructor() : this(null, null, null, null, null)
}

// Certification data class
@Parcelize
data class Certification(
    val title: String? = null,
    val institute: String? = null
):Parcelable{
    // Empty constructor required for Firebase
    constructor() : this(null, null)
}

// Project data class
@Parcelize
data class Project(
    val title: String? = null,
    val description: String? = null,
    val link: String? = null
):Parcelable{
    // Empty constructor required for Firebase
    constructor() : this(null, null, null)
}