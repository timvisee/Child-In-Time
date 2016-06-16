package me.childintime.childintime.database.entity.spec.couple.groupteacher;

import me.childintime.childintime.database.entity.AbstractEntity;
import me.childintime.childintime.database.entity.AbstractEntityManifest;

public class GroupTeacher extends AbstractEntity {

    /**
     * Constructor.
     */
    public GroupTeacher() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Entity id.
     */
    public GroupTeacher(int id) {
        super(id);
    }

    @Override
    public AbstractEntityManifest getManifest() {
        return GroupTeacherManifest.getInstance();
    }

    @Override
    public String getDisplayName() {
        try {
            // Prefetch the fields
            getFields(new GroupTeacherFields[]{
                    GroupTeacherFields.GROUP_ID,
                    GroupTeacherFields.TEACHER_ID
            });

            // Build and return the display name
            return getFieldFormatted(GroupTeacherFields.GROUP_ID) + " & " + getFieldFormatted(GroupTeacherFields.TEACHER_ID);

        } catch(Exception e) {
            // Print the stack trace
            e.printStackTrace();

            // Some error occurred, return an error string
            return "<error>";
        }
    }
}
