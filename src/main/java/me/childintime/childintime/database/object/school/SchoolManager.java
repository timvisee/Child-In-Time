package me.childintime.childintime.database.object.school;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class SchoolManager extends AbstractDatabaseObjectManager{
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
                String name = result.getString("name");
                String commune = result.getString("commune");
                School school = new School();
                school.getCachedFields().put(SchoolFields.ID, ID);
                school.getCachedFields().put(SchoolFields.NAME, name);
                school.getCachedFields().put(SchoolFields.COMMUNE, commune);

                super.objects.add(school);
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
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM school");
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objectCount;
    }

    @Override
    public String getTypeName() {
        return "School";
    }

    @Override
    public String getTableName() {
        return "school";
    }
}
