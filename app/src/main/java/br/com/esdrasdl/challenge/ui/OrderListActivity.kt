package br.com.esdrasdl.challenge.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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

    @BindView(R.id.order_list_header)
    lateinit var headerContainer: ConstraintLayout

    @BindView(R.id.order_list)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.order_progressbar)
    lateinit var progress: ProgressBar

    private val viewModel: OrderListViewModel by viewModel()

    private var listOfOrderItem = ArrayList<OrderItem>()

    private var adapter: OrderListAdapter? = null

    private lateinit var layoutManager: LinearLayoutManager

    private val toolbarElevation = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val isRecyclerViewScrolling = newState == RecyclerView.SCROLL_STATE_DRAGGING &&
                    ViewCompat.getElevation(headerContainer) != -1f

            if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                layoutManager.findFirstVisibleItemPosition() == 0 &&
                layoutManager.findViewByPosition(0)!!.top == recyclerView.paddingTop &&
                ViewCompat.getElevation(headerContainer) != 0f
            ) {
                ViewCompat.setElevation(headerContainer, 0f)
            } else {
                if (isRecyclerViewScrolling) {
                    // grid scrolled, lower toolbar to allow content to pass in front
                    ViewCompat.setElevation(headerContainer, -1f)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        ButterKnife.bind(this)
        val shouldDoLogin = intent?.getBooleanExtra(EXTRA_DO_LOGIN, false) ?: false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = null
        }

        handleState()
        if (savedInstanceState == null) {
            viewModel.init(shouldDoLogin)
        } else {
            listOfOrderItem = savedInstanceState.getParcelableArrayList(STATE_ORDER_LIST)
        }

        setupRecyclerView(listOfOrderItem)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_ORDER_LIST, listOfOrderItem)
    }

    private fun handleState() {
        lifecycle.addObserver(viewModel)
        viewModel.getState().observe(this, Observer { state ->
            when (state.status) {
                ViewState.Status.LOADING -> {
                    progress.visibility = View.VISIBLE
                }
                ViewState.Status.SUCCESS -> {
                    progress.visibility = View.GONE

                    val listOfOrders = state.data as List<Order>

                    listOfOrderItem = listOfOrders.map {
                        OrderItem(
                            it.id,
                            it.ownId,
                            it.buyerEmail,
                            it.currentStatus.value,
                            it.currentStatusDate,
                            it.totalAmount
                        )
                    }.let {
                        ArrayList(it)
                    }
                    adapter?.addItems(listOfOrderItem)
                }
                ViewState.Status.ERROR -> {
                    progress.visibility = View.GONE
                }
            }
        })
    }

    private fun setupRecyclerView(list: ArrayList<OrderItem>) {
        layoutManager = LinearLayoutManager(this)
        adapter = OrderListAdapter(list) { id ->
            val intent = Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra(OrderDetailsActivity.EXTRA_ORDER_ID, id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.addOnScrollListener(toolbarElevation)
            headerContainer.doOnLayout {
                val topPadding = headerContainer.height * 1.15
                recyclerView.setPadding(0, topPadding.toInt(), 0, 0)
            }
        }
    }

    companion object {
        const val EXTRA_DO_LOGIN = "br.com.esdrasdl.challenge.extra.EXTRA_DO_LOGIN"
        private const val STATE_ORDER_LIST = "STATE_ORDER_LIST"
    }
}
