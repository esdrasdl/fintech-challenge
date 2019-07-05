package br.com.esdrasdl.challenge.remote.mapper

interface Mapper<E, D> {
    fun toDomain(entity: E): D
}
