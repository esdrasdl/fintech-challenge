package br.com.esdrasdl.challenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.esdrasdl.challenge.R
import butterknife.BindView
import butterknife.ButterKnife

class OrderListActivity : AppCompatActivity() {

    @BindView(R.id.order_list)
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        ButterKnife.bind(this)
    }
}
