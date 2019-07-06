package br.com.esdrasdl.challenge.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.canarinho.formatador.FormatadorValor
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.model.OrderItem
import butterknife.BindView
import butterknife.ButterKnife
import java.text.SimpleDateFormat
import java.util.Locale

class OrderListAdapter(private val list: List<OrderItem>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {


    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.view_order_item, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.order_item_value)
        lateinit var amountField: TextView

        @BindView(R.id.order_item_email)
        lateinit var emailField: TextView

        @BindView(R.id.order_item_status)
        lateinit var currentStatusField: TextView

        @BindView(R.id.order_item_date)
        lateinit var dateField: TextView

        @BindView(R.id.order_item_ownid)
        lateinit var ownIdField: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(item: OrderItem) {
            amountField.text = FormatadorValor.VALOR_COM_SIMBOLO.formata(item.totalAmount.toString())
            emailField.text = item.buyerEmail
            currentStatusField.text = item.currentStatus
            dateField.text = dateFormatter.format(item.currentStatusDate)
            ownIdField.text = item.ownId
        }
    }
}