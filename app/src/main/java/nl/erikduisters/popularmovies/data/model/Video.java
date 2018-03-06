package nl.erikduisters.popularmovies.data.model;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Erik Duisters on 03-03-2018.
 */

public class Video {
    private static final String THUMBNAIL_URL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    @IntDef({Size.SIZE360P, Size.SIZE480P, Size.SIZE720P, Size.SIZE1080P})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Size {
        int SIZE360P = 360;
        int SIZE480P = 480;
        int SIZE720P = 720;
        int SIZE1080P = 1080;
    }

    @StringDef({Type.TYPE_TRAILER, Type.TYPE_TEASER, Type.TYPE_CLIP, Type.TYPE_FEATURETTE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Type {
        String TYPE_TRAILER = "Trailer";
        String TYPE_TEASER = "Teaser";
        String TYPE_CLIP = "Clip";
        String TYPE_FEATURETTE = "Featurette";
    }

    private String id;
    @SerializedName("iso_639_1") private String languageCode;
    @SerializedName("iso_3166_1") private String countryCode;
    private String key;
    private String name;
    private String site;
    private @Size int size;
    private @Type String type;

    public String getName() { return name; }
    public String getThumbnailUrl() { return String.format(THUMBNAIL_URL, key); }
    public boolean isTrailer() { return type.equals(Type.TYPE_TRAILER); }
    public String getKey() { return key; }
}
