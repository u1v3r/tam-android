package cz.vutbr.fit.testmind.io;

import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMEditorConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.ITAMConnection;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import java.io.File;
import java.util.ArrayList;
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
            "\"title\" TEXT NOT NULL," +
            "\"body\" TEXT," +
            "\"type\" INTEGER NOT NULL," +
            "\"x\" INTEGER NOT NULL," +
            "\"y\" INTEGER NOT NULL," +
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

    static private final String[] COLUMNS_NODES = {"id", "title", "body", "type", "x",
                                                   "y", "background", "foreground", "highlightColor",
                                                   "isEnabled", "isHighlighted"};
    
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
    public void serialize(TAMEditor editor)
    {
        SQLiteDatabase db = createDB();
        
        for (Iterator<TAMEditorNode> i = editor.getListOfNodes().iterator(); i.hasNext(); )
        {
             TAMEditorNode currentNode = (TAMEditorNode) i.next();
             insertNode(db, currentNode);
        }
        
        for (Iterator<TAMEditorConnection> i = editor.getListOfConnections().iterator(); i.hasNext(); )
        {
             TAMEditorConnection currentConnection = (TAMEditorConnection) i.next();
             insertConnection(db, currentConnection);
        }   
        
        db.close();
    }
    
    public void deserialize(TAMGraph graph)
    {
        SQLiteDatabase db = openDB();


        
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
            // TODO check journal
            fileDB.delete();
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(fileDB, null);
        db.execSQL(CREATE_TABLE_NODES);
        db.execSQL(CREATE_TABLE_CONNECTIONS);
        
        return db;
    }
    
    /**
     * open sqlite file
     * @return
     */
    private SQLiteDatabase openDB()
    {
        return SQLiteDatabase.openDatabase(fileDB.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }
    
    /**
     * serialize node
     * @param db
     * @param node
     * @return SQL rowid of root node
     */
    private void insertNode(SQLiteDatabase db, TAMEditorNode node)
    {
        ITAMNode core = node.getCore();
        
        ContentValues values = new ContentValues();
        values.put("id", node.getId());
        values.put("title", core.getText());
        values.put("title", node.getBody());
        values.put("type", core.getType());
        Point position = core.getPosition();
        values.put("x", position.x);
        values.put("y", position.y);
        values.put("background", core.getBackground());
        values.put("foreground", core.getForeground());
        values.put("highlightColor", core.getHighlightColor());
        values.put("isEnabled", booleanToInt(core.isEnabled()));
        values.put("isHighlighted", booleanToInt(core.isHighlighted()));
    }
    
    /**
     * insert connection to database
     * @param db
     * @param parentId
     * @param childId
     * @param connection
     */
    private void insertConnection(SQLiteDatabase db, TAMEditorConnection connection)
    {
        ContentValues values = new ContentValues();
        
        ITAMConnection core = connection.getCore();
        values.put("parent", connection.getParent().getId());
        values.put("child", connection.getChild().getId());
        values.put("background", core.getBackground());
        values.put("highlightColor", core.getHighlightColor());
        values.put("isHighlighted", booleanToInt(core.isHighlighted()));
        
        db.insert("connections", null, values);
    }
    
//    private Integer loadNodes(SQLiteDatabase db, ArrayList<ITAMNode> nodes)
//    {
//        db.query("nodes", "id, isRoot", null, null, null, null, null);
//    }
    
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