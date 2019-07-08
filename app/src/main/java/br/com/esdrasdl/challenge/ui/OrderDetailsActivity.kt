package br.com.esdrasdl.challenge.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import br.com.concrete.canarinho.formatador.FormatadorValor
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.domain.model.OperationType
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.OrderStatus
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.presentation.viewmodel.OrderDetailsViewModel
import butterknife.BindView
import butterknife.ButterKnife
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetailsActivity : AppCompatActivity() {

    private val viewModel: OrderDetailsViewModel by viewModel()

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.order_detail_value)
    lateinit var totalAmountView: TextView

    @BindView(R.id.order_detail_payment_method)
    lateinit var paymentMethodView: TextView

    @BindView(R.id.order_detail_own_id)
    lateinit var orderOwnIdView: TextView

    @BindView(R.id.order_detail_id)
    lateinit var orderDetailIdView: TextView

    @BindView(R.id.order_detail_buyer_name)
    lateinit var buyerNameView: TextView

    @BindView(R.id.order_detail_buyer_email)
    lateinit var buyerEmailView: TextView

    @BindView(R.id.order_detail_created_at)
    lateinit var createdAtView: TextView

    @BindView(R.id.order_detail_updated_at)
    lateinit var updatedAtView: TextView

    @BindView(R.id.order_detail_status)
    lateinit var statusView: TextView

    @BindView(R.id.order_detail_total_value)
    lateinit var orderResumeTotalValueView: TextView

    @BindView(R.id.order_detail_fee)
    lateinit var orderResumeFeeValueView: TextView

    @BindView(R.id.order_detail_liquid_value)
    lateinit var orderResumeLiquidValueView: TextView

    @BindView(R.id.order_detail_payment_amount)
    lateinit var paymentNumbersView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        ButterKnife.bind(this)

        setupToolbar()
        setSupportActionBar(toolbar)

        val id = intent?.getStringExtra(EXTRA_ORDER_ID) ?: ""
        handleState()

        viewModel.init(id)

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Detalhes do pedido"
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleState() {
        lifecycle.addObserver(viewModel)
        viewModel.getState().observe(this, Observer { state ->
            when (state.status) {
                ViewState.Status.LOADING -> {
                }
                ViewState.Status.SUCCESS -> {
                    val order = state.data as Order
                    setupOrderDetails(order)
                }
                ViewState.Status.ERROR -> {

                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupOrderDetails(order: Order) {
        val moneyFormatter = FormatadorValor.VALOR_COM_SIMBOLO
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        orderDetailIdView.text = order.id
        orderOwnIdView.text = order.ownId
        totalAmountView.text = moneyFormatter.formata(order.totalAmount.toString())
        buyerNameView.text = order.buyerName
        buyerEmailView.text = order.buyerEmail
        statusView.text = when (order.currentStatus) {
            OrderStatus.PAID -> getString(R.string.order_status_paid)
            OrderStatus.WAITING -> getString(R.string.order_status_waiting)
            OrderStatus.REVERTED -> getString(R.string.order_status_reverted)
            OrderStatus.CREATED -> getString(R.string.order_status_created)
            OrderStatus.NOT_PAID -> getString(R.string.order_status_not_paid)
        }
        createdAtView.text = sdf.format(order.createdAt)
        updatedAtView.text = sdf.format(order.currentStatusDate)
        orderResumeTotalValueView.text = "+ ${moneyFormatter.formata(order.totalAmount.toString())}"
        orderResumeFeeValueView.text = "- ${moneyFormatter.formata(order.fee.toString())}"
        orderResumeLiquidValueView.text = "= ${moneyFormatter.formata(order.liquidValue.toString())}"
        val numberOfPayments = order.numberOfPayments ?: 1
        paymentNumbersView.text =
            resources.getQuantityString(R.plurals.payments, numberOfPayments, numberOfPayments)
        paymentMethodView.text =
            if (order.operation == OperationType.BOLETO) {
                getString(R.string.boleto)
            } else {
                getString(R.string.credit_card)
            }
    }

    companion object {
        const val EXTRA_ORDER_ID = "br.com.esdrasdl.challenge.extra.EXTRA_ORDER_ID"
    }
}
