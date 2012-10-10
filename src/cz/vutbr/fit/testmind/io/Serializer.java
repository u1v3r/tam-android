package cz.vutbr.fit.testmind.io;

import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.ITAMConnection;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * class for serializing 
 * @author jules
 *
 */
public class Serializer
{
    static private final String CREATE_TABLE_NODES = "CREATE TABLE \"nodes\" (" +
            "\"id\" INTEGER PRIMARY KEY NOT NULL," +
            "\"isRoot\" INTEGER NOT NULL," +
            "\"title\" TEXT NOT NULL," +
            "\"body\" TEXT," +
            "\"type\" INTEGER NOT NULL," +
            "\"position_x\" INTEGER NOT NULL," +
            "\"position_y\" INTEGER NOT NULL," +
            "\"background\" INTEGER," +
            "\"foreground\" INTEGER," +
            "\"highlightColor\" INTEGER," +
            "\"isEnabled\" INTEGER," +
            "\"isHighlighted\" INTEGER)";
    static private final String CREATE_TABLE_CONNECTIONS = "CREATE TABLE \"connections\" (" +
            "\"id\" INTEGER PRIMARY KEY NOT NULL," +
            "\"parent\" INTEGER NOT NULL," +
            "\"child\" INTEGER NOT NULL," +
            "\"background\" INTEGER," +
            "\"highlightColor\" INTEGER," +
            "\"isHighlighted\" INTEGER," +
            "FOREIGN KEY(parent) REFERENCES node(id)," +
            "FOREIGN KEY(child) REFERENCES node(id))";
       
    private File fileDB;
    
    /**
     * Constructor
     * @param file_path 
     */
    public Serializer(String file_path)
    {
        this.fileDB = new File(file_path);
    }
    
    // public methods ========================================================================
    
    /**
     * method create database and serialize nodes
     * @param node
     */
    public void serialize(ITAMNode node)
    {
        SQLiteDatabase db = createDB();
        
        serializeNode(db, node, true);
        
        db.close();
    }
    
    // private methods =======================================================================
    
    /**
     * remove old DB file and create new
     * @return
     */
    private SQLiteDatabase createDB()
    {
        // remove old database
        if(fileDB.exists())
        {
            fileDB.delete();
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(fileDB, null);
        db.execSQL(CREATE_TABLE_NODES);
        db.execSQL(CREATE_TABLE_CONNECTIONS);
        
        return db;
    }
    
    /**
     * serialize node and all childs
     * @param db
     * @param node
     * @param isRoot
     * @return SQL rowid of root node
     */
    private Long serializeNode(SQLiteDatabase db, ITAMNode node, Boolean isRoot)
    {
        ContentValues values = new ContentValues();
        // obligate
        values.put("isRoot", booleanToInt(isRoot));
        values.put("title", node.getText());
        values.put("type", node.getType());
        Point position = node.getPosition();
        values.put("position_x", position.x);
        values.put("position_y", position.y);
        // optional
//        values.put("body", );
        values.put("background", node.getBackground());
        values.put("foreground", node.getForeground());
        values.put("highlightColor", node.getHighlightColor());
        values.put("isEnabled", booleanToInt(node.isEnabled()));
        values.put("isHighlighted", booleanToInt(node.isHighlighted()));
        
        Long parentId = db.insert("nodes", null, values);
        
        List<ITAMConnection> childConnections = node.getListOfChildConnections();
        for (Iterator<ITAMConnection> i = childConnections.iterator(); i.hasNext(); )
        {
             ITAMConnection currentConnection = (ITAMConnection) i.next();
             ITAMNode childNode = currentConnection.getChildNode();
             Long childId = serializeNode(db, childNode, false);
             insertConnection(db, parentId, childId, currentConnection);
        }
        
        return parentId;
    }
    
    /**
     * insert connection to database
     * @param db
     * @param parentId
     * @param childId
     * @param connection
     */
    private void insertConnection(SQLiteDatabase db, Long parentId, Long childId, ITAMConnection connection)
    {
        ContentValues values = new ContentValues();
        
        // obligate
        values.put("parent", parentId);
        values.put("child", childId);
        // optional
        values.put("background", connection.getBackground());
        values.put("highlightColor", connection.getHighlightColor());
        values.put("isHighlighted", booleanToInt(connection.isHighlighted()));
        
        db.insert("connections", null, values);
    }
    
    /**
     * convert boolean value to integer
     * @param value
     * @return
     */
    static private Integer booleanToInt(Boolean value)
    {
        return value ? 1 : 0;
    }
    
}