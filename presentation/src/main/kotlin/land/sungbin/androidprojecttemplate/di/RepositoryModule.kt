package land.sungbin.androidprojecttemplate.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import land.sungbin.androidprojecttemplate.data.repository.auth.AuthRepository
import land.sungbin.androidprojecttemplate.data.repository.auth.AuthRepositoryImpl
import land.sungbin.androidprojecttemplate.data.repository.gallery.GalleryRepository
import land.sungbin.androidprojecttemplate.data.repository.gallery.GalleryRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun provideGalleryRepository(
        galleryRepositoryImpl: GalleryRepositoryImpl
    ): GalleryRepository
}