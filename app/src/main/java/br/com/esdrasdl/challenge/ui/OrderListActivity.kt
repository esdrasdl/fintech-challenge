package br.com.esdrasdl.challenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.model.OrderItem
import br.com.esdrasdl.challenge.presentation.viewmodel.OrderListViewModel
import br.com.esdrasdl.challenge.ui.adapter.OrderListAdapter
import butterknife.BindView
import butterknife.ButterKnife
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderListActivity : AppCompatActivity() {

    @BindView(R.id.order_list)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.order_progressbar)
    lateinit var progress: ContentLoadingProgressBar

    private val viewModel: OrderListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        ButterKnife.bind(this)
        val shouldDoLogin = intent?.getBooleanExtra(EXTRA_DO_LOGIN, false) ?: false

        handleState()
        viewModel.init(shouldDoLogin)
    }

    private fun handleState() {
        lifecycle.addObserver(viewModel)
        viewModel.getState().observe(this, Observer { state ->
            when (state.status) {
                ViewState.Status.LOADING -> {
                    progress.show()
                }
                ViewState.Status.SUCCESS -> {
                    progress.hide()
                    val list = state.data as List<Order>
                    recyclerView.adapter = OrderListAdapter(list.map {
                        OrderItem(
                            it.ownId,
                            it.buyerEmail,
                            it.currentStatus,
                            it.currentStatusDate,
                            it.totalAmount
                        )
                    })
                }
                ViewState.Status.ERROR -> {
                    progress.hide()
                }
            }
        })
    }

    companion object {
        const val EXTRA_DO_LOGIN = "br.com.esdrasdl.challenge.extra.EXTRA_DO_LOGIN"
    }
}
