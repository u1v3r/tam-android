package cz.vutbr.fit.testmind.io;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMEItem;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEItemFactory;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.TAMGZoom;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;
import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * class for serializing 
 * @author jules
 *
 */
public class Serializer
{
    static private final String CREATE_TABLE_PROFILE = "CREATE TABLE \"profile\" (" +
            "\"root\" id NOT NULL," +
            "\"nodeCounter\" int NOT NULL," +
            "\"connectionCounter\" int NOT NULL," +
            "FOREIGN KEY(root) REFERENCES node(id))";
    static private final String CREATE_TABLE_EDITORS = "CREATE TABLE \"editors\" (" +
            "\"name\" TEXT UNIQUE NOT NULL," +
            "\"sx\" int NOT NULL," +
            "\"sy\" int NOT NULL," +
            "\"px\" int NOT NULL," +
            "\"py\" int NOT NULL)";
    static private final String CREATE_TABLE_NODE_REFERENCES = "CREATE TABLE \"node_references\" (" +
            "\"node\" int NOT NULL," +
            "\"editor\" TEXT UNIQUE NOT NULL," +
            "\"type\" int NOT NULL," +
            "\"x\" int NOT NULL," +
            "\"y\" int NOT NULL," +
            "\"background\" int," +
            "\"backgroundStroke\" int," +
            "\"foreground\" int," +
            "\"highlightColor\" int," +
            "FOREIGN KEY(node) REFERENCES node(id)," +
            "FOREIGN KEY(editor) REFERENCES editor(id))";
    static private final String CREATE_TABLE_CONNECTION_REFERENCES = "CREATE TABLE \"connection_references\" (" +
            "\"connection\" int NOT NULL," +
            "\"editor\" TEXT UNIQUE NOT NULL," +
            "\"type\" int NOT NULL," +
            "\"background\" int," +
            "\"highlightColor\" int," +
            "FOREIGN KEY(connection) REFERENCES connection(id)," +
            "FOREIGN KEY(editor) REFERENCES editor(id))";
    static private final String CREATE_TABLE_NODES = "CREATE TABLE \"nodes\" (" +
            "\"id\" int PRIMARY KEY NOT NULL," +
            "\"title\" TEXT NOT NULL," +
            "\"body\" TEXT)";
    static private final String CREATE_TABLE_CONNECTIONS = "CREATE TABLE \"connections\" (" +
            "\"id\" int PRIMARY KEY NOT NULL," +
            "\"parent\" int NOT NULL," +
            "\"child\" int NOT NULL," +
            "FOREIGN KEY(parent) REFERENCES node(id)," +
            "FOREIGN KEY(child) REFERENCES node(id))";

//    static private final String[] COLUMNS_PROFILE = {"root", "nodeCounter", "connectionCounter"};
//    
//    static private final String[] COLUMNS_EDITOR = {"name", "sx", "sy", "px", "py"};
//
//    static private final String[] COLUMNS_NODES = {"id", "title", "body", "type", "x", "y", "hasVisibleChilds",
//                                                   "background", "foreground", "backgroundStroke", "highlightColor"};
//
//    static private final String[] COLUMNS_CONNECTIONS = {"id", "parent", "child", "type", "background", "highlightColor"};
    
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
     * @param profile
     */
    public void serialize(TAMProfile profile)
    {
        SQLiteDatabase db = createDB();
        
        // profile
        insertProfile(db, profile);
        
        // editors
        for (ITAMEditor editor: profile.getListOfEditors())
        {       
            insertEditor(db, editor);
        }
        
        // nodes
        for (TAMPNode node: profile.getListOfPNodes())
        {
            insertNode(db, node);
            
            Map<ITAMEditor, ITAMEItem> references = node.getEditorReferences();
            Set<ITAMEditor> editors = references.keySet();
            for (ITAMEditor editor: editors)
            {
                insertNodeReference(db, editor, (ITAMENode)references.get(editor));
            }
        }
        
        // connections
        for (TAMPConnection connection: profile.getListOfPConnections())
        {
             insertConnection(db, connection);
             
             Map<ITAMEditor, ITAMEItem> references = connection.getEditorReferences();
             Set<ITAMEditor> editors = references.keySet();
             for (ITAMEditor editor: editors)
             {
                 insertConnectionReference(db, editor, (ITAMEConnection)references.get(editor));
             }
        } 
        
        db.close();
    }
    
