package com.example.demoyoutubeapi

import com.example.demoyoutubeapi.data.*
import io.reactivex.Observable
import retrofit2.http.*


interface UserServices {
    @GET("youtube/v3/search")
    fun searchByKeyWord(
        @Query("key") apiKey: String,
        @Query("part") part: String,
        @Query("q") order: String,
        @Query("maxResults") maxResults: Int,
        @Query("type") type:String
    ): Observable<SearchResponse>

    @GET("youtube/v3/search")
    fun searchRelatedtoVideoId(
        @Query("key") apiKey: String,
        @Query("relatedToVideoId") relatedToVideoId:String,
        @Query("part") part: String,
        @Query("type") type:String,
        @Query("maxResults") maxResults: Int
    ): Observable<SearchResponse>

    @GET("youtube/v3/videos")
    fun searchInfoVideo(
        @Query("key") apiKey: String,
        @Query("part") part: String,
        @Query("id") id: String
    ): Observable<InfoResponse>

    @FormUrlEncoded
    @POST("oauth2/v4/token")
    fun requestToken(
        @Field("code") code : String,
        @Field("client_id") clientId : String,
        @Field("redirect_uri") redirectUri : String,
        @Field("grant_type") grant_type : String
    ): Observable<OAuthToken>


}