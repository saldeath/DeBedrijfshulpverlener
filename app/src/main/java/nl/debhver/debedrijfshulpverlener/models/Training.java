package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import nl.debhver.debedrijfshulpverlener.enums.TrainingType;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("training")
public class Training extends ParseObject{
    public Training() {
        super();
    }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }
    public String getDescription() {
        return getString("description");
    }
    public void setDescription(String description) {
        put("description", description);
    }
    public TrainingType getType() {
        return TrainingType.valueOf(getString("type"));
    }
    public void setType(TrainingType type) {
        put("type", type.name());
    }
}