    /**
     * load nodes and connections from db file
     * @param editor
     */
    public void deserialize(TAMEditor editor)
    {
        SQLiteDatabase db = openDB();
        
        /*TAMEItemFactory factory = editor.getFactory();
        
        SparseArray<TAMENode> nodes = importNodes(db, factory);
        
        importGraph(db, editor, nodes);
        importConnections(db, factory, nodes);*/
        
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
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_EDITORS);
        db.execSQL(CREATE_TABLE_NODES);
        db.execSQL(CREATE_TABLE_CONNECTIONS);
        db.execSQL(CREATE_TABLE_NODE_REFERENCES);
        db.execSQL(CREATE_TABLE_CONNECTION_REFERENCES);
        
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
     * insert profile to database
     * @param db
     * @param profile
     */
    private void insertProfile(SQLiteDatabase db, TAMProfile profile)
    {
        ContentValues values = new ContentValues();
        values.put("root", profile.getRoot().getId());
        values.put("nodeCounter", TAMPNode.getCounter());
        values.put("connectionCounter", TAMPConnection.getCounter());
      
        db.insert("profile", null, values);        
    }
    
    /**
     * insert editor to database
     * @param db
     * @param editor
     */
    private void insertEditor(SQLiteDatabase db, ITAMEditor editor)
    {
        TAMGZoom zoom = editor.getZoom();
        
        ContentValues values = new ContentValues();
        values.put("name", editor.getClass().getName());
        values.put("sx", zoom.sx);
        values.put("sy", zoom.sy);
        values.put("px", zoom.px);
        values.put("py", zoom.py);
        
        db.insert("editors", null, values);
    }
    
    /**
     * serialize node
     * @param db
     * @param node
     */
    private void insertNode(SQLiteDatabase db, TAMPNode node)
    {        
        ContentValues values = new ContentValues();
        values.put("id", node.getId());
        values.put("title", node.getTitle());
        values.put("body", node.getBody());
        
        db.insert("nodes", null, values);
    }
    
    /**
     * insert node reference to database
     * @param db
     * @param editor
     * @param node
     */
    private void insertNodeReference(SQLiteDatabase db, ITAMEditor editor, ITAMENode node)
    {
        ContentValues values = new ContentValues();
        
        ITAMGNode gui = node.getGui();
        values.put("node", node.getProfile().getId());
        values.put("editor", editor.getClass().getName());
        values.put("type", gui.getType());
        Point position = gui.getPosition();
        values.put("x", position.x);
        values.put("y", position.y);
        values.put("background", gui.getBackground());
        values.put("foreground", gui.getForeground());
        values.put("backgroundStroke", gui.getBackgroundStroke());
        values.put("highlightColor", gui.getHighlightColor());
        
        db.insert("node_references", null, values);
    }
    
    /**
     * insert connection to database
     * @param db
     * @param parentId
     * @param childId
     * @param connection
     */
    private void insertConnection(SQLiteDatabase db, TAMPConnection connection)
    {
        ContentValues values = new ContentValues();
 
        values.put("id", connection.getId());
        values.put("parent", connection.getParent().getId());
        values.put("child", connection.getChild().getId());
        
        db.insert("connections", null, values);
    }

