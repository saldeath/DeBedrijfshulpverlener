package nl.debhver.debedrijfshulpverlener.enums;

import nl.debhver.debedrijfshulpverlener.R;
import nl.debhver.debedrijfshulpverlener.parse.ParseApplication;

/**
 * Created by timvn on 10/8/15.
 */
public enum EquipmentType {
    FIRE(R.string.equipment_type_fire), MEDICAL(R.string.equipment_type_medical),
    PERSONNEL_EQUIPMENT(R.string.equipment_type_personnel_equipment);

    private final int resourceId;

    private EquipmentType(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return ParseApplication.getContext().getString(resourceId);
    }
}
