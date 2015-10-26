package nl.debhver.debedrijfshulpverlener.enums;

/**
 * Created by timvn on 10/16/15.
 */
public enum Table {
    USER("_user"), BRANCH("branch"), EQUIPMENT("equipment"), INCIDENT("incident"), TRAINING("training"),
    EVACUATIONPLAN("evacuation_plan"), IMAGE("image"), USERTRAINING("user_training");

    private final String tableName;

    private Table(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }
}
