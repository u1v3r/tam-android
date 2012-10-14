package cz.vutbr.fit.testmind.io;

import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEItemFactory;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.util.SparseArray;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * class for serializing 
 * @author jules
 *
 */
public class Serializer
{
    static private final String CREATE_TABLE_GRAPH = "CREATE TABLE \"graph\" (" +
            "\"root\" int NOT NULL," +
            "\"nodeCounter\" int NOT NULL," +
            "\"connectionCounter\" int NOT NULL," +
            "\"sx\" int NOT NULL," +
            "\"sy\" int NOT NULL," +
            "\"px\" int NOT NULL," +
            "\"py\" int NOT NULL," +
            "FOREIGN KEY(root) REFERENCES node(id))";
    static private final String CREATE_TABLE_NODES = "CREATE TABLE \"nodes\" (" +
            "\"id\" int PRIMARY KEY NOT NULL," +
            "\"title\" TEXT NOT NULL," +
            "\"body\" TEXT," +
            "\"type\" int NOT NULL," +
            "\"x\" int NOT NULL," +
            "\"y\" int NOT NULL," +
            "\"hasVisibleChilds\" int NOT NULL," +
            "\"background\" int," +
            "\"foreground\" int," +
            "\"backgroundStroke\" int," +
            "\"highlightColor\" int)";
    static private final String CREATE_TABLE_CONNECTIONS = "CREATE TABLE \"connections\" (" +
            "\"id\" int PRIMARY KEY NOT NULL," +
            "\"parent\" int NOT NULL," +
            "\"child\" int NOT NULL," +
            "\"type\" int NOT NULL," +
            "\"background\" int," +
            "\"highlightColor\" int," +
            "FOREIGN KEY(parent) REFERENCES node(id)," +
            "FOREIGN KEY(child) REFERENCES node(id))";

    static private final String[] COLUMNS_GRAPH = {"root", "nodeCounter", "connectionCounter", "sx", "sy", "px", "py"};

    static private final String[] COLUMNS_NODES = {"id", "title", "body", "type", "x", "y", "hasVisibleChilds",
                                                   "background", "foreground", "backgroundStroke", "highlightColor"};

    static private final String[] COLUMNS_CONNECTIONS = {"id", "parent", "child", "type", "background", "highlightColor"};
    
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
        
        insertGraph(db, editor);
        
        for (Iterator<TAMENode> i = editor.getListOfNodes().iterator(); i.hasNext(); )
        {
             TAMENode currentNode = (TAMENode) i.next();
             insertNode(db, currentNode);
        }
        
