data class Job(
    val id: String,
    val title: String,
    val companyName: String,
    val location: String,
    val salary: String?,
    val jobType: String,
    val description: String,
    val requirements: List<String>,
    val postedDate: String,
    val deadline: String?,
    val experienceLevel: String,
    val category: String,
    val applicants: Int
)