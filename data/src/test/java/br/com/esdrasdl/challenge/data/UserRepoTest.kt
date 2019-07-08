package br.com.esdrasdl.challenge.data

import br.com.esdrasdl.challenge.data.login.UserLocalDataSource
import br.com.esdrasdl.challenge.data.login.UserRemoteDataSource
import br.com.esdrasdl.challenge.data.login.UserRepo
import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import br.com.esdrasdl.challenge.domain.model.Token
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class UserRepoTest {

    private lateinit var repository: UserRepo

    private val remote: UserRemoteDataSource = mock()
    private val local: UserLocalDataSource = mock()

    @Before
    fun setUp() {
        repository = UserRepo(localSource = local, remoteSource = remote)
    }

    @Test
    fun testHasUserInfoTrue() {
        whenever(local.hasUserInfo()).thenReturn(true)
        val result = repository.hasUserInfo()
        assertEquals(true, result)
    }

    @Test
    fun testHasUserInfoFalse() {
        whenever(local.hasUserInfo()).thenReturn(false)
        val result = repository.hasUserInfo()
        assertEquals(false, result)
    }

    @Test
    fun testLoadUserInfo() {
        val expectedUserInfo = BasicUserInfo("username", "password")
        whenever(local.loadUserInfo()).thenReturn(Single.just(expectedUserInfo))

        val test = repository.loadUserInfo().test()

        verify(local, times(1)).loadUserInfo()

        test.assertNoErrors()
        test.assertValue(expectedUserInfo)
    }

    @Test
    fun testLogin() {
        val expectedToken = Token("accessToken")
        val username = "username"
        val password = "password"

        whenever(remote.login(anyString(), anyString())).thenReturn(Observable.just(expectedToken))

        val test = repository.login(username, password).test()
        verify(local, times(1)).saveUserInfo(eq(BasicUserInfo(username, password)))
        test.assertNoErrors()
        test.assertValue(expectedToken)
    }

}