package workshop.akbolatss.tagsnews.api

import java.lang.reflect.Type

import me.toptas.rssconverter.RssConverterFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class XmlOrJsonConverterFactory : Converter.Factory() {

    private val rssXml: Converter.Factory = RssConverterFactory.create()
    private val json: Converter.Factory = GsonConverterFactory.create()

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            if (annotation.annotationClass == NewsApiService.Json::class) {
                return json.responseBodyConverter(type, annotations, retrofit)
            } else if (annotation.annotationClass == NewsApiService.Xml::class) {
                return rssXml.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null
    }
}

//    final Converter.Factory rssXml = RssConverterFactory.create();
//    final Converter.Factory json = GsonConverterFactory.create();
//
//    @Override
//    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//        for (Annotation annotation : annotations) {
//            if (annotation.annotationType() == NewsApiService.Json.class) {
//                return json.responseBodyConverter(type, annotations, retrofit);
//            } else if (annotation.annotationType() == NewsApiService.Xml.class){
//                return rssXml.responseBodyConverter(type, annotations, retrofit);
//            }
//        }
//        return null;
//    }
