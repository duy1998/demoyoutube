package com.example.demoyoutubeapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoyoutubeapi.adapter.PlaylistAdapter
import com.example.demoyoutubeapi.playlist.PlaylistResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val service = Retrofit.instance.create(UserServices::class.java)
    private var adapter : PlaylistAdapter =
        PlaylistAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        listItem.layoutManager = LinearLayoutManager(this)
        listItem.adapter = adapter
        service.getPlaylists(" Bearer "+Constant.accessToken,
            "application/json",
            true,
            "snippet",
            Constant.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PlaylistResponse>{
                lateinit var disposable: Disposable
                override fun onSubscribe(d: Disposable) {
                    disposable=d
                    Log.d("HomeActivity", Constant.accessToken)
                }

                override fun onNext(t: PlaylistResponse) {
                    adapter.setData(t.items)
                    Log.d("HomeActivity", "Thanh Cong")
                }

                override fun onError(e: Throwable) {
                    Log.d("HomeActivity", e.toString())
                }

                override fun onComplete() {
                    disposable.dispose()
                }

            })

    }
}
