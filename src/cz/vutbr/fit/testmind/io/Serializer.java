package cz.vutbr.fit.testmind.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMEItem;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGZoom;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;
import cz.vutbr.fit.testmind.profile.Tag;

/**
 * class for serializing 
 * @author jules
 *
 */
public class Serializer
{
    static private final String INIT_PRAGMA = "PRAGMA journal_mode=DELETE";
    static private final String CREATE_TABLE_PROFILE = "CREATE TABLE \"profile\" (" +
            "\"root\" id NOT NULL," +
            "\"nodeCounter\" int NOT NULL," +
            "\"connectionCounter\" int NOT NULL," +
            "FOREIGN KEY(root) REFERENCES node(id))";
    static private final String CREATE_TABLE_EDITORS = "CREATE TABLE \"editors\" (" +
            "\"name\" TEXT UNIQUE NOT NULL," +
            "\"sx\" real NOT NULL," +
            "\"sy\" real NOT NULL," +
            "\"px\" real NOT NULL," +
            "\"py\" real NOT NULL," +
            "\"tx\" real NOT NULL," +
            "\"ty\" real NOT NULL)";
    static private final String CREATE_TABLE_NODE_REFERENCES = "CREATE TABLE \"node_references\" (" +
            "\"node\" int NOT NULL," +
            "\"editor\" TEXT NOT NULL," +
            "\"type\" int NOT NULL," +
            "\"x\" int NOT NULL," +
            "\"y\" int NOT NULL," +
            "\"background\" int," +
            "\"backgroundStroke\" int," +
            "\"backgroundStyle\" TEXT," +
            "\"foreground\" int," +
            "\"highlightColor\" int," +
            "FOREIGN KEY(node) REFERENCES node(id)," +
            "FOREIGN KEY(editor) REFERENCES editor(id))";
    static private final String CREATE_TABLE_CONNECTION_REFERENCES = "CREATE TABLE \"connection_references\" (" +
            "\"id\" INTEGER PRIMARY KEY," +
            "\"connection\" int NOT NULL," +
            "\"editor\" TEXT NOT NULL," +
            "\"type\" int NOT NULL," +
            "\"background\" int," +
            "\"highlightColor\" int," +
            "FOREIGN KEY(connection) REFERENCES connection(id)," +
            "FOREIGN KEY(editor) REFERENCES editor(id))";
    static private final String CREATE_TABLE_CONNECTION_MIDDLEPOINTS = "CREATE TABLE \"connection_middlepoints\" (" +
            "\"connection_reference\" int NOT NULL," +
            "\"x\" int NOT NULL," +
            "\"y\" int NOT NULL," +
            "\"order_points\" int NOT NULL," +
            "FOREIGN KEY(connection_reference) REFERENCES connection_references(id))";
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
    static private final String CREATE_TABLE_TAGS = "CREATE TABLE \"tags\" (" +
            "\"node\" int NOT NULL," +
            "\"name\" TEXT NOT NULL," +
            "\"start\" int NOT NULL," +
            "\"end\" int NOT NULL," +
            "FOREIGN KEY(node) REFERENCES node(id))";
    
    static private final String SELECT_PROFILE = "SELECT profile.nodeCounter, profile.connectionCounter, " +
            "nodes.id, nodes.title, nodes.body " +
            "from profile inner join nodes on profile.root = nodes.id";
    
