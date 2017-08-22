package workshop.akbolatss.tagsnews.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import me.toptas.rssconverter.RssConverterFactory;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AkbolatSS on 17.08.2017.
 */

public class XmlOrJsonConverterFactory extends Converter.Factory {
    final Converter.Factory rssXml = RssConverterFactory.create();
    final Converter.Factory json = GsonConverterFactory.create();

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == NewsApiService.Json.class) {
                return json.responseBodyConverter(type, annotations, retrofit);
            } else if (annotation.annotationType() == NewsApiService.Xml.class){
                return rssXml.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return null;
    }
}
