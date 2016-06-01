package me.childintime.childintime.database.object.group;

import me.childintime.childintime.Core;
import me.childintime.childintime.database.object.AbstractDatabaseObject;
import me.childintime.childintime.database.object.AbstractDatabaseObjectManager;
import me.childintime.childintime.database.object.DatabaseFieldsInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class GroupManager extends AbstractDatabaseObjectManager{
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
                int schoolID = result.getInt("school_id");
                Group group = new Group();
                group.getCachedFields().put(GroupFields.ID, ID);
                group.getCachedFields().put(GroupFields.NAME, name);
                group.getCachedFields().put(GroupFields.SCHOOL_ID, schoolID);

                super.objects.add(group);
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
            PreparedStatement countQuery = Core.getInstance().getDatabaseConnector().getConnection().prepareStatement("SELECT count(id) FROM group");
            ResultSet result = countQuery.executeQuery();
            objectCount = result.getInt("count(id)");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return objectCount;
    }

    @Override
    public String getTypeName() {
        return "Group";
    }

    @Override
    public String getTableName() {
        return "group";
    }
}
