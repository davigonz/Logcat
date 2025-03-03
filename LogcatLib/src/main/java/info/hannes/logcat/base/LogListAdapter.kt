package info.hannes.logcat.base

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import info.hannes.logcat.R
import java.util.*

class LogListAdapter(private val completeLogs: ArrayList<String>, filter: String) : RecyclerView.Adapter<LogListAdapter.LogViewHolder>() {
    var filterLogs: List<String> = ArrayList()

    init {
        setFilter(filter)
    }

    fun setFilter(vararg filters: String) {
        filterLogs = completeLogs.filter { line ->
            var include = false
            for (filter in filters)
                if (!include && line.contains(filter))
                    include = true
            include
        }
        notifyDataSetChanged()
    }

    /**
     * Define the view for each log in the list
     */
    class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val logContent: TextView = view.findViewById(R.id.logLine)
    }

    /**
     * Create the view for each log in the list
     *
     * @param viewGroup
     * @param i
     * @return
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LogViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_log, viewGroup, false)
        return LogViewHolder(view)
    }

    /**
     * Fill in each log in the list
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.logContent.text = filterLogs[position]
        filterLogs[position].let {
            if (it.contains(" ${LogBaseFragment.ERROR_LINE}") || it.startsWith(LogBaseFragment.ERROR_LINE)) {
                holder.logContent.setTextColor(Color.RED)
            } else if (it.contains(" ${LogBaseFragment.ASSERT_LINE}") || it.startsWith(LogBaseFragment.ASSERT_LINE)) {
                holder.logContent.setTextColor(Color.RED)
            } else if (it.contains(" ${LogBaseFragment.INFO_LINE}") || it.startsWith(LogBaseFragment.INFO_LINE)) {
                holder.logContent.setTextColor(Color.BLACK)
            } else if (it.contains(" ${LogBaseFragment.WARNING_LINE}") || it.startsWith(LogBaseFragment.WARNING_LINE)) {
                holder.logContent.setTextColor(Color.MAGENTA)
            } else if (it.contains(" ${LogBaseFragment.VERBOSE_LINE}") || it.startsWith(LogBaseFragment.VERBOSE_LINE)) {
                holder.logContent.setTextColor(Color.GRAY)
//        } else {
//            holder.logContent.setTextColor(ContextCompat.getColor(context, R.color.primary_dark))
            }
        }
        when {
            filterLogs[position].contains(" E: ") -> holder.logContent.setTextColor(Color.RED)
            filterLogs[position].contains(" W: ") -> holder.logContent.setTextColor(Color.MAGENTA)
            filterLogs[position].contains(" V: ") -> holder.logContent.setTextColor(Color.GRAY)
//        } else {
//            holder.logContent.setTextColor(ContextCompat.getColor(context, R.color.primary_dark))
        }
    }

    override fun getItemCount(): Int {
        return filterLogs.size
    }

}
