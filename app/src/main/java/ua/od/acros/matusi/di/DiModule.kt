package ua.od.acros.matusi.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.od.acros.matusi.data.datasource.DbDataSource
import ua.od.acros.matusi.data.datasource.FirestoreDataSource
import ua.od.acros.matusi.data.datasource.LocationDataSource
import ua.od.acros.matusi.data.datasource.impl.DbDataSourceImpl
import ua.od.acros.matusi.data.datasource.impl.FirestoreDataSourceImpl
import ua.od.acros.matusi.data.datasource.impl.LocationDataSourceImpl
import ua.od.acros.matusi.data.db.AppDatabase
import ua.od.acros.matusi.data.mapper.*
import ua.od.acros.matusi.domain.repository.LocationRepository
import ua.od.acros.matusi.data.repository.LocationRepositoryImpl
import ua.od.acros.matusi.data.repository.UsersRepositoryImpl
import ua.od.acros.matusi.domain.repository.UsersRepository
import ua.od.acros.matusi.domain.usecase.*
import ua.od.acros.matusi.presentation.authorization.fragment.AuthorizationFragment
import ua.od.acros.matusi.presentation.authorization.fragment.EmailSignInFragment
import ua.od.acros.matusi.presentation.authorization.fragment.RegisterFragment
import ua.od.acros.matusi.presentation.authorization.fragment.SignInFragment
import ua.od.acros.matusi.presentation.authorization.vm.AuthorizationViewModel
import ua.od.acros.matusi.presentation.main.fragment.MainMenuFragment
import ua.od.acros.matusi.presentation.main.vm.MainApplicationViewModel
import ua.od.acros.matusi.presentation.settings.fragment.*
import ua.od.acros.matusi.presentation.settings.vm.UserSettingsViewModel

val settingsModule = module {
    fragment{ UserAccountFragment(get()) }
    fragment{ UserSettingsNameAndSocialFragment(get()) }
    fragment{ UserSettingsAddChildrenFragment(get()) }
    fragment{ UserSettingsUserLocationFragment(get()) }
    fragment{ UserSettingsUserLocationRadiusFragment(get()) }
    fragment{ UserSettingsUserScheduleFragment(get()) }
    viewModel { UserSettingsViewModel(get(), get(), get(), get(), get(), get()) }
}

val authorizationModule = module {
    viewModel { AuthorizationViewModel(get()) }
}

val mainModule = module {
    fragment{ MainMenuFragment() }
    viewModel { MainApplicationViewModel(get()) }
}

val repositoryModule = module {
    factory<UsersRepository> { UsersRepositoryImpl(get(), get(), get(), get()) }
    factory<LocationRepository> { LocationRepositoryImpl(get()) }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }
}

val dataSourceModule = module {
    factory<DbDataSource> { DbDataSourceImpl(get()) }
    factory<FirestoreDataSource> { FirestoreDataSourceImpl(get()) }
    factory<LocationDataSource> { LocationDataSourceImpl(get()) }
}

val useCaseModule = module {
    factory { GetCurrentLocationUseCase(get()) }
    factory { GetAddressFromLocationUseCase(get()) }
    factory { GetLocationFromAddressUseCase(get()) }
    factory { GetParentByIdUseCase(get()) }
    factory { InsertParentUseCase(get()) }
}

val mapperModule = module {
    factory { ParentMapper(get(), get()) }
    factory { ParentDtoMapper(get(), get()) }
    factory { ChildMapper() }
    factory { ChildDtoMapper() }
    factory { CareScheduleMapper() }
    factory { CareScheduleDtoMapper() }
}

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "matusi.db"
        )
            .build()
    }
    factory { get<AppDatabase>().parentDao() }
}

val appModules = listOf (
    dataSourceModule,
    repositoryModule,
    useCaseModule,
    mapperModule,
    authorizationModule,
    settingsModule,
    mainModule,
    dbModule
)