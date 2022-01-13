package com.example.top10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.reflect.Array.newInstance
import java.util.*
import kotlin.collections.ArrayList

class ParseApplications {
    private val TAG="ParseApplication"
    val applications=ArrayList<FeedEntry>()

    fun parse(xmlData:String):Boolean{
        Log.d(TAG,"parse called with $xmlData")
        var status=true
        var inEntry=false
        var textValue=""

        try {
            val factory=XmlPullParserFactory.newInstance()
            factory.isNamespaceAware=true
            val xpp=factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType=xpp.eventType
            var currentRecord=FeedEntry()
            while(eventType != XmlPullParser.END_DOCUMENT){
                val tagname= xpp.name?.toLowerCase()
                when(eventType){

                    XmlPullParser.START_DOCUMENT->{
                        Log.d(TAG, "parse: Starting tag for $tagname")
                        if(tagname=="entry"){
                            inEntry=true

                        }
                    }

                    XmlPullParser.TEXT -> textValue =xpp.text

                    XmlPullParser.END_DOCUMENT -> {
                        Log.d(TAG, "parse : Endiing tag for $tagname")
                        if(inEntry){
                            when(tagname){
                                "wntry"->{
                                    applications.add(currentRecord)
                                    inEntry=false
                                    currentRecord= FeedEntry()
                                }
                                "name" -> currentRecord.name =textValue
                                "artist"-> currentRecord.artist=textValue
                                "releasedate"-> currentRecord.releaseDate=textValue
                                "summary" -> currentRecord.summary
                                "image" -> currentRecord.imageURL=textValue
                            }
                        }
                    }
                }
                eventType=xpp.next()
            }
//            for (app in applications) {
//                Log.d(TAG, "****************")
//                Log.d(TAG, app.toString())
        } catch (e:Exception){
            e.printStackTrace()
            status=false
        }
        return  status
    }
}