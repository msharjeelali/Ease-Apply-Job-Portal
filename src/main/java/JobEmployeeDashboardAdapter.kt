import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class JobEmployeeDashboardAdapter(private var jobList: MutableList<Job>) :
    RecyclerView.Adapter<JobEmployeeDashboardAdapter.JobViewHolder>() {

    private lateinit var clickListener: ItemClickListenerInterface

    inner class JobViewHolder(itemView: View, listener: ItemClickListenerInterface) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textviewJobTitle)
        val company: TextView = itemView.findViewById(R.id.textviewCompanyName)
        val location: TextView = itemView.findViewById(R.id.textviewLocation)
        val jobType: TextView = itemView.findViewById(R.id.textviewJobType)
        val salary: TextView = itemView.findViewById(R.id.textviewSalary)
        val posted: TextView = itemView.findViewById(R.id.textviewPosted)
        val deadline: TextView = itemView.findViewById(R.id.textviewDeadline)
        val skills: TextView = itemView.findViewById(R.id.textviewSkills)
        val applicationsCount: TextView = itemView.findViewById(R.id.textviewApplicationsCount)
        val jobDescription: TextView = itemView.findViewById(R.id.textviewJobDescription)

        init {
            company.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun interface ItemClickListenerInterface {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: ItemClickListenerInterface) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_job_employee_dashboard, parent, false)
        return JobViewHolder(view, clickListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]
        holder.title.text = job.title
        holder.company.text = job.companyName
        holder.location.text = job.location
        holder.jobType.text = job.category
        holder.salary.text = "Rs. ${job.salary}"
        holder.posted.text = "Posted: ${job.deadline}"
        holder.deadline.text = "Deadline: ${job.deadline}"
        holder.skills.text = "Skills: " + job.skills.joinToString(", ")
        holder.applicationsCount.text = "Applicants: ${job.applicants.size.toString()}"
        holder.jobDescription.text = job.description
    }

    override fun getItemCount(): Int = jobList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(position: Int, newJob: Job) {
        jobList.add(position, newJob)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int) {
        jobList.removeAt(position)
        notifyDataSetChanged()
    }

}
