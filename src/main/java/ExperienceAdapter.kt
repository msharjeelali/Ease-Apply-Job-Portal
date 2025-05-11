import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class ExperienceAdapter(private var experienceList: MutableList<Experience>?, private val onDeleteClick: (Experience) -> Unit) :
    RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder>() {

    inner class ExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.jobTitle)
        val companyTextView: TextView = itemView.findViewById(R.id.companyName)
        val startDateTextView: TextView = itemView.findViewById(R.id.dateFrom)
        val endDateTextView: TextView = itemView.findViewById(R.id.dateTill)
        val descriptionTextView: TextView = itemView.findViewById(R.id.jobSummary)
        val deleteButton: TextView = itemView.findViewById(R.id.buttonDeleteExperience)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_experience, parent, false)
        return ExperienceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val experience = experienceList!![position]
        holder.titleTextView.text = experience.title
        holder.companyTextView.text = experience.company
        holder.startDateTextView.text = experience.startDate
        holder.endDateTextView.text = experience.endDate
        holder.descriptionTextView.text = experience.description
        holder.deleteButton.setOnClickListener {
            onDeleteClick(experience)
        }
    }

    override fun getItemCount(): Int = experienceList!!.size
}
