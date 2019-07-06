package br.com.esdrasdl.challenge.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.model.OrderItem
import butterknife.ButterKnife

class OrderListAdapter(private val list: List<OrderItem>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.view_order_item, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var amountField: TextView
        lateinit var emailField: TextView
        lateinit var currentStatusField: TextView
        lateinit var dateField: TextView
        lateinit var ownIdField: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(item: OrderItem) {

        }

    }

}