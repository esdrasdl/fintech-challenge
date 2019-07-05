package br.com.esdrasdl.challenge.remote.mapper

import br.com.esdrasdl.challenge.domain.model.Summary
import br.com.esdrasdl.challenge.remote.response.SummaryResponse

object SummaryMapper : Mapper<SummaryResponse, Summary> {

    override fun toDomain(entity: SummaryResponse): Summary {
        return Summary(
            amountOfOrders = entity.count,
            balance = entity.amount.toDouble() / 100
        )
    }
}
