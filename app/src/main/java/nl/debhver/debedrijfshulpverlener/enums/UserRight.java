package nl.debhver.debedrijfshulpverlener.enums;

import nl.debhver.debedrijfshulpverlener.R;
import nl.debhver.debedrijfshulpverlener.parse.ParseApplication;

/**
 * Created by timvn on 10/8/15.
 */
public enum UserRight {
    NONE(R.string.user_right_none), ADMIN(R.string.user_right_admin), EQUIPMENT(R.string.user_right_equipment),
    TRAINING(R.string.user_right_training), EQUIPMENT_TRAINING(R.string.user_right_equipment_training);

    private final int resourceId;

    private UserRight(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return ParseApplication.getContext().getString(resourceId);
    }
}
