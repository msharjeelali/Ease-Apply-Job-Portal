import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class ProjectAdapter(private var projectList: MutableList<Project>?, private val onDeleteClick: (Project) -> Unit) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectTitleTextView: TextView = itemView.findViewById(R.id.projectTitle)
        val projectDescriptionTextView: TextView = itemView.findViewById(R.id.projectDetail)
        val projectLinkTextView: TextView = itemView.findViewById(R.id.projectLink)
        val projectDeleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteProject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projectList!![position]
        holder.projectTitleTextView.text = project.title
        holder.projectDescriptionTextView.text = project.description
        holder.projectLinkTextView.text = project.link
        holder.projectDeleteButton.setOnClickListener {
            onDeleteClick(project)
        }
    }

    override fun getItemCount(): Int = projectList!!.size
}