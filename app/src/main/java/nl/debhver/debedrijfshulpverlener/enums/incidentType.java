package nl.debhver.debedrijfshulpverlener.enums;

import nl.debhver.debedrijfshulpverlener.R;
import nl.debhver.debedrijfshulpverlener.parse.ParseApplication;

/**
 * Created by timvn on 10/8/15.
 */
public enum incidentType {
    FIRE(R.string.incident_type_fire), MEDICAL(R.string.incident_type_medical);

    private final int resourceId;

    private incidentType(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return ParseApplication.getContext().getString(resourceId);
    }
}
