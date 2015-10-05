package nl.debhver.debedrijfshulpverlener.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("image")
public class ImageModel extends ParseObject{
    public ImageModel() {
        super();
    }

    public byte[] getImage() {
        return getBytes("image");
    }
    public void setImage(byte[] image) {
        put("image", image);
    }
}
