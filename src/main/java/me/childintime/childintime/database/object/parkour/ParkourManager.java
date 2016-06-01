package me.childintime.childintime.database.object.parkour;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ParkourManager extends AbstractDatabaseObjectManager{
    @Override
    public List<AbstractDatabaseObject> fetchObjects(DatabaseFieldsInterface[] fields) {
        String fieldsToFetch = null;


        for (DatabaseFieldsInterface field : fields) {
            fieldsToFetch = fieldsToFetch + ", " + field.getDatabaseField();
        }

        try {
            PreparedStatement fetchStatement = Core.getInstance().getDatabaseConnector().getConnection()
                    .prepareStatement("SELECT id" + fieldsToFetch + " FROM parkour");

            ResultSet result = fetchStatement.executeQuery();

            while (result.next()){
                int ID = result.getInt("id");
                String description = result.getString("description");
                Parkour parkour = new Parkour();
                parkour.getCachedFields().put(ParkourFields.ID, ID);
                parkour.getCachedFields().put(ParkourFields.DESCRIPTION, description);

                super.objects.add(parkour);
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
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM parkour");
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objectCount;
    }

    @Override
    public String getTypeName() {
        return "Parkour";
    }

    @Override
    public String getTableName() {
        return "parkour";
    }
}
