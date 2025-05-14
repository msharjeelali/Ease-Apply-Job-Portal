import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    var jobId: String,
    val title: String,
    val description: String,
    val location: String,
    val salary: String?,
    val skills: List<String>,
    val category: String,
    val experienceLevel: String,
    val deadline: String?,
    val posted: String?,
    val companyName: String,
    val companyId: String,
    var applicants: MutableList<Applications>
) : Parcelable {
    // Empty constructor required for Firebase
    constructor() : this("",
        "",
        "",
        "",
        null,
        emptyList(),
        "",
        "",
        null,
        null,
        "",
        "",
        mutableListOf()
    )
}

@Parcelize
data class Applications(
    val userId: String,
    val profile: Profile,
    var status: String

) : Parcelable {
    // Empty constructor required for Firebase
    constructor(): this("", Profile(), "")
}