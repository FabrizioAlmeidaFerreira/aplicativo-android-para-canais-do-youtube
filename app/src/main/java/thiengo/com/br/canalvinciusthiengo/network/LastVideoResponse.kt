package thiengo.com.br.canalvinciusthiengo.network

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import thiengo.com.br.canalvinciusthiengo.data.UtilDatabase
import thiengo.com.br.canalvinciusthiengo.domain.LastVideo
import thiengo.com.br.canalvinciusthiengo.network.video.Video

class LastVideoResponse(
    private val context: Context,
    private val callbackSuccess: (LastVideo)->Unit = {},
    private val callbackFailure: (NetworkRetrieveDataProblem)->Unit = {} ) : Callback<Video> {

    override fun onResponse(
        call: Call<Video>,
        response: Response<Video> ){
        parse( response = response )
    }

    override fun onFailure(
        call: Call<Video>,
        t: Throwable ){

        callbackFailure( NetworkRetrieveDataProblem.NO_INTERNET_CONNECTION )
    }

    fun parse( response: Response<Video> ){

        if( response.isSuccessful ){
            val video = response.body()!!

            if( video.id.isNotEmpty() ){
                val lastVideo = LastVideo(
                    uid = video.id,
                    title = video.title,
                    description = video.description,
                    thumbUrl = video.thumbUrl
                )

                UtilDatabase
                    .getInstance( context = context )
                    .saveLastVideo( lastVideo = lastVideo )

                callbackSuccess( lastVideo )
            }
            else{
                callbackFailure( NetworkRetrieveDataProblem.NO_VIDEO )
            }
        }
        else{
            callbackFailure( NetworkRetrieveDataProblem.NO_INTERNET_CONNECTION )
        }
    }
}