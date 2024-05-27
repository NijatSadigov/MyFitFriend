package com.example.myfitfriend.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.myfitfriend.connectivity.ConnectivityObserver
import com.example.myfitfriend.connectivity.NetworkConnectivityObserver
import com.example.myfitfriend.data.local.DAO.DietaryLogDAO
import com.example.myfitfriend.data.local.db.LocalDB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.ClearDietaryLogsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.SetDietaryLogsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.ClearExercisesUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.DeleteExerciseByIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.GetExerciseByWorkoutIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.SetExercisesUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.GetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.SetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.ClearWorkoutsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.GetWorkoutsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.SetWorkoutsUseCaseLB
import com.example.myfitfriend.data.remote.BasicAuthInterceptor
import com.example.myfitfriend.data.remote.MyFitFriendAPI
import com.example.myfitfriend.data.repository.MyFitFriendLocalRepositoryIMPL
import com.example.myfitfriend.data.repository.MyFitFriendRepositoryIMPL
import com.example.myfitfriend.domain.repository.MyFitFriendLocalRepository
import com.example.myfitfriend.domain.repository.MyFitFriendRepository
import com.example.myfitfriend.domain.use_case.Workout.CreateWorkoutUseCase
import com.example.myfitfriend.domain.use_case.Workout.DeleteWorkoutByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.GetWorkoutsUseCase
import com.example.myfitfriend.domain.use_case.Workout.exercise.AddExerciseToWorkoutUseCase
import com.example.myfitfriend.domain.use_case.Workout.exercise.DeleteExerciseByIdUseCase
import com.example.myfitfriend.domain.use_case.Workout.exercise.GetExercisesUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.DeleteDietaryLogUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.GetDietaryLogsUseCase
import com.example.myfitfriend.domain.use_case.dietarylogs.InsertDietaryLogCase
import com.example.myfitfriend.domain.use_case.sync.delete.DeleteDeletionTableUseCaseLB
import com.example.myfitfriend.domain.use_case.sync.delete.GetDeletionTableUseCaseLB
import com.example.myfitfriend.domain.use_case.users.EditProfileUserCase
import com.example.myfitfriend.domain.use_case.users.ProfileUserCase
import com.example.myfitfriend.util.Constants.BASE_URL
import com.example.myfitfriend.util.Constants.ENCRYPTED_SHARED_PREF_NAME
import com.example.myfitfriend.util.SyncOperationsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.annotation.Signed
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBasicAuthInterceptor() = BasicAuthInterceptor()

    @Singleton
    @Provides
    fun provideMyFitFriendApi(
        basicAuthInterceptor: BasicAuthInterceptor
    ): MyFitFriendAPI {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MyFitFriendAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMyFitFriendRepository(api: MyFitFriendAPI):
            MyFitFriendRepository = MyFitFriendRepositoryIMPL(api)

    @Singleton
    @Provides
    fun provideMyFitFriendLocalRepository(dao: DietaryLogDAO):
            MyFitFriendLocalRepository = MyFitFriendLocalRepositoryIMPL(dao)

    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDB {
        return Room.databaseBuilder(
            context,
            LocalDB::class.java,
            "Local"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSyncOperationsUtil(
        getUserUseCaseLB: GetUserUseCaseLB,
        editProfileUserCase: EditProfileUserCase,
        getDeletionTableUseCaseLB: GetDeletionTableUseCaseLB,
        deletionTableUseCaseLB: DeleteDeletionTableUseCaseLB,
        deleteDietaryLogByIdUseCase:DeleteDietaryLogUseCase,
        repository: MyFitFriendLocalRepository,
        getDietaryLogsUseCase:GetDietaryLogsUseCase,
        insertDietaryLogCase: InsertDietaryLogCase,
        setDietaryLogsUseCaseLB: SetDietaryLogsUseCaseLB,
        clearDietaryLogsUseCaseLB: ClearDietaryLogsUseCaseLB,
        getWorkoutsUseCaseLB: GetWorkoutsUseCaseLB,
        getExerciseByWorkoutIdUseCaseLB: GetExerciseByWorkoutIdUseCaseLB,
        clearWorkoutsUseCaseLB: ClearWorkoutsUseCaseLB,
        clearExercisesUseCaseLB: ClearExercisesUseCaseLB,
        createWorkoutUseCase: CreateWorkoutUseCase,
        addExerciseToWorkoutUseCase: AddExerciseToWorkoutUseCase,
        setWorkoutsUseCaseLB: SetWorkoutsUseCaseLB,
        getExercisesUseCase: GetExercisesUseCase,
        getWorkoutsUseCase: GetWorkoutsUseCase,
        setExercisesUseCaseLB: SetExercisesUseCaseLB,
        deleteWorkoutByIdUseCase: DeleteWorkoutByIdUseCase,
        deleteExerciseByIdUseCase: DeleteExerciseByIdUseCase,
        getUserCase: ProfileUserCase,
        setUserUseCaseLB: SetUserUseCaseLB
    ): SyncOperationsUtil {
        return SyncOperationsUtil(
            getUserUseCaseLB,
            editProfileUserCase,
            getDeletionTableUseCaseLB,
            deletionTableUseCaseLB,

            repository,
            deleteDietaryLogByIdUseCase,

            getDietaryLogsUseCase = getDietaryLogsUseCase,
            insertDietaryLogCase = insertDietaryLogCase,
            setDietaryLogsUseCaseLB=setDietaryLogsUseCaseLB,
            clearDietaryLogsUseCaseLB=clearDietaryLogsUseCaseLB,
            getWorkoutsUseCaseLB = getWorkoutsUseCaseLB,
            addExerciseToWorkoutUseCase = addExerciseToWorkoutUseCase,
            clearWorkoutsUseCaseLB = clearWorkoutsUseCaseLB,
            clearExercisesUseCaseLB = clearExercisesUseCaseLB,
            createWorkoutUseCase = createWorkoutUseCase,
            getExerciseByWorkoutIdUseCaseLB = getExerciseByWorkoutIdUseCaseLB,
            getExercisesUseCase = getExercisesUseCase,
            setExercisesUseCaseLB = setExercisesUseCaseLB,
            getWorkoutsUseCase = getWorkoutsUseCase,
            setWorkoutsUseCaseLB = setWorkoutsUseCaseLB,
            deleteWorkoutByIdUseCase = deleteWorkoutByIdUseCase,
            deleteExerciseByIdUseCase=deleteExerciseByIdUseCase,
            getUserCase=getUserCase,
            setUserCaseLB = setUserUseCaseLB

        )
    }

    @Singleton
    @Provides
    fun provideDietaryLogDao(db: LocalDB): DietaryLogDAO {
        return db.dietaryLogDao()
    }



}