package com.example.demoyoutubeapi

import com.example.demoyoutubeapi.data.InfoResponse
import com.example.demoyoutubeapi.data.OAuthToken
import com.example.demoyoutubeapi.data.SearchResponse
import com.example.demoyoutubeapi.playlist.PlaylistItem
import com.example.demoyoutubeapi.playlist.PlaylistResponse
import com.example.demoyoutubeapi.playlistitem.PlaylistItemResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
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

    @GET("youtube/v3/playlists")
    fun getPlaylists(
        @Header("Authorization") authHeader:String,
        @Header("Accept") accept:String,
        @Query("maxResults") max: Int,
        @Query("mine") mine: Boolean,
        @Query("part") part: String,
        @Query("key") apiKey: String

    ):Observable<PlaylistResponse>

    @POST("youtube/v3/playlistItems")
    fun insertPlaylistItems(@Query("part") part: String,
                            @Query("key") key: String,
                            @Query("access_token") accessToken: String,
                            @Body item: PlaylistItem
    ) : Observable<ResponseBody>

    @DELETE ("youtube/v3/playlistItems")
    fun deletePlaylistItems(@Header("Authorization") authHeader:String,
                            @Header("Accept") accept:String,
                            @Query ("id") id:String,
                            @Query("key") key :String
    ) : Observable<retrofit2.Response<Void>>

    @GET("https://accounts.google.com/o/oauth2/revoke")
    fun revokeToken(@Query("token") token:String
    ) : Observable<ResponseBody>

    @GET("youtube/v3/playlistItems")
    fun getPlaylistItemById(@Header("Authorization") authHeader:String,
                            @Query("part") part: String,
                            @Query("maxResults") max:Int,
                            @Query("playlistId") playlistId: String,
                            @Query("key") key :String
    ) : Observable<PlaylistItemResponse>


}