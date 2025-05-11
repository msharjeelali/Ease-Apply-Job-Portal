import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class JobAdapter(private var jobList: MutableList<Job>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

        private lateinit var clickListener: ItemClickListenerInterface

    inner class JobViewHolder(itemView: View, listener: ItemClickListenerInterface) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.jobTitle)
        val company: TextView = itemView.findViewById(R.id.companyName)
        val location: TextView = itemView.findViewById(R.id.jobLocation)
        val jobType: TextView = itemView.findViewById(R.id.jobType)
        val salary: TextView = itemView.findViewById(R.id.salary)
        val posted: TextView = itemView.findViewById(R.id.jobPosted)
        val deadline: TextView = itemView.findViewById(R.id.jobDeadline)

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun interface ItemClickListenerInterface{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: ItemClickListenerInterface){
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_job, parent, false)
        return JobViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]
        holder.title.text = job.title
        holder.company.text = job.companyName
        holder.location.text = job.location
        holder.jobType.text = job.category
        holder.salary.text = job.salary ?: "Not specified"
        holder.posted.text = job.dateposted
        holder.deadline.text = job.deadline ?: "No deadline"
    }

    override fun getItemCount(): Int = jobList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(position: Int, newJob: Job){
        jobList.add(position, newJob)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int){
        jobList.removeAt(position)
        notifyDataSetChanged()
    }

}
