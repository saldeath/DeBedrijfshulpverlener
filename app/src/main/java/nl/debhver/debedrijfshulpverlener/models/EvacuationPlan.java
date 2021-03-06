package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("evacuation_plan")
public class EvacuationPlan extends ParseObject {
    public EvacuationPlan() {
        super();
    }
    public Number getFloor() {
        return getNumber("floor");
    }
    public void setFloor(Number floor) {
        put("floor", floor);
    }
    public void setEvacuationPlan(ImageModel evacuationPlan) {
        put("evacuation_plan", evacuationPlan);
    }
    public ImageModel getEvacuationPlan()  {
        return (ImageModel) getParseObject("evacuation_plan");
    }
    public void setBranch(Branch branch) {
        put("branch", branch);
    }
    public Branch getBranch()  {
        return (Branch) getParseObject("branch");
    }
    @Override
    public String toString(){
        return getFloor().toString();
    }
}
