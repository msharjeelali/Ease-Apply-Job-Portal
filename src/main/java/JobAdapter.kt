import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class JobAdapter(private val jobList: List<Job>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.jobTitle)
        val company: TextView = itemView.findViewById(R.id.companyName)
        val location: TextView = itemView.findViewById(R.id.jobLocation)
        val jobType: TextView = itemView.findViewById(R.id.jobType)
        val salary: TextView = itemView.findViewById(R.id.salary)
        val posted: TextView = itemView.findViewById(R.id.jobPosted)
        val deadline: TextView = itemView.findViewById(R.id.jobDeadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]
        holder.title.text = job.title
        holder.company.text = job.companyName
        holder.location.text = job.location
        holder.jobType.text = job.jobType
        holder.salary.text = job.salary ?: "Not specified"
        holder.posted.text = job.postedDate
        holder.deadline.text = job.deadline ?: "No deadline"
    }

    override fun getItemCount(): Int = jobList.size
}
