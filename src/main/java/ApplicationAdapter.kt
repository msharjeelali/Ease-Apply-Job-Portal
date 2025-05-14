import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class ApplicationAdapter (
    private val applications: List<Applications>,
    private val listener: OnApplicationListener
) : RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>() {

    interface OnApplicationListener {
        fun onRejectCLick(application: Applications, position: Int)
        fun onAcceptClick(application: Applications, position: Int)
    }

    inner class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.userName)
        val email = itemView.findViewById<TextView>(R.id.userEmail)
        val phone = itemView.findViewById<TextView>(R.id.userPhone)
        val title = itemView.findViewById<TextView>(R.id.profileTitle)
        val summary = itemView.findViewById<TextView>(R.id.profileSummary)
        /*val education = itemView.findViewById<RecyclerView>(R.id.educationRecyclerView)
        val social = itemView.findViewById<RecyclerView>(R.id.socialRecyclerView)
        val course = itemView.findViewById<RecyclerView>(R.id.courseRecyclerView)
        val project = itemView.findViewById<RecyclerView>(R.id.projectRecyclerView)
        val experience = itemView.findViewById<RecyclerView>(R.id.experienceRecyclerView)
        val certification = itemView.findViewById<RecyclerView>(R.id.certificationRecyclerView)*/

        val reject = itemView.findViewById<Button>(R.id.buttonReject)
        val accept = itemView.findViewById<Button>(R.id.buttonAccpet)


        @SuppressLint("SetTextI18n")
        fun bind(application: Applications, position: Int) {
            name.text = application.userId
            title.text = application.profile.title
            summary.text = application.profile.summary

            reject.setOnClickListener {
                listener.onRejectCLick(application, position)
            }

            accept.setOnClickListener {
                listener.onAcceptClick(application, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_application, parent, false)
        return ApplicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.bind(applications[position], position)
    }

    override fun getItemCount(): Int = applications.size
}