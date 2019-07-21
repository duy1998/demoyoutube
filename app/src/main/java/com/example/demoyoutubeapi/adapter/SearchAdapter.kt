package com.example.demoyoutubeapi.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.demoyoutubeapi.*
import com.example.demoyoutubeapi.data.SearchItem
import com.example.demoyoutubeapi.playlist.PlaylistItem
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_item_info_video.view.*
import okhttp3.ResponseBody


class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items:MutableList<SearchItem> = mutableListOf()

    private var service = Retrofit.instance.create(UserServices::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_item_info_video,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchViewHolder).bind(items[position])
    }

    fun setData(list: List<SearchItem>) {
        items.clear()
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addMoreData(list: List<SearchItem>?){
        val currCount = items.size
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
            notifyItemRangeInserted(currCount, list.size)
        }
    }

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleTextView: TextView = itemView.title

        private var channelTextView:TextView = itemView.channel

        private var videoThumbnails: YouTubeThumbnailView  = itemView.thumbnail

        private var youTubeThumbnailLoader: YouTubeThumbnailLoader? = null

        private var addButton: Button = itemView.addButton

//        private var deleteButton: Button = itemView.deleteButton

        fun bind(item: SearchItem){
            titleTextView.text = item.snippet.title
            channelTextView.text = item.snippet.channelTitle
            videoThumbnails.tag = item.id
            videoThumbnails.initialize(Constant.API_KEY,object :YouTubeThumbnailView.OnInitializedListener{
                override fun onInitializationSuccess(p0: YouTubeThumbnailView?, p1: YouTubeThumbnailLoader?) {
                    Toast.makeText(itemView.context, "YouTubeThumbnailView.onInitializationSuccess()", Toast.LENGTH_LONG).show()
                    p1!!.setVideo(item.id.channelId)
                    p1.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener{
                        override fun onThumbnailLoaded(p0: YouTubeThumbnailView?, p2: String?) {
                            p1.release()
                        }

                        override fun onThumbnailError(p0: YouTubeThumbnailView?, p1: YouTubeThumbnailLoader.ErrorReason?
                        ) {
                        }

                    })

                }

                override fun onInitializationFailure(p0: YouTubeThumbnailView?, p1: YouTubeInitializationResult?) {
                    Toast.makeText(itemView.context,
                        "YouTubeThumbnailView.onInitializationFailure()",
                        Toast.LENGTH_LONG).show()
                }

            })

            addButton.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    val itemAdd = PlaylistItem(
                        PlaylistItem.Snippet(
                            "PL3Tti26r-CYEOkIjztBJwXDr3wOKPIyIf",
                            PlaylistItem.ResourceId("youtube#video", item.id.channelId)
                        )
                    )
                    service.insertPlaylistItems(
                        "snippet",
                        Constant.API_KEY,
                        Constant.accessToken,
                        itemAdd
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<ResponseBody> {
                            override fun onComplete() {

                            }

                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(t: ResponseBody) {
                                Log.d("add", "Successful")
                            }

                            override fun onError(e: Throwable) {
                                Log.d("add", "error")
                            }
                        })
                }

            })

//            deleteButton.setOnClickListener(object : View.OnClickListener{
//                override fun onClick(p0: View?) {
//                    service.deletePlaylistItems(" Bearer "+Constant.accessToken,"application/json","Wde87zn6RMQ",Constant.API_KEY)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(object : Observer<ResponseBody> {
//                            override fun onComplete() {
//
//                            }
//
//                            override fun onSubscribe(d: Disposable) {
//                            }
//
//                            override fun onNext(t: ResponseBody) {
//                                Log.d("add", "Successful")
//                            }
//
//                            override fun onError(e: Throwable) {
//                                Log.d("add", "error")
//                            }
//                        })
//                }
//
//            })



            itemView.setOnClickListener {
                var intent = Intent(itemView.context, PlayerActivity::class.java)
                intent.putExtra("videoId",item.id.channelId)
                itemView.context.startActivity(intent)
            }
        }
    }
}