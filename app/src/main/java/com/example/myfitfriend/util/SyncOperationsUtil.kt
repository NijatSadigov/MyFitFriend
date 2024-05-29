package com.example.myfitfriend.util

import androidx.lifecycle.viewModelScope
import com.MyFitFriend.data.model.Exercise
import com.MyFitFriend.requests.ExerciseRequest
import com.MyFitFriend.requests.WorkoutRequest
import com.example.myfitfriend.data.local.DAO.DietaryLogDAO
import com.example.myfitfriend.data.local.DietaryLogEntity
import com.example.myfitfriend.data.local.ExerciseEntity
import com.example.myfitfriend.data.local.WorkoutEntity
import com.example.myfitfriend.data.local.asDietaryLogEntity
import com.example.myfitfriend.data.local.asExerciseEntity
import com.example.myfitfriend.data.local.asUser
import com.example.myfitfriend.data.local.domain.use_case.user.GetUserUseCaseLB
import com.example.myfitfriend.domain.use_case.users.EditProfileUserCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.example.myfitfriend.data.local.asWorkoutEntity
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.ClearDietaryLogsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.dietary_log.SetDietaryLogsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.ClearExercisesUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.GetExerciseByWorkoutIdUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.exercise.SetExercisesUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.user.SetUserUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.ClearWorkoutsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.GetWorkoutsUseCaseLB
import com.example.myfitfriend.data.local.domain.use_case.workout.SetWorkoutsUseCaseLB
import com.example.myfitfriend.data.remote.reponses.User
import com.example.myfitfriend.data.remote.reponses.Workout
import com.example.myfitfriend.data.remote.requests.UserEditRequest
import com.example.myfitfriend.domain.repository.MyFitFriendLocalRepository
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
import com.example.myfitfriend.domain.use_case.users.ProfileUserCase
import kotlinx.coroutines.Job
import javax.inject.Inject

