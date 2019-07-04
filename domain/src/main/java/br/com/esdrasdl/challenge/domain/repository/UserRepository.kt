package br.com.esdrasdl.challenge.domain.repository

interface UserRepository {
    fun login(username: String, password: String)
    fun hasUserInfo(): Boolean
}