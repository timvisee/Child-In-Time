package me.childintime.childintime.database.object.student;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

public class StudentManager extends AbstractDatabaseObjectManager{
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
                Date birthdate = result.getDate("birthdate");
                int groupID = result.getInt("group_id");

                Student student = new Student();

                student.getCachedFields().put(StudentFields.ID, ID);
                student.getCachedFields().put(StudentFields.FIRST_NAME, firstName);
                student.getCachedFields().put(StudentFields.LAST_NAME, lastName);
                student.getCachedFields().put(StudentFields.GENDER, gender);
                student.getCachedFields().put(StudentFields.BIRTHDATE, birthdate);
                student.getCachedFields().put(StudentFields.GROUP_ID, groupID);

                super.objects.add(student);
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
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM student");
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objectCount;
    }

    @Override
    public String getTypeName() {
        return "Student";
    }

    @Override
    public String getTableName() {
        return "student";
    }
}
