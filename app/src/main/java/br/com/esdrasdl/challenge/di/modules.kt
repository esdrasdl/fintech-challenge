package br.com.esdrasdl.challenge.di

import br.com.esdrasdl.challenge.AppSchedulerProvider
import br.com.esdrasdl.challenge.BuildConfig
import br.com.esdrasdl.challenge.data.login.UserLocalDataSource
import br.com.esdrasdl.challenge.data.login.UserRemoteDataSource
import br.com.esdrasdl.challenge.data.login.UserRepo
import br.com.esdrasdl.challenge.data.order.OrderRemoteDataSource
import br.com.esdrasdl.challenge.data.order.OrderRepo
import br.com.esdrasdl.challenge.data.token.TokenLocalDataSource
import br.com.esdrasdl.challenge.data.token.TokenRepo
import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.repository.OrderRepository
import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import br.com.esdrasdl.challenge.domain.repository.UserRepository
import br.com.esdrasdl.challenge.domain.usecase.DoLogin
import br.com.esdrasdl.challenge.domain.usecase.GetOrders
import br.com.esdrasdl.challenge.domain.usecase.SaveToken
import br.com.esdrasdl.challenge.local.repository.TokenLocalRepository
import br.com.esdrasdl.challenge.local.repository.UserLocalRepository
import br.com.esdrasdl.challenge.presentation.viewmodel.OrderListViewModel
import br.com.esdrasdl.challenge.presentation.viewmodel.SignInViewModel
import br.com.esdrasdl.challenge.remote.api.OrderAPI
import br.com.esdrasdl.challenge.remote.api.UserAPI
import br.com.esdrasdl.challenge.remote.repository.OrderRemoteRepository
import br.com.esdrasdl.challenge.remote.repository.UserRemoteRepository
import br.com.esdrasdl.challenge.remote.service.ApiServiceFactory
import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { AppSchedulerProvider() as SchedulerProvider }
}

val dataModule = module {
    single {
        ApiServiceFactory.makeLoggingInterceptor(BuildConfig.DEBUG)
    }
    single {
        GsonBuilder().create()
    }
    single {
        ApiServiceFactory.makeRequestInterceptor(get())
    }
    single {
        ApiServiceFactory.makeOkHttpClient(httpLoggingInterceptor = get(), authInterceptor = get())
    }
    single {
        ApiServiceFactory.create(UserAPI::class.java, endpoint = ApiServiceFactory.LOGIN_BASE_URL, client = get())
    }
    single {
        ApiServiceFactory.create(OrderAPI::class.java, client = get())
    }
    single {
        UserLocalRepository(gson = get()) as UserLocalDataSource
    }
    single {
        UserRemoteRepository(api = get()) as UserRemoteDataSource
    }
    single {
        UserRepo(localSource = get(), remoteSource = get()) as UserRepository
    }
    single {
        OrderRemoteRepository(api = get()) as OrderRemoteDataSource
    }
    single {
        OrderRepo(remoteSource = get()) as OrderRepository
    }
    single {
        TokenLocalRepository() as TokenLocalDataSource
    }
    single {
        TokenRepo(local = get()) as TokenRepository
    }
}
val domainModule = module {
    factory {
        DoLogin(repository = get(), executor = get())
    }
    factory {
        GetOrders(repository = get(), executor = get())
    }
    factory {
        SaveToken(repository = get(), executor = get())
    }
}

val presentationModule = module {
    viewModel {
        SignInViewModel(doLogin = get(), saveToken = get())
    }
    viewModel {
        OrderListViewModel(getOrders = get())
    }
}
