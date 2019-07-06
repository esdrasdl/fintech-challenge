package br.com.esdrasdl.challenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.com.esdrasdl.challenge.AppSchedulerProvider
import br.com.esdrasdl.challenge.BuildConfig
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.data.order.OrderRepo
import br.com.esdrasdl.challenge.data.token.TokenRepo
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.GetOrders
import br.com.esdrasdl.challenge.local.repository.TokenLocalRepository
import br.com.esdrasdl.challenge.model.OrderItem
import br.com.esdrasdl.challenge.presentation.viewmodel.OrderListViewModel
import br.com.esdrasdl.challenge.remote.api.OrderAPI
import br.com.esdrasdl.challenge.remote.repository.OrderRemoteRepository
import br.com.esdrasdl.challenge.remote.service.ApiServiceFactory
import br.com.esdrasdl.challenge.ui.adapter.OrderListAdapter
import butterknife.BindView
import butterknife.ButterKnife

class OrderListActivity : AppCompatActivity() {

    @BindView(R.id.order_list)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.order_progressbar)
    lateinit var progress: ContentLoadingProgressBar

    private var viewModel: OrderListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        ButterKnife.bind(this)

        val authInterceptor = ApiServiceFactory.makeRequestInterceptor(TokenRepo(TokenLocalRepository()))

        val client = ApiServiceFactory.makeOkHttpClient(
            httpLoggingInterceptor = ApiServiceFactory.makeLoggingInterceptor(BuildConfig.DEBUG),
            authInterceptor = authInterceptor
        )
        val api = ApiServiceFactory.create(clazz = OrderAPI::class.java, client = client)

        val remote = OrderRemoteRepository(api)
        val repository = OrderRepo(remote)
        val getOrders = GetOrders(repository, AppSchedulerProvider())

        viewModel = OrderListViewModel(getOrders)

        handleState()
    }

    private fun handleState() {
        lifecycle.addObserver(viewModel!!)
        viewModel?.getState()?.observe(this, Observer { state ->
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

}
