import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class JobEmployerDashboardAdapter (
    private val jobList: List<Job>,
    private val listener: OnJobActionListener
) : RecyclerView.Adapter<JobEmployerDashboardAdapter.JobViewHolder>() {

    interface OnJobActionListener {
        fun onApplicationClick(job: Job)
        fun onEditJobClick(job: Job)
    }

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitle = itemView.findViewById<TextView>(R.id.textViewJobTitle)
        val categorySalary = itemView.findViewById<TextView>(R.id.textViewCategorySalary)
        val skills = itemView.findViewById<TextView>(R.id.textViewSkills)
        val applicants = itemView.findViewById<TextView>(R.id.textViewApplicants)
        val dates = itemView.findViewById<TextView>(R.id.textViewDates)
        val editButton = itemView.findViewById<TextView>(R.id.buttonEditJob)

        @SuppressLint("SetTextI18n")
        fun bind(job: Job) {
            jobTitle.text = job.title
            categorySalary.text = "${job.category} • Rs ${job.salary}"
            skills.text = "Skills: ${job.skills.joinToString(", ")}"
            applicants.text = "Applicants: ${job.applicants.size}"
            dates.text = "Posted: ${job.posted} | Deadline: ${job.deadline}"
            editButton.setOnClickListener {
                listener.onEditJobClick(job)
            }

            itemView.setOnClickListener {
                listener.onApplicationClick(job)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_job_employer_dashboard, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(jobList[position])
    }

    override fun getItemCount(): Int = jobList.size
}