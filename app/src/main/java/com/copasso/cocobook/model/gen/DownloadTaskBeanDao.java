package com.copasso.cocobook.model.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.copasso.cocobook.model.bean.DownloadTaskBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWNLOAD_TASK_BEAN".
*/
public class DownloadTaskBeanDao extends AbstractDao<DownloadTaskBean, String> {

    public static final String TABLENAME = "DOWNLOAD_TASK_BEAN";

    /**
     * Properties of entity DownloadTaskBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property TaskName = new Property(0, String.class, "taskName", true, "TASK_NAME");
        public final static Property BookId = new Property(1, String.class, "bookId", false, "BOOK_ID");
        public final static Property CurrentChapter = new Property(2, int.class, "currentChapter", false, "CURRENT_CHAPTER");
        public final static Property LastChapter = new Property(3, int.class, "lastChapter", false, "LAST_CHAPTER");
        public final static Property Status = new Property(4, int.class, "status", false, "STATUS");
        public final static Property Size = new Property(5, long.class, "size", false, "SIZE");
    }

    private DaoSession daoSession;


    public DownloadTaskBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DownloadTaskBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWNLOAD_TASK_BEAN\" (" + //
                "\"TASK_NAME\" TEXT PRIMARY KEY NOT NULL ," + // 0: taskName
                "\"BOOK_ID\" TEXT," + // 1: bookId
                "\"CURRENT_CHAPTER\" INTEGER NOT NULL ," + // 2: currentChapter
                "\"LAST_CHAPTER\" INTEGER NOT NULL ," + // 3: lastChapter
                "\"STATUS\" INTEGER NOT NULL ," + // 4: status
                "\"SIZE\" INTEGER NOT NULL );"); // 5: size
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWNLOAD_TASK_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownloadTaskBean entity) {
        stmt.clearBindings();
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(1, taskName);
        }
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(2, bookId);
        }
        stmt.bindLong(3, entity.getCurrentChapter());
        stmt.bindLong(4, entity.getLastChapter());
        stmt.bindLong(5, entity.getStatus());
        stmt.bindLong(6, entity.getSize());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownloadTaskBean entity) {
        stmt.clearBindings();
 
        String taskName = entity.getTaskName();
        if (taskName != null) {
            stmt.bindString(1, taskName);
        }
 
        String bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindString(2, bookId);
        }
        stmt.bindLong(3, entity.getCurrentChapter());
        stmt.bindLong(4, entity.getLastChapter());
        stmt.bindLong(5, entity.getStatus());
        stmt.bindLong(6, entity.getSize());
    }

    @Override
    protected final void attachEntity(DownloadTaskBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public DownloadTaskBean readEntity(Cursor cursor, int offset) {
        DownloadTaskBean entity = new DownloadTaskBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // taskName
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bookId
            cursor.getInt(offset + 2), // currentChapter
            cursor.getInt(offset + 3), // lastChapter
            cursor.getInt(offset + 4), // status
            cursor.getLong(offset + 5) // size
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownloadTaskBean entity, int offset) {
        entity.setTaskName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setBookId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCurrentChapter(cursor.getInt(offset + 2));
        entity.setLastChapter(cursor.getInt(offset + 3));
        entity.setStatus(cursor.getInt(offset + 4));
        entity.setSize(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final String updateKeyAfterInsert(DownloadTaskBean entity, long rowId) {
        return entity.getTaskName();
    }
    
    @Override
    public String getKey(DownloadTaskBean entity) {
        if(entity != null) {
            return entity.getTaskName();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownloadTaskBean entity) {
        return entity.getTaskName() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
