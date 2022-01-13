package com.example.top10downloader
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.net.URL
import kotlin.properties.Delegates

class FeedEntry{
    var name:String=""
    var artist:String=""
    var releaseDate:String=""
    var summary:String=""
    var imageURL:String=""
    override fun toString(): String {
        return """
            name=$name
            artist=$artist
            releaseDate=$releaseDate
            imageURL=$imageURL
            """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private  val TAG="MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called")
        val downloadData = DownloadData(this, findViewById(R.id.xmlListView))
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG,"onCreate done")
    }
    companion object {
        private  class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext :Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init {
                propContext=context
                propListView=listView
            }
            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
//                Log.d(TAG, "onPostExecute: parameter is $result")
                val parseApplications = ParseApplications()
                parseApplications.parse(result)
                val arrayAdapter=ArrayAdapter<FeedEntry>(propContext,R.layout.list_item,parseApplications.applications)
                propListView.adapter = arrayAdapter
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground Error downloading")
                }
                return "doInBackground completed"
            }
            private fun downloadXML(urlPath:String?):String{
//                val xmlResult=StringBuilder()
//
//                try {
//                    val url = URL(urlPath)
//                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//                    val response = connection.responseCode
//                    Log.d(TAG, "download : the response code was  ${response}")
//
////            val inputStream=connection.inputStream
////            val inputStreamReader=InputStreamReader(inputStream)
////            val reader=BufferedReader(inputStreamReader)
////                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
////                    val inputBuffer = CharArray(500)
////                    var charsRead = 0
////                    while (charsRead <= 0) {
////                        charsRead = reader.read(inputBuffer)
////                        if (charsRead > 0) {
////                            xmlResult.append(String(inputBuffer, 0, charsRead))
////                        }
////                    }
////                    reader.close()
//                    val stream=connection.inputStream
//                    stream.buffered().reader().use{reader->
//                        xmlResult.append(reader.readText())
//                    }
//
//                    Log.d(TAG, "Received ${xmlResult.length} bytes ")
//                    return xmlResult.toString()
//
////                } catch(e:MalformedURLException){
////                    Log.e(TAG,"downloadXML : Invalid URL ${e.message}")
////                }catch(e:IOException) {
////                    Log.e(TAG, "downloadXML : IO Exception reading data : ${e.message}")
////                }catch(e:SecurityException){
////                    Log.e(TAG,"Security exception .Needs permission : ${e.message}")
////                }catch (e:Exception){
////                    Log.e(TAG,"Unknown error ${e.message}")
////                }
//                }catch(e:Exception){
//                    val errorMessage:String=when(e){
//                        is MalformedURLException -> "download :Invalid URL ${e.message}"
//                        is IOException -> "download : IO Exception reading data : ${e.message}"
//                        is SecurityException -> {e.printStackTrace()
//                            "download : Security Exception . Needs permission ? ${e.message}"
//                        }
//                        else -> "Unknown error : ${e.message}"
//                    }
//                }
//                return ""
                return URL(urlPath).readText()
        }
        }

    }
}