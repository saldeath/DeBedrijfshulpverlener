package nl.debhver.debedrijfshulpverlener.enums;

import nl.debhver.debedrijfshulpverlener.R;
import nl.debhver.debedrijfshulpverlener.parse.ParseApplication;

/**
 * Created by timvn on 10/8/15.
 */
public enum UserEROFunction {
    FIRE(R.string.user_ERO_function_fire), FIRST_AID(R.string.user_ERO_function_first_aid),
    AED(R.string.user_ERO_function_AED);

    private final int resourceId;

    private UserEROFunction(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return ParseApplication.getContext().getString(resourceId);
    }
}