    static private final String[] COLUMNS_PROFILE = {"nodeCounter", "connectionCounter", "id", "title", "body"};    
    static private final String[] COLUMNS_EDITORS = {"name", "sx", "sy", "px", "py", "tx", "ty"};
    static private final String[] COLUMNS_NODES = {"id", "title", "body"};
    static private final String[] COLUMNS_CONNECTIONS = {"id", "parent", "child"};
    static private final String[] COLUMNS_NODE_REFERENCES = {"node", "editor", "type", "x", "y",
                                                   "background", "backgroundStroke", "backgroundStyle", "foreground", "highlightColor"};
    static private final String[] COLUMNS_CONNECTION_REFERENCES = {"id", "connection", "editor", "type",
                                                                   "background", "highlightColor"};
    static private final String[] COLUMNS_CONNECTION_MIDDLEPOINTS = {"connection_reference", "x", "y", "order_points"};
    static private final String[] COLUMNS_TAGS = {"node", "name", "start", "end"};
	private static final String TAG = "Serializer";

    
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
        removeJournal();
    }
    
    /**
     * load nodes and connections from db file
     * @param editor_main
     */
    public void deserialize(TAMProfile profile)
    {
        SQLiteDatabase db;
        
        try
        {
            db = openDB();
        }
        catch (SQLiteCantOpenDatabaseException e)
        {
            Context context = profile.getListOfEditors().get(0).getContext();
            String path = String.format(context.getString(R.string.cannot_open_file), fileDB.getAbsolutePath());
            Toast.makeText(context, path, Toast.LENGTH_LONG).show();
            return;
        }        
        
        SparseArray<TAMPNode> nodes = loadProfile(db, profile);
        HashMap<String, ITAMEditor> editors = loadEditors(db, profile);

        loadNodes(db, profile, nodes);
        SparseArray<TAMPConnection> connections = loadConnections(db, profile, nodes);

        loadNodeReferences(db, editors, nodes);
        loadConnectionReferences(db, editors, connections);
        loadTags(db, nodes);
        
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
        db.rawQuery(INIT_PRAGMA, null);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_EDITORS);
        db.execSQL(CREATE_TABLE_NODES);
        db.execSQL(CREATE_TABLE_CONNECTIONS);
        db.execSQL(CREATE_TABLE_NODE_REFERENCES);
        db.execSQL(CREATE_TABLE_CONNECTION_REFERENCES);
        db.execSQL(CREATE_TABLE_CONNECTION_MIDDLEPOINTS);
        db.execSQL(CREATE_TABLE_TAGS);
        
        return db;
    }
    
    /**
     * open sqlite file
     * @return
     */
    private SQLiteDatabase openDB()
    {    	
        return SQLiteDatabase.openDatabase(fileDB.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
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
        PointF translation = editor.getTranslation();
        
        ContentValues values = new ContentValues();
        values.put("name", editor.getClass().getName());
        values.put("sx", zoom.sx);
        values.put("sy", zoom.sy);
        values.put("px", zoom.px);
        values.put("py", zoom.py);        
        values.put("tx", translation.x);
        values.put("ty", translation.y);
        
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
        
        // insert all tags
        for(Tag tag: node.getListOfTags())
        {
            ContentValues valuesTag = new ContentValues();
            valuesTag.put("node", node.getId());
            valuesTag.put("name", tag.getTag());
            valuesTag.put("start", tag.getStart());            
            valuesTag.put("end", tag.getEnd());
            
            db.insert("tags", null, valuesTag);
        }
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
        values.put("background", gui.getColorBackground());
        values.put("backgroundStroke", gui.getBackgroundStroke());
        values.put("backgroundStyle", node.getBackgroundStyle());
        values.put("foreground", gui.getColorText());
        values.put("highlightColor", gui.getColorBackgroundHighlight());
        
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
        values.put("background", gui.getColorBackground());
        values.put("highlightColor", gui.getColorBackgroundHighlight());
        
        long id = db.insert("connection_references", null, values);
        
        insertConnectionMiddlepoints(db,gui.getListOfMiddlePoints(), id);
    }
    
    /**
     * insert middlepoints of connection to database
     * @param db
     * @param connection
     * @param connection_id
     */
    private void insertConnectionMiddlepoints(SQLiteDatabase db, List<Point> points, long connectionId)
    {
        int size = points.size();
        
        for(int i=0; i < size; i++)
        {
            ContentValues values = new ContentValues();
            values.put("connection_reference", connectionId);
            values.put("x", points.get(i).x);
            values.put("y", points.get(i).y);
            values.put("order_points", i);
            
            db.insert("connection_middlepoints", null, values);   
        }
    }
    
    /**
     * load profile from db
     * @param db
     * @param profile
     * @return
     */
    private SparseArray<TAMPNode> loadProfile(SQLiteDatabase db, TAMProfile profile)
    {
        Cursor cur = db.rawQuery(SELECT_PROFILE, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_PROFILE);
    
        cur.moveToFirst();

        profile.reset(cur.getInt(indexes.get("nodeCounter")), cur.getInt(indexes.get("connectionCounter")));
        String title = cur.getString(indexes.get("title"));
        String body = cur.getString(indexes.get("body"));
        int id = cur.getInt(indexes.get("id"));
        
        SparseArray<TAMPNode> nodes = new SparseArray<TAMPNode>();
        
        nodes.put(id, profile.importRoot(title, body, id));
        
        return nodes;
    }
    
    /**
     * load editors from database
     * @param db
     * @param profile
     */
    private HashMap<String, ITAMEditor> loadEditors(SQLiteDatabase db, TAMProfile profile)
    {
        HashMap<String, ITAMEditor> editors = new HashMap<String, ITAMEditor>();
        for (ITAMEditor editor: profile.getListOfEditors())
        {       
            editors.put(editor.getClass().getName(), editor);
        }
        
        Cursor cur = db.query("editors", COLUMNS_EDITORS, null, null, null, null, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_EDITORS);

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            ITAMEditor editor = editors.get(cur.getString(indexes.get("name")));
                    
            float sx = cur.getFloat(indexes.get("sx"));
            float sy = cur.getFloat(indexes.get("sy"));
            float px = cur.getFloat(indexes.get("px"));
            float py = cur.getFloat(indexes.get("py"));            
            float tx = cur.getFloat(indexes.get("tx"));
            float ty = cur.getFloat(indexes.get("ty"));
            
            editor.zoom(sx, sy, px, py);          
            editor.translate(tx, ty);
        }
        
        return editors;
    }    
    
    /**
     * load all nodes
     * @param db
     * @param profile
     * @param nodes
     */
    private void loadNodes(SQLiteDatabase db, TAMProfile profile, SparseArray<TAMPNode> nodes)
    {
        String[] arguments = {Integer.toString(profile.getRoot().getId())};
        Cursor cur = db.query("nodes", COLUMNS_NODES, "id != ?", arguments, null, null, null);
        
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_NODES);

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            int id = cur.getInt(indexes.get("id"));
            String title = cur.getString(indexes.get("title"));
            String body = cur.getString(indexes.get("body"));
            
            nodes.append(id, profile.importNode(title, body, id));
        }
    }

    /**
     * load all connections
     * @param db
     * @param profile
     * @param nodes
     * @return
     */
    private SparseArray<TAMPConnection> loadConnections(SQLiteDatabase db, TAMProfile profile, SparseArray<TAMPNode> nodes)
    {
        SparseArray<TAMPConnection> connections = new SparseArray<TAMPConnection>();
        Cursor cur = db.query("connections", COLUMNS_CONNECTIONS, null, null, null, null, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_CONNECTIONS);

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            int id = cur.getInt(indexes.get("id"));
            int parent = cur.getInt(indexes.get("parent"));
            int child = cur.getInt(indexes.get("child"));
            
            connections.append(id, profile.importConnection(nodes.get(parent), nodes.get(child), id));
        }
        
        return connections;
    }
    
    /**
     * load node references
     * @param db
     * @param editors
     * @param nodes
     */
    private void loadNodeReferences(SQLiteDatabase db,
            HashMap<String, ITAMEditor> editors, SparseArray<TAMPNode> nodes)
    {
        Cursor cur = db.query("node_references", COLUMNS_NODE_REFERENCES, null, null, null, null, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_NODE_REFERENCES);
        
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            TAMPNode node = nodes.get(cur.getInt(indexes.get("node")));
            ITAMEditor editor = editors.get(cur.getString(indexes.get("editor")));
            int type = cur.getInt(indexes.get("type"));
            int x = cur.getInt(indexes.get("x"));
            int y = cur.getInt(indexes.get("y"));
            ITAMENode eNode = TAMPConnectionFactory.addEReference(node, editor, x, y, type);
            
            ITAMGNode gNode = eNode.getGui();
            
            eNode.setBackgroundStyle(cur.getInt(indexes.get("backgroundStyle")));
            gNode.setColorText(cur.getInt(indexes.get("foreground")));
            gNode.setColorBackgroundHighlight(cur.getInt(indexes.get("highlightColor")));
        }           
    }
    
    /**
     * load connection references
     * @param db
     * @param editors
     * @param connections
     */
    private void loadConnectionReferences(SQLiteDatabase db,
            HashMap<String, ITAMEditor> editors, SparseArray<TAMPConnection> connections)
    {
        Cursor cur = db.query("connection_references", COLUMNS_CONNECTION_REFERENCES, null, null, null, null, null);
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_CONNECTION_REFERENCES);
        
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            TAMPConnection connection = connections.get(cur.getInt(indexes.get("connection")));
            ITAMEditor editor = editors.get(cur.getString(indexes.get("editor")));
            int type = cur.getInt(indexes.get("type"));
            
            ITAMGConnection gConnection = TAMPConnectionFactory.addEReference(connection, editor, type).getGui();
            
            gConnection.setColorBackground(cur.getInt(indexes.get("background")));
            gConnection.setColorBackgroundHighlight(cur.getInt(indexes.get("highlightColor")));
            
            gConnection.setListOfMiddlePoints(getMiddlepoints(db, cur.getInt(indexes.get("id"))));
        }           
    }
        
    /**
     * load middlepoints by connection
     * @param db
     * @param connectionId
     * @return
     */
    private List<Point> getMiddlepoints(SQLiteDatabase db, int connectionId)
    {
        List<Point> result = new ArrayList<Point>();
        
        Cursor cur = db.query("connection_middlepoints", COLUMNS_CONNECTION_MIDDLEPOINTS, "connection_reference = ?",
                              new String[] {Integer.toString(connectionId)}, null, null, "order_points");
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_CONNECTION_MIDDLEPOINTS);
        
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            int x = cur.getInt(indexes.get("x"));
            int y = cur.getInt(indexes.get("y"));
            result.add(new Point(x, y));
        }
        
        return result;
    }
    
    /**
     * load tags from database
     * @param db
     * @param nodes
     */
    private void loadTags(SQLiteDatabase db, SparseArray<TAMPNode> nodes)
    {
        Cursor cur = db.query("tags", COLUMNS_TAGS, null, null, null, null, null);  
        HashMap<String, Integer> indexes = getIndexesCursor(cur, COLUMNS_TAGS);
        
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            int nodeId = cur.getInt(indexes.get("node"));;
            String name = cur.getString(indexes.get("name"));
            Tag tag = new Tag(name);
            
            tag.setStart(cur.getInt(indexes.get("start")));
            tag.setEnd(cur.getInt(indexes.get("end")));
            
            TAMPNode node = nodes.get(nodeId);
            List<Tag> listOfTags = node.getListOfTags();
            if(listOfTags == null)
            {
                listOfTags = new ArrayList<Tag>();
                node.setListOfTags(listOfTags);
            }
            listOfTags.add(tag);
        }
    }
    
    /**
     * remove journal file
     */
    private void removeJournal()
    {
        String path = fileDB.getAbsolutePath() + "-journal";
        File fileJournal = new File(path);
        if(fileJournal.exists())
        {
            fileJournal.delete();
        }
    }
    
    // static private methods =================================================
    
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