        for (Iterator<TAMEConnection> i = editor.getListOfConnections().iterator(); i.hasNext(); )
        {
             TAMEConnection currentConnection = (TAMEConnection) i.next();
             insertConnection(db, currentConnection);
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
        
        TAMEItemFactory factory = editor.getFactory();
        
        SparseArray<TAMENode> nodes = importNodes(db, factory);
        
        importGraph(db, editor, nodes);
        importConnections(db, factory, nodes);
        
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
        db.execSQL(CREATE_TABLE_GRAPH);
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

    private void insertGraph(SQLiteDatabase db, TAMEditor editor)
    {
        
        ContentValues values = new ContentValues();
        values.put("root", editor.getRoot().getId());
        // TODO values.put("nodeCounter", );
        // TODO values.put("connectionCounter", );
        values.put("sx", editor.getScaleX());
        values.put("sy", editor.getScaleY());
        values.put("px", editor.getPivotX());
        values.put("py", editor.getPivotY());
        
        db.insert("graph", null, values);
    }
    
    /**
     * serialize node
     * @param db
     * @param node
     * @return SQL rowid of root node
     */
    private void insertNode(SQLiteDatabase db, TAMENode node)
    {
        ITAMGNode core = node.getCore();
        
        ContentValues values = new ContentValues();
        values.put("id", node.getId());
        values.put("title", core.getText());
        values.put("body", node.getBody());
        values.put("type", core.getType());
        Point position = core.getPosition();
        values.put("x", position.x);
        values.put("y", position.y);
        values.put("hasVisibleChilds", booleanToInt(node.hasVisibleChilds()));
        values.put("background", core.getBackground());
        values.put("foreground", core.getForeground());
        values.put("backgroundStroke", core.getBackgroundStroke());
        values.put("highlightColor", core.getHighlightColor());
        
        db.insert("nodes", null, values);
    }
    
    /**
     * insert connection to database
     * @param db
     * @param parentId
     * @param childId
     * @param connection
     */
    private void insertConnection(SQLiteDatabase db, TAMEConnection connection)
    {
        ContentValues values = new ContentValues();
 
        ITAMGConnection core = connection.getCore();
        values.put("id", connection.getId());
        values.put("parent", connection.getParent().getId());
        values.put("child", connection.getChild().getId());
        values.put("type", core.getType());
        values.put("background", core.getBackground());
        values.put("highlightColor", core.getHighlightColor());
        
        db.insert("connections", null, values);
    }

    private void importGraph(SQLiteDatabase db, TAMEditor editor, SparseArray<TAMENode> nodes)
    {
        Cursor cur = db.query("graph", COLUMNS_GRAPH, null, null, null, null, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_GRAPH);

        cur.moveToFirst();
        // TODO editor.setRoot(nodes.get(cur.getInt(indexes.get("root"))));
        // TODO node counter
        // TODO connection counter
        
        editor.setScaleX(cur.getInt(indexes.get("sx")));
        editor.setScaleY(cur.getInt(indexes.get("sy")));
        editor.setPivotX(cur.getInt(indexes.get("px")));
        editor.setPivotY(cur.getInt(indexes.get("py")));
    }    
    
    /**
     * import all nodes
     * @param db
     * @param factory
     * @return
     */
    private SparseArray<TAMENode> importNodes(SQLiteDatabase db, TAMEItemFactory factory)
    {
        SparseArray<TAMENode> result = new SparseArray<TAMENode>();
        
        Cursor cur = db.query("nodes", COLUMNS_NODES, null, null, null, null, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_NODES);

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            int id = cur.getInt(indexes.get("id"));
            String title = cur.getString(indexes.get("title"));
            String body = cur.getString(indexes.get("body"));
            int type = cur.getInt(indexes.get("type"));
            int x = cur.getInt(indexes.get("x"));
            int y = cur.getInt(indexes.get("y"));
            TAMENode node = factory.importNode(id, x, y, title, body, type);
            ITAMGNode core = node.getCore();
            node.setChildsVisible(intToboolean(cur.getInt(indexes.get("hasVisibleChilds"))));
            core.setBackground(cur.getInt(indexes.get("background")));
            core.setForeground(cur.getInt(indexes.get("foreground")));
            core.setBackgroundStroke(cur.getInt(indexes.get("backgroundStroke")));
            core.setHighlightColor(cur.getInt(indexes.get("highlightColor")));
            
            result.append(id, node);
        }
        
        return result;
    }

    /**
     * import all connections
     * @param db
     * @param factory
     * @param nodes
     */
    private void importConnections(SQLiteDatabase db, TAMEItemFactory factory, SparseArray<TAMENode> nodes)
    {
        Cursor cur = db.query("connections", COLUMNS_CONNECTIONS, null, null, null, null, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_CONNECTIONS);

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            int id = cur.getInt(indexes.get("id"));
            TAMENode parent = nodes.get(cur.getInt(indexes.get("parent")));
            TAMENode child = nodes.get(cur.getInt(indexes.get("child")));
            int type = cur.getInt(indexes.get("type"));
            ITAMGConnection core = factory.importConnection(id, parent, child, type).getCore();
            core.setBackground(cur.getInt(indexes.get("background")));
            core.setHighlightColor(cur.getInt(indexes.get("highlightColor")));
        }        
    }
    
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