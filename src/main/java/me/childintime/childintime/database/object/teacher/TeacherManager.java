package me.childintime.childintime.database.object.teacher;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TeacherManager extends AbstractDatabaseObjectManager{
    @Override
    public List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface[] fields) {
        String fieldsToFetch = null;


        for (DatabaseFieldsInterface field : fields) {
            fieldsToFetch = fieldsToFetch + ", " + field.getDatabaseField();
        }

        try {
            PreparedStatement fetchStatement = Core.getInstance().getDatabaseConnector().getConnection()
                    .prepareStatement("SELECT id" + fieldsToFetch + " FROM group");

            ResultSet result = fetchStatement.executeQuery();

            while (result.next()){
                int ID = result.getInt("id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                int gender = result.getInt("gender");
                int isGym = result.getInt("is_gym");
                int schoolID = result.getInt("school_id");
                Teacher teacher = new Teacher();
                teacher.getCachedFields().put(TeacherFields.ID, ID);
                teacher.getCachedFields().put(TeacherFields.FIRST_NAME, firstName);
                teacher.getCachedFields().put(TeacherFields.LAST_NAME, lastName);
                // teacher.getCachedFields().put(TeacherFields.GENDER, gender);
                teacher.getCachedFields().put(TeacherFields.IS_GYM, isGym);
                teacher.getCachedFields().put(TeacherFields.SCHOOL_ID, schoolID);

                super.objects.add(teacher);
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }


        return super.objects;
    }

    @Override
    public int getObjectCount() {
        int objectCount = 0;

        try {
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM teacher");
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objectCount;
    }

    @Override
    public String getTypeName() {
        return "Teacher";
    }

    @Override
    public String getTableName() {
        return "teacher";
    }
}