    /**
     * insert connection reference to database
     * @param db
     * @param editor
     * @param connection
     */
    private void insertConnectionReference(SQLiteDatabase db, ITAMEditor editor,
                                           ITAMEConnection connection)
    {
        ContentValues values = new ContentValues();
        
        ITAMGConnection gui = connection.getGui();
        values.put("connection", connection.getProfile().getId());
        values.put("editor", editor.getClass().getName());
        values.put("type", gui.getType());
        values.put("background", gui.getBackground());
        values.put("highlightColor", gui.getHighlightColor());
        
        db.insert("connection_references", null, values);        
    }
    
//    private void importGraph(SQLiteDatabase db, TAMEditor editor, SparseArray<TAMENode> nodes)
//    {
//        Cursor cur = db.query("graph", COLUMNS_GRAPH, null, null, null, null, null);
//        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_GRAPH);
//
//        cur.moveToFirst();
//        // TODO editor.setRoot(nodes.get(cur.getInt(indexes.get("root"))));
//        // TODO node counter
//        // TODO connection counter
//        
//        editor.setScaleX(cur.getInt(indexes.get("sx")));
//        editor.setScaleY(cur.getInt(indexes.get("sy")));
//        editor.setPivotX(cur.getInt(indexes.get("px")));
//        editor.setPivotY(cur.getInt(indexes.get("py")));
//    }    
//    
//    /**
//     * import all nodes
//     * @param db
//     * @param factory
//     * @return
//     */
//    private SparseArray<TAMENode> importNodes(SQLiteDatabase db, TAMEItemFactory factory)
//    {
//        SparseArray<TAMENode> result = new SparseArray<TAMENode>();
//        
//        Cursor cur = db.query("nodes", COLUMNS_NODES, null, null, null, null, null);
//        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_NODES);
//
//        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
//        {
//            int id = cur.getInt(indexes.get("id"));
//            String title = cur.getString(indexes.get("title"));
//            String body = cur.getString(indexes.get("body"));
//            int type = cur.getInt(indexes.get("type"));
//            int x = cur.getInt(indexes.get("x"));
//            int y = cur.getInt(indexes.get("y"));
//            /*TAMENode node = factory.importNode(id, x, y, title, body, type);
//            ITAMGNode core = node.getGui();
//            node.setChildsVisible(intToboolean(cur.getInt(indexes.get("hasVisibleChilds"))));
//            core.setBackground(cur.getInt(indexes.get("background")));
//            core.setForeground(cur.getInt(indexes.get("foreground")));
//            core.setBackgroundStroke(cur.getInt(indexes.get("backgroundStroke")));
//            core.setHighlightColor(cur.getInt(indexes.get("highlightColor")));*/
//            
//            //result.append(id, node);
//        }
//        
//        return result;
//    }
//
//    /**
//     * import all connections
//     * @param db
//     * @param factory
//     * @param nodes
//     */
//    private void importConnections(SQLiteDatabase db, TAMEItemFactory factory, SparseArray<TAMENode> nodes)
//    {
//        Cursor cur = db.query("connections", COLUMNS_CONNECTIONS, null, null, null, null, null);
//        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_CONNECTIONS);
//
//        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
//        {
//            int id = cur.getInt(indexes.get("id"));
//            TAMENode parent = nodes.get(cur.getInt(indexes.get("parent")));
//            TAMENode child = nodes.get(cur.getInt(indexes.get("child")));
//            int type = cur.getInt(indexes.get("type"));
//            /*ITAMGConnection core = factory.importConnection(id, parent, child, type).getGui();
//            core.setBackground(cur.getInt(indexes.get("background")));
//            core.setHighlightColor(cur.getInt(indexes.get("highlightColor")));*/
//        }        
//    }
    
    // static private methods =================================================
    
    /**
     * convert boolean value to int
     * @param value
     * @return
     */
    static private int booleanToInt(Boolean value)
    {
        return value ? 1 : 0;
    }

    /**
     * convert int to boolean
     * @param value
     * @return
     */
    static private boolean intToboolean(int value)
    {
        return value != 0;
    }
    
    /**
     * crate hashMap column name -> index of column
     * @param cur
     * @param columns
     * @return
     */
    static private HashMap<String, Integer> getIndexesCursor(Cursor cur, String[] columns)
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        
        for (int i = 0; i < columns.length; i++)
        {
            map.put(columns[i], cur.getColumnIndex(columns[i]));
        }
        
        return map;
    }
}