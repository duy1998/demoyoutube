package com.example.demoyoutubeapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoyoutubeapi.adapter.PlaylistAdapter
import com.example.demoyoutubeapi.playlist.PlaylistItem
import com.example.demoyoutubeapi.playlist.PlaylistResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.demoyoutubeapi.playlist.Snippet
import com.example.demoyoutubeapi.playlist.SnippetPlaylist
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private val service = Retrofit.instance.create(UserServices::class.java)
    private var adapter : PlaylistAdapter =
        PlaylistAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        listItem.layoutManager = LinearLayoutManager(this)
        listItem.adapter = adapter
        addButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Title")

// Set up the input
            val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

// Set up the buttons
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, which ->
                    if(input.text.toString().isNotEmpty()){
                        val itemAdd = SnippetPlaylist(SnippetPlaylist.Snippet(input.text.toString()))
                        service.insertPlaylist("Bearer "+Constant.accessToken,"snippet",Constant.API_KEY,itemAdd)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<Response<Void>>{
                                lateinit var disposable: Disposable
                                override fun onSubscribe(d: Disposable) {
                                    disposable=d
                                    Log.d("HomeActivity", Constant.accessToken)
                                }

                                override fun onNext(t: Response<Void>) {
                                    Log.d("DUYDINH", "Thanh Cong")
                                }

                                override fun onError(e: Throwable) {
                                    Log.d("DUYDINH", e.toString())
                                }

                                override fun onComplete() {
                                    disposable.dispose()
                                }

                            })

                    }

                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()

        }
        service.getPlaylists(" Bearer "+Constant.accessToken,
            "application/json",
            25,
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
