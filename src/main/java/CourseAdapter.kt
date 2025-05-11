import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class CourseAdapter(private var courseList: MutableList<String>, private val onDeleteClick: (String) -> Unit) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseTextView: TextView = itemView.findViewById(R.id.courseName)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.courseTextView.text = course
        holder.deleteButton.setOnClickListener {
            onDeleteClick(course.toString())
        }
    }

    override fun getItemCount(): Int = courseList.size
}