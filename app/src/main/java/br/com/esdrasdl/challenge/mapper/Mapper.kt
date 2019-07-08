package br.com.esdrasdl.challenge.mapper

interface Mapper<E, D> {
    fun fromDomain(domain: D): E
}