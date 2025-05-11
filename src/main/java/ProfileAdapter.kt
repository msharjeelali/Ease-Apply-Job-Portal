import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class ProfileAdapter(private var profileList: MutableList<Profile>, private val onItemClick: (profile: Profile, position: Int) -> Unit) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(profile: Profile) {
            itemView.findViewById<TextView>(R.id.profileTitle).text = profile.title
            itemView.findViewById<TextView>(R.id.profileSummary).text = profile.summary
            itemView.setOnClickListener {
                onItemClick(profileList[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(profileList[position])
    }

    override fun getItemCount(): Int = profileList.size
}