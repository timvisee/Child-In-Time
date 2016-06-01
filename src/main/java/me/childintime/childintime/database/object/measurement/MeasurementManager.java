package me.childintime.childintime.database.object.measurement;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;


public class MeasurementManager extends AbstractDatabaseObjectManager{
    @Override
    public List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface[] fields) {
        String fieldsToFetch = null;


        for (DatabaseFieldsInterface field : fields) {
            fieldsToFetch = fieldsToFetch + ", " + field.getDatabaseField();
        }

        try {
            PreparedStatement fetchStatement = Core.getInstance().getDatabaseConnector().getConnection()
                    .prepareStatement("SELECT id" + fieldsToFetch + " FROM measurement");

            ResultSet result = fetchStatement.executeQuery();

            while (result.next()){
                int ID = result.getInt("id");
                Date date = result.getDate("date");
                int time = result.getInt("time");
                int studentID = result.getInt("student_id");
                int parkourID = result.getInt("parkour_id");
                Measurement measurement = new Measurement();
               // measurement.getCachedFields().put(MeasurementFields.ID, ID);
                measurement.getCachedFields().put(MeasurementFields.DATE, date);
                measurement.getCachedFields().put(MeasurementFields.TIME, time);
                measurement.getCachedFields().put(MeasurementFields.STUDENT_ID, studentID);
                measurement.getCachedFields().put(MeasurementFields.PARKOUR_ID, parkourID);

                super.objects.add(measurement);
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
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM measurement");
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objectCount;
    }

    @Override
    public String getTypeName() {
        return "Measurement";
    }

    @Override
    public String getTableName() {
        return "measurement";
    }
}
