package nl.debhver.debedrijfshulpverlener.enums;

import nl.debhver.debedrijfshulpverlener.R;
import nl.debhver.debedrijfshulpverlener.parse.ParseApplication;

/**
 * Created by timvn on 10/8/15.
 */
public enum trainingType {
    BASIS(R.string.training_type_basis), REPEAT(R.string.training_type_repeat);

    private final int resourceId;

    private trainingType(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return ParseApplication.getContext().getString(resourceId);
    }
}
