package com.example.funnypuny.data.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): Single<List<Habit>>

    @Insert
    fun insertAll(habit: Habit): Completable

    @Query("DELETE FROM habit WHERE habit.id = :id")
    fun delete(id: Int): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun edit(habit: Habit): Completable

    /*@Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>*/

    /*@Query(
        "SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1"
    )
    fun findByName(first: String, last: String): User*/

}



