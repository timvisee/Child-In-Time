package me.childintime.childintime.database.object.bodystate;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DataType;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

public class BodyStateManager extends AbstractDatabaseObjectManager {


    @Override
    public List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface fields[]) {

        String fieldsToFetch = null;


        for (DatabaseFieldsInterface field : fields) {
            fieldsToFetch = fieldsToFetch + ", " + field.getDatabaseField();
        }

        try {
            PreparedStatement fetchStatement = Core.getInstance().getDatabaseConnector().getConnection()
                    .prepareStatement("SELECT id" + fieldsToFetch + " FROM bodystate");

            ResultSet result = fetchStatement.executeQuery();

            while (result.next()){
                int ID = result.getInt("id");
                Date date = result.getDate("date");
                int length = result.getInt("length");
                int weight = result.getInt("weight");
                int studentID = result.getInt("student_id");
                BodyState bodyState = new BodyState();
               // bodyState.getCachedFields().put(BodyStateFields.ID, ID);
                bodyState.getCachedFields().put(BodyStateFields.DATE, date);
                bodyState.getCachedFields().put(BodyStateFields.LENGTH, length);
                bodyState.getCachedFields().put(BodyStateFields.WEIGHT, weight);
                bodyState.getCachedFields().put(BodyStateFields.STUDENT_ID, studentID);

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
