package br.com.esdrasdl.challenge.remote.api

import br.com.esdrasdl.challenge.remote.response.OrdersResponse
import br.com.esdrasdl.challenge.remote.response.SimpleOrderResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderAPI {

    @GET(ORDERS_API)
    fun getOrders(): Single<Response<OrdersResponse>>

    @GET("$ORDERS_API/{orderId}")
    fun getOrderById(@Path("orderId") orderId: String): Single<Response<SimpleOrderResponse>>

    companion object {
        const val ORDERS_API = "/v2/orders"
    }
}