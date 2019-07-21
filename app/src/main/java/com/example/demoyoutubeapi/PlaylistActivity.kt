package com.example.demoyoutubeapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoyoutubeapi.adapter.PlaylistItemAdapter
import com.example.demoyoutubeapi.data.SearchResponse
import com.example.demoyoutubeapi.playlistitem.PlaylistItemResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_playlist.*

class PlaylistActivity : AppCompatActivity() {

    private var adapter: PlaylistItemAdapter = PlaylistItemAdapter()

    private val service = Retrofit.instance.create(UserServices::class.java)

    private var id :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        id = intent.extras?.getString("ID")

        listItem.layoutManager = LinearLayoutManager(this)
        listItem.adapter = adapter

        Log.d("PlaylistActivity",Constant.accessToken)
        Log.d("PlaylistActivity",id)
        Log.d("PlaylistActivity",Constant.API_KEY)
        service.getPlaylistItemById("Bearer "+Constant.accessToken,"snippet",25,id!!,Constant.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PlaylistItemResponse> {
                lateinit var disposable: Disposable
                override fun onComplete() {
                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: PlaylistItemResponse) {
                    adapter.setData(t.items)
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", e.toString())
                }
            })

    }
}
