package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("image")
public class ImageModel extends ParseObject{

    public ParseFile getParseFile() {
        return getParseFile("image");
    }

    public void setParseFile(ParseFile parseFile) {

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
