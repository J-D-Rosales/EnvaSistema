package com.example.envasistema.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface KardexApi {

    @POST("/api/kardex/movimientos")
    suspend fun registerMovement(
        @Body movement: NetworkMovementDto
    ): Response<Unit>
}
