package com.example.geologger.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM location_table ORDER BY id DESC")
    fun getAllLocations(): Flow<List<LocationEntity>>


}
