data class Job(
    val title: String,
    val description: String,
    val location: String,
    val salary: String?,
    val skills: List<String>,
    val category: String,
    val experienceLevel: String,
    val deadline: String?,
    val dateposted: String?,
    val companyName: String,
    val companyId: String,
) {
    // Empty constructor required for Firebase
    constructor() : this("", "", "", null, emptyList(), "", "", null, null, "", "")
}
