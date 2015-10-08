package nl.debhver.debedrijfshulpverlener.enums;

import nl.debhver.debedrijfshulpverlener.R;
import nl.debhver.debedrijfshulpverlener.parse.ParseApplication;

/**
 * Created by timvn on 10/8/15.
 */
public enum userRight {
    NONE(R.string.user_right_none), ADMIN(R.string.user_right_admin), EQUIPMENT(R.string.user_right_equipment),
    USER(R.string.user_right_user), EQUIPMENT_USER(R.string.user_right_equipment_user);

    private final int resourceId;

    private userRight(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return ParseApplication.getContext().getString(resourceId);
    }
}
