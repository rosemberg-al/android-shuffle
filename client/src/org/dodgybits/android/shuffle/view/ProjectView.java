/*
 * Copyright (C) 2009 Android Shuffle Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dodgybits.android.shuffle.view;

import org.dodgybits.android.shuffle.R;
import org.dodgybits.android.shuffle.model.Project;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.widget.TextView;

public class ProjectView extends ItemView<Project> {
	private TextView mName;
	private SparseIntArray mTaskCountArray;
	
	public ProjectView(Context androidContext) {
		super(androidContext);
		
        LayoutInflater vi = (LayoutInflater)androidContext.
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(getViewResourceId(), this, true); 
		
		mName = (TextView) findViewById(R.id.name);
	}
	
	protected int getViewResourceId() {
		return R.layout.list_item_view;
	}

	
	public void setTaskCountArray(SparseIntArray taskCountArray) {
		mTaskCountArray = taskCountArray;
	}
	
	@Override
	public void updateView(Project project) {
		if (mTaskCountArray != null) {
			Integer count = mTaskCountArray.get(project.id.intValue());
			if (count == null) count = 0;
			mName.setText(project.name + " (" + count + ")");
		} else {
			mName.setText(project.name);
		}
		
	}

}