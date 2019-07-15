package com.example.demoyoutubeapi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoyoutubeapi.data.OAuthToken
import com.example.demoyoutubeapi.data.SearchResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.HttpUrl


class MainActivity : AppCompatActivity() {
    private val compositeDisposable: CompositeDisposable? = null

    private var adapter:SearchAdapter = SearchAdapter()

    private val service = Retrofit.instance.create(UserServices::class.java)

    private val REDIRECT_URI_ROOT : String = "com.example.demoyoutubeapi"
    private val REDIRECT_URI : String = "com.example.demoyoutubeapi:/oauth2redirect"
    private val SCOPE = ArrayList<String>()
    private val CODE : String = "code"
    private val CLIENT_ID : String = "505059170979-vgj6v7hhjs6ienjqdfnhuhb0aof6uhgl.apps.googleusercontent.com"

    private var accessToken : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SCOPE.add("https://www.googleapis.com/auth/youtube")
        SCOPE.add("https://www.googleapis.com/auth/youtube")
        loginButton.setOnClickListener {  startIntentToGetOauthAccessToken() }

        val code : String?
        val error : String?
        val data : Uri? = intent.data
        if(data!=null){
            if(REDIRECT_URI_ROOT == intent.scheme){
                code = data.getQueryParameter("code")
                error = data.getQueryParameter("error")
                if(!error.isNullOrEmpty()){
                    Toast.makeText(this,error,Toast.LENGTH_SHORT).show()
                }else{
                    requestToken(code)
                }
            }
        }

        searchEditText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, SHOW_IMPLICIT)

        listItem.adapter = adapter
        listItem.layoutManager = LinearLayoutManager(this)

        searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                    service.searchByKeyWord(Constant.API_KEY, "snippet", searchEditText.text.toString(), 10, "video")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<SearchResponse> {
                            lateinit var disposable: Disposable
                            override fun onComplete() {
                                disposable.dispose()
                            }

                            override fun onSubscribe(d: Disposable) {
                                disposable = d
                                compositeDisposable?.add(disposable)
                            }

                            override fun onNext(t: SearchResponse) {
                                adapter.setData(t.items)
                            }

                            override fun onError(e: Throwable) {
                                Log.d("TAG", e.toString())
                            }
                        })
                    return true
                }
                return false
            }

        })




    }

    private fun requestToken(code: String?) {

        code?.let {
            service
                .requestToken(it,CLIENT_ID,REDIRECT_URI,"authorization_code")
                .subscribeOn(Schedulers.io())
                .observeOn((AndroidSchedulers.mainThread()))
                .subscribe(object : Observer<OAuthToken> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: OAuthToken) {
                        accessToken = t.accessToken
                        loginButton.text = t.accessToken
                    }

                    override fun onError(e: Throwable) {
                        Log.d("oauthToken",e.toString())
                    }
                })
        }
    }

    private fun startIntentToGetOauthAccessToken() {
        val authorizeUrl : HttpUrl = HttpUrl.parse("https://accounts.google.com/o/oauth2/v2/auth")!!
            .newBuilder()
            .addQueryParameter("client_id",CLIENT_ID)
            .addQueryParameter("scope","https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtube.force-ssl" )
            .addQueryParameter("redirect_uri",REDIRECT_URI)
            .addQueryParameter("response_type",CODE)
            .build()
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(authorizeUrl.url().toString())
        startActivity(i)
    }

}
