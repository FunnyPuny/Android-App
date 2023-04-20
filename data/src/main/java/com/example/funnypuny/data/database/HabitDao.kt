package com.example.funnypuny.data.database

import androidx.room.*
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.WeekEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): Single<List<Habit>>
    @Query("SELECT * FROM habit")
    fun getAll(date: DateEntity): Single<List<Habit>>
    @Query("SELECT * FROM habit WHERE habit.day_of_week = :week")
    fun getAllDayOfWeek  (week: WeekEntity): Single<List<Habit>>
    @Query("SELECT * FROM habit WHERE habit.day = :-1 AND habit.month = :-1 AND habit.year = :-1")
    fun getAllEveryday(): Single<List<Habit>>

    @Insert
    fun insert(habit: Habit): Completable

    @Query("DELETE FROM habit WHERE habit.id = :id")
    fun delete(id: Int): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun edit(habit: Habit): Completable

    @Query("SELECT * FROM habit WHERE habit.id = :id")
    fun get(id: Int): Single<Habit>

    /*@Query(
        "SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1"
    )
    fun findByName(first: String, last: String): User*/

}



