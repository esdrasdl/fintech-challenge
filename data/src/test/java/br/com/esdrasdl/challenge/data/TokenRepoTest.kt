package br.com.esdrasdl.challenge.data

import br.com.esdrasdl.challenge.data.token.TokenLocalDataSource
import br.com.esdrasdl.challenge.data.token.TokenRepo
import br.com.esdrasdl.challenge.domain.model.Token
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TokenRepoTest {

    private lateinit var repository: TokenRepo
    private val local: TokenLocalDataSource = mock()

    @Before
    fun setUp() {
        repository = TokenRepo(local)
    }

    @Test
    fun testSaveToken() {
        val token = Token("accessToken")

        repository.saveToken(token)

        verify(local, times(1)).saveToken(eq(token))
    }

    @Test
    fun testLoadToken() {
        val token = Token("accessToken")
        whenever(local.getToken()).thenReturn(token)
        assertEquals(token, repository.getToken())
    }

    @Test
    fun testSaveTokenAndGetCachedToken() {
        val token = Token("accessToken")

        repository.saveToken(token)
        val cachedToken = repository.getToken()

        verify(local, times(1)).saveToken(eq(token))
        verify(local, times(0)).getToken()
        assertEquals(token, cachedToken)
    }
}