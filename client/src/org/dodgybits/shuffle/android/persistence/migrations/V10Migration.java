package org.dodgybits.shuffle.android.persistence.migrations;

import org.dodgybits.shuffle.android.persistence.provider.TaskProvider;

import android.database.sqlite.SQLiteDatabase;

public class V10Migration extends AbstractMigration {

	@Override
	public void migrate(SQLiteDatabase db) {
		// Shuffle v1.1 (1st release)
		createTaskProjectIdIndex(db);
		createTaskContextIdIndex(db);
		// no break since we want it to fall through
	}
	private void createTaskProjectIdIndex(SQLiteDatabase db) {
        db.execSQL("DROP INDEX IF EXISTS taskProjectIdIndex");
		db.execSQL("CREATE INDEX taskProjectIdIndex ON " + TaskProvider.cTaskTableName
				+ " (" + TaskProvider.Tasks.PROJECT_ID + ");");
	}
	private void createTaskContextIdIndex(SQLiteDatabase db) {
        db.execSQL("DROP INDEX IF EXISTS taskContextIdIndex");
		db.execSQL("CREATE INDEX taskContextIdIndex ON " + TaskProvider.cTaskTableName
				+ " (" + TaskProvider.Tasks.CONTEXT_ID + ");");
	}

}