class SyncOperationsUtil
@Inject constructor  (
            private val    getUserUseCaseLB: GetUserUseCaseLB,
            private val editProfileUserCase: EditProfileUserCase,
            private val getDeletionTableUseCaseLB: GetDeletionTableUseCaseLB,
            private val deleteDeletionTableUseCaseLB: DeleteDeletionTableUseCaseLB,
            private val repository: MyFitFriendLocalRepository,
            private val deleteDietaryLogByIdUseCase: DeleteDietaryLogUseCase,
            private val insertDietaryLogCase: InsertDietaryLogCase,
            private val getDietaryLogsUseCase: GetDietaryLogsUseCase,
            private val setDietaryLogsUseCaseLB: SetDietaryLogsUseCaseLB,
            private val clearDietaryLogsUseCaseLB: ClearDietaryLogsUseCaseLB,
            private val getWorkoutsUseCaseLB: GetWorkoutsUseCaseLB,
            private val getExerciseByWorkoutIdUseCaseLB: GetExerciseByWorkoutIdUseCaseLB,
            private val clearWorkoutsUseCaseLB: ClearWorkoutsUseCaseLB,
            private val clearExercisesUseCaseLB: ClearExercisesUseCaseLB,
            private val createWorkoutUseCase: CreateWorkoutUseCase,
            private val addExerciseToWorkoutUseCase: AddExerciseToWorkoutUseCase,
            private val setWorkoutsUseCaseLB: SetWorkoutsUseCaseLB,
            private val getWorkoutsUseCase: GetWorkoutsUseCase,
            private val getExercisesUseCase: GetExercisesUseCase,
    private val setExercisesUseCaseLB: SetExercisesUseCaseLB,
    private val deleteWorkoutByIdUseCase: DeleteWorkoutByIdUseCase,
    private val deleteExerciseByIdUseCase: DeleteExerciseByIdUseCase,
    private val getUserCase: ProfileUserCase,
    private val setUserCaseLB: SetUserUseCaseLB
            )
{

    suspend fun syncDeletions(scope: CoroutineScope): Job {
        return scope.launch {
            val deletedLogs = repository.getDeletionTable()
            println("syncDeletions: $deletedLogs")

            for (i in deletedLogs) {
                when (i.typeOfItem) {
                    0 -> deleteDietaryLogFromServer(i.deletedId, this).join()
                    1 -> deleteWorkoutFromServer(i.deletedId, this).join()
                    2 -> deleteExerciseFromServer(i.deletedId, this).join()
                }
            }

            deleteDeletionTableUseCaseLB.invoke().launchIn(this).join()
        }
    }
    fun deleteWorkoutFromServer(id: Int, scope: CoroutineScope): Job {
        return scope.launch {
            println("deleted workout id $id")
            deleteWorkoutByIdUseCase.invoke(id).launchIn(this).join()
        }
    }

    fun deleteExerciseFromServer(id: Int, scope: CoroutineScope): Job {
        return scope.launch {
            println("deleted exercise id $id")
            deleteExerciseByIdUseCase(id).launchIn(this).join()
        }
    }

    fun deleteDietaryLogFromServer(id: Int, scope: CoroutineScope): Job {
        return scope.launch {
            deleteDietaryLogByIdUseCase.invoke(id).launchIn(this).join()
        }
    }


    suspend fun addUnSyncedLogs(scope: CoroutineScope): Job {
        val logsToAdded = repository.getDietaryLogsLB().filter { !it.isSync }
        return scope.launch {

            for (log in logsToAdded) {
                insertDietaryLogCase.invoke(
                    log
                ).launchIn(this)  // Note: Using this instead of scope to ensure it belongs to the same job
            }
        }
    }
    fun syncDietaryLogs(scope: CoroutineScope):Job {
    return    scope.launch {

            val job = addUnSyncedLogs(this)  // this refers to the CoroutineScope of the launch
            job.join()  // Wait until all un-synced logs are added
            getDietaryLogsOfUserFromServer(this)  // Proceed to fetch logs from server after sync
        }
    }
     fun getDietaryLogsOfUserFromServer(scope:CoroutineScope){
        scope.launch {
            getDietaryLogsUseCase.invoke().onEach{
                r->
                when(r){
                    is Resources.Error -> {

                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(r.data!=null){
                           val logs=r.data
                           val syncLogs= logs.map { it.asDietaryLogEntity() }.map { it.apply { isSync = true
                               isAdded=true

                           }}
                            println("got logs $syncLogs")
                               setDietaryLogs(scope,syncLogs )
                        }

                    }
                }
            }.launchIn(scope)


        }



    }
    suspend fun setDietaryLogs(scope:CoroutineScope, dietaryLogEntities: List<DietaryLogEntity>){
        scope.launch {
            clearDietaryLogsUseCaseLB.invoke().collect {
                r->
                when(r){
                    is Resources.Error -> {println("error")}
                    is Resources.Loading -> {println("loading")}
                    is Resources.Success -> {println("logs cleared")}
                }
            }
            setDietaryLogsUseCaseLB.invoke(dietaryLogEntities).collect {
                    r->
                when(r){
                    is Resources.Error -> {println("error")}
                    is Resources.Loading -> {println("loading")}
                    is Resources.Success -> {println("logs cleared")}
                }
            }
            println("setted logs")

        }
    }











     fun syncProfileDetails(
          scope: CoroutineScope,
        ):Job {
      return  scope.launch {
          lateinit var user:User

            getUserUseCaseLB.invoke().collect {
                    result->

                when(result){
                    is Resources.Error -> {
                        println(result.message)
                    }
                    is Resources.Loading -> {
                        //println(result.message)

                    }
                    is Resources.Success -> {
                        result.data?.let { userData ->
                            user = userData.asUser()



                        } ?: println("No user data available.")
                    }
                }

            }
          getUserCase.invoke().collect{
              r->
              when(r){
                  is Resources.Error -> {}
                  is Resources.Loading -> {}
                  is Resources.Success -> {
                      if(r.data!=null){
                          if (r.data.lastEditDate>user.lastEditDate)
                          {
                              //so server has newer data
                              println("so server has newer data")
                              setUserCaseLB.invoke(r.data).collect{
                                      res->when(res){
                                  is Resources.Error -> {}
                                  is Resources.Loading -> {}
                                  is Resources.Success -> {
                                      println("set data to lb done")
                                  }
                              }
                              }
                          }
                          else if(r.data.lastEditDate<user.lastEditDate){
                              //user have newer data
                              println("user have newer data")
                              editProfileUserCase.invoke(
                                  UserEditRequest(
                                  sex=user.sex,
                                      activityLevel = user.activityLevel,
                                      age = user.age,
                                      height = user.height,
                                      username = user.username,
                                      weight = user.weight,
                                      lastEditDate = user.lastEditDate
                              )
                              ).collect{
                                  res->when(res){
                                  is Resources.Error -> {}
                                  is Resources.Loading -> {}
                                  is Resources.Success -> {

                                      println("set data to server done")
                                  }
                              }
                              }
                          }
                          //data are same no need to do smth

                      }

                  }
              }
          }
        }
    }

    fun syncWorkouts(scope: CoroutineScope):Job {
      return  scope.launch {
            val job = addUnSyncedWorkoutsAndExercises(this)  // this refers to the CoroutineScope of the launch
            job.join()
            // Wait until all un-synced logs are added
            val job2=getSyncedWorkoutsAndExercises(this)
            job2.join()

            // Proceed to fetch logs from server after sync
        }
    }
    fun getSyncedWorkoutsAndExercises(scope: CoroutineScope):Job{
        return scope.launch {

            clearWorkoutsUseCaseLB.invoke().collect{
                    result->
                when(result){
                    is Resources.Error -> {
                        println(result.data)
                    }
                    is Resources.Loading -> {
                        println(result.data)


                    }
                    is Resources.Success -> {

                    }
                }

            }
            clearExercisesUseCaseLB.invoke().collect{
                    result->
                when(result){
                    is Resources.Error -> {
                        println(result.data)
                    }
                    is Resources.Loading -> {
                        println(result.data)


                    }
                    is Resources.Success -> {

                    }
                }

            }


            getWorkoutsUseCase.invoke().collect{
                r->
                when(r){
                    is Resources.Error ->{}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if (r.data != null){
                            val dataAsEntity= r.data.map {
                                it.asWorkoutEntity().copy(isSync = true, isAdded = true, workoutId = it.workoutId)

                            }
                            setWorkoutsUseCaseLB.invoke(workouts = dataAsEntity).collect { result ->
                                when (result) {
                                    is Resources.Error -> {}
                                    is Resources.Loading -> {}
                                    is Resources.Success -> {
                                    println("setting workouts done $dataAsEntity")
                                    }
                                }
                            }
                    }
                    }
                }
            }

            getExercisesUseCase.invoke().collect{
                r->
                when(r){
                    is Resources.Error -> {}
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        if(r.data!=null){
                            val exercisesAsEntity=r.data.map {
                                it.asExerciseEntity().copy(isSync = true, isAdded = true, exerciseId = it.exerciseId)
                            }
                        setExercisesUseCaseLB.invoke(exercisesAsEntity).collect{
                            when(it){
                                is Resources.Error -> {}
                                is Resources.Loading -> {}
                                is Resources.Success -> {
                                    print("exercises settet ${it.data}")
                                }
                            }
                        }

                        }}
                }
            }
        }
    }

    fun addUnSyncedWorkoutsAndExercises(scope: CoroutineScope):Job{
        return  scope.launch {
            val workouts=repository.getWorkouts().filter{it.isSync==false}
            val exercises=repository.getExercises().filter{it.isSync==false}
            val workoutExercisePairs: List<Pair<WorkoutEntity, List<ExerciseEntity>>> = workouts.map { workout ->
                // For each workout, find all exercises that have a matching workoutId
                val matchingExercises = exercises.filter { exercise ->
                    exercise.workoutId == workout.workoutId
                }
                // Create a pair of the workout and the list of matching exercises
                workout to matchingExercises
            }
            val unmatchedExercises = exercises.filter { exercise ->
                workouts.none { workout -> workout.workoutId == exercise.workoutId }
            }

            println("gotten data pairs: $workoutExercisePairs")
            println("gotten data unmatchedEx: $unmatchedExercises")

            for(pair in workoutExercisePairs){
                createWorkoutUseCase.invoke(pair.first).collect {
                    r->
                    when(r){
                        is Resources.Error -> {}
                        is Resources.Loading -> {}
                        is Resources.Success -> {
                            if(r.data!=null){
                                val workout:Workout=r.data
                                val workoutExercises=pair.second.map { it.copy(workoutId = workout.workoutId, isSync = true) }

                                for(exercise in workoutExercises){
                                    addExerciseToWorkoutUseCase.invoke(workout.workoutId,
                                        exercise
                                    ).collect{
                                        r->
                                        when(r){
                                            is Resources.Error -> {}
                                            is Resources.Loading -> {}
                                            is Resources.Success -> {
                                                 println("exercise added $exercise")
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    }
                }

            }
            for (exercise in unmatchedExercises){
                addExerciseToWorkoutUseCase.invoke(exercise.workoutId,exercise).onEach{
                    r->
                    when(r){
                        is Resources.Error -> println("eeror at unmatched exercises")
                        is Resources.Loading -> {}
                        is Resources.Success -> {
                            println("addeed unmatched ex: $exercise")
                        }
                    }
                }.launchIn(scope)
            }

        }
    }



}