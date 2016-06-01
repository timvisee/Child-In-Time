package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class BodyStateManager extends AbstractDatabaseObjectManager {


    @Override
    public List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface fields[]) {

        StringBuilder fieldsToFetch = new StringBuilder("id");
        for (DatabaseFieldsInterface field : fields)
            fieldsToFetch.append(", ").append(field.getDatabaseField());

        try {
            PreparedStatement fetchStatement = Core.getInstance().getDatabaseConnector().getConnection()
                    .prepareStatement("SELECT " + fieldsToFetch.toString() + " FROM bodystate");

            ResultSet result = fetchStatement.executeQuery();

            while (result.next()) {
                // Get the object ID
                int id = result.getInt("id");

                // Create the database object instance
                BodyState bodyState = new BodyState(id);

                // Parse and cache the fields
                for (DatabaseFieldsInterface field : fields)
                    bodyState.parseField(field, result.getString(field.getDatabaseField()));

                // Add the object to the list
                super.objects.add(bodyState);
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

        try{
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM bodystate");
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return objectCount;
    }


    @Override
    public String getTypeName() {
        return "BodyState";
    }

    @Override
    public String getTableName() {
        return "bodystate";
    }
}
