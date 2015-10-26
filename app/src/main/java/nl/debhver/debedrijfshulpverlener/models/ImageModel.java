package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("image")
public class ImageModel extends ParseObject{
    public ImageModel() {
        super();
    }

    public ParseFile getImageParseFile() {
        try {
            return fetchIfNeeded().getParseFile("image");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setImageParseFile(ParseFile parseFile) {
        put("image", parseFile);
    }



    //    public ImageModel() {
//        super();
//    }
//
//    public byte[] getImage() {
//        return getBytes("image");
//    }
//    public void setImage(ParseFile image) {
//        put("image", image);
//    }
}
