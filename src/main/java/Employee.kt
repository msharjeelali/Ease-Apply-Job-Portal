import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// User data class
data class Employee(
    val type: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val education: MutableList<Education>? = mutableListOf(),
    val social: MutableList<SocialLink>? = mutableListOf(),
    val profile: MutableList<Profile>? = mutableListOf()
) {
    // Empty constructor required for Firebase
    constructor() : this(null, null, null, null, mutableListOf(), mutableListOf(), mutableListOf())
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
    var title: String? = null,
    var summary: String? = null,
    var experience: MutableList<Experience> = mutableListOf(),
    var certification: MutableList<Certification>? = mutableListOf(),
    var project: MutableList<Project>? = mutableListOf(),
    var courses: MutableList<String>? = mutableListOf()
):Parcelable {
    // Empty constructor required for Firebase
    constructor() : this(null, null, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
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