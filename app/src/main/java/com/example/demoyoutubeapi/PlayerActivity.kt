package com.example.demoyoutubeapi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoyoutubeapi.adapter.SearchAdapter
import com.example.demoyoutubeapi.data.InfoResponse
import com.example.demoyoutubeapi.data.SearchResponse
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : YouTubeBaseActivity() {

    private var videoId: String? = null

    private val service = Retrofit.instance.create(UserServices::class.java)

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val adapter: SearchAdapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        videoId = intent.extras?.getString("videoId")

        service.searchInfoVideo(Constant.API_KEY, "statistics,snippet", videoId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<InfoResponse> {
                lateinit var disposable: Disposable
                override fun onComplete() {
                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                    compositeDisposable.add(disposable)
                }

                @SuppressLint("SetTextI18n")
                override fun onNext(t: InfoResponse) {
                    infoTextView.text =
                            "Title: "+ t.items[0].snippet.title +"\n"+
                            "Comment count: "+ t.items[0].statistics.commentCount +"\n"+
                            "Dislike count: "+ t.items[0].statistics.dislikeCount +"\n"+
                            "Like count: "+ t.items[0].statistics.likeCount +"\n"+
                            "View count: "+ t.items[0].statistics.viewCount +"\n"+
                            "Description: "+ t.items[0].snippet.description+"\n"


                }

                override fun onError(e: Throwable) {
                }

            })

        service.searchRelatedtoVideoId(Constant.API_KEY, videoId!!, "snippet", "video", 10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SearchResponse> {
                lateinit var disposable: Disposable
                override fun onComplete() {
                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                    compositeDisposable.add(disposable)
                }

                override fun onNext(t: SearchResponse) {
                    adapter.setData(t.items)
                }

                override fun onError(e: Throwable) {

                }

            })


        listItem.layoutManager = LinearLayoutManager(this)
        listItem.adapter = adapter

        youtubeView.initialize(Constant.API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
                if (!p2) {
                    p1!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    //load the video
                    p1.loadVideo(videoId)
                }
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}