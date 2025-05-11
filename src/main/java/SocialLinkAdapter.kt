import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class SocialLinkAdapter(private var linkList: MutableList<SocialLink>, private val listener: clickListenerSocial) :
    RecyclerView.Adapter<SocialLinkAdapter.SocialLinkViewHolder>() {

    interface clickListenerSocial {
        fun editSocialAccount(social: SocialLink, position: Int)
        fun deleteSocialAccount(social: SocialLink, position: Int)
    }

    inner class SocialLinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.platformName)
        val link: TextView = itemView.findViewById(R.id.platformLink)
        val editButton: ImageButton = itemView.findViewById(R.id.buttonEditSocialLink)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteSocialLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialLinkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_social_account, parent, false)
        return SocialLinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocialLinkViewHolder, position: Int) {
        val social = linkList[position]
        holder.name.text = social.name
        holder.link.text = social.link
        holder.editButton.setOnClickListener {
            listener.editSocialAccount(social, position)
        }
        holder.deleteButton.setOnClickListener {
            listener.deleteSocialAccount(social, position)
        }
    }

    override fun getItemCount(): Int = linkList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(position: Int, newSocialLink: SocialLink) {
        linkList.add(position, newSocialLink)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int) {
        linkList.removeAt(position)
        notifyDataSetChanged()
    }
}
