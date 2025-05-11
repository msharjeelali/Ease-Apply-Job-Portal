import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class EducationAdapter(private var educationList: MutableList<Education>, private val listener: clickListenerEducation ) :
    RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    interface clickListenerEducation {
        fun editEducationInfo(education: Education, position: Int)
        fun deleteEducationInfo(education: Education, position: Int)
    }

    inner class EducationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val degree: TextView = itemView.findViewById(R.id.degreeName)
        val institute: TextView = itemView.findViewById(R.id.instituteName)
        val start: TextView = itemView.findViewById(R.id.dateFrom)
        val till: TextView = itemView.findViewById(R.id.dateTill)
        val editButton: ImageButton = itemView.findViewById(R.id.buttonEditEducation)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteEducation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_education, parent, false)
        return EducationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val education = educationList[position]
        holder.degree.text = education.degree
        holder.institute.text = education.university
        holder.start.text = education.startDate
        holder.till.text = education.endDate
        holder.editButton.setOnClickListener {
            listener.editEducationInfo(education, position)
        }
        holder.deleteButton.setOnClickListener {
            listener.deleteEducationInfo(education, position)
        }
    }

    override fun getItemCount(): Int = educationList.size
}
