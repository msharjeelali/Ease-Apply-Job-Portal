import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easeapplyportal.R

class CertificationAdapter(private var certificateList: MutableList<Certification>?, private val onDeleteClick: (Certification) -> Unit) :
    RecyclerView.Adapter<CertificationAdapter.CertificationViewHolder>() {

    inner class CertificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val certificateTitleTextView: TextView = itemView.findViewById(R.id.certificateTitle)
        val certificateFromTextView: TextView = itemView.findViewById(R.id.certificateFrom)
        val certificateDeleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteCertificate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_certificate, parent, false)
        return CertificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertificationViewHolder, position: Int) {
        val certificate = certificateList!![position]
        holder.certificateTitleTextView.text = certificate.title
        holder.certificateFromTextView.text = certificate.institute
        holder.certificateDeleteButton.setOnClickListener {
            onDeleteClick(certificate)
        }
    }

    override fun getItemCount(): Int = certificateList?.size ?: 0
}