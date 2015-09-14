package android.flickrtestapp;

import android.graphics.Bitmap;

/**
 * Created by Sola2Be on 14.09.2015.
 */
public class ModelPhoto {

    private String id;
    private String thumbURL;
    private Bitmap thumb;
    private Bitmap photo;
    private String mediumURL;

    public ModelPhoto(String _id){
        setId(_id);
    }

    public ModelPhoto(String thumbURL,String mediumURL){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getMediumURL() {
        return mediumURL;
    }

    public void setMediumURL(String mediumURL) {
        this.mediumURL = mediumURL;
    }
}
