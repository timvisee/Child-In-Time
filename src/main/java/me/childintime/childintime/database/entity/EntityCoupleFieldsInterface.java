package me.childintime.childintime.database.entity;

public interface EntityCoupleFieldsInterface extends EntityFieldsInterface {

    /**
     * Get the first reference field for this couple.
     *
     * @return Reference field.
     */
    EntityCoupleFieldsInterface getFieldA();

    /**
     * Get the second reference field for this couple.
     *
     * @return Reference field.
     */
    EntityCoupleFieldsInterface getFieldB();
}
