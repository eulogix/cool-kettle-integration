/*! ******************************************************************************
*
* Pentaho Data Integration
*
* Copyright (C) 2002-2013 by Pentaho : http://www.pentaho.com
*
*******************************************************************************
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
******************************************************************************/

package com.eulogix.cool.pentaho.steps.file_search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import com.eulogix.cool.lib.App;
import com.eulogix.cool.lib.KettleAppConfiguration;
import com.eulogix.cool.lib.pentaho.CoolStep;
import com.eulogix.cool.lib.pentaho.Environment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This class is part of the demo step plug-in implementation.
 * It demonstrates the basics of developing a plug-in step for PDI. 
 * 
 * The demo step adds a new string field to the row stream and sets its
 * value to "Hello World!". The user may select the name of the new field.
 *   
 * This class is the implementation of StepInterface.
 * Classes implementing this interface need to:
 * 
 * - initialize the step
 * - execute the row processing logic
 * - dispose of the step 
 * 
 * Please do not create any local fields in a StepInterface class. Store any
 * information related to the processing logic in the supplied step data interface
 * instead.  
 * 
 */

public class FileSearchStep extends CoolStep implements StepInterface {

	/**
	 * The constructor should simply pass on its arguments to the parent class.
	 * 
	 * @param s 				step description
	 * @param stepDataInterface	step data class
	 * @param c					step copy
	 * @param t					transformation description
	 * @param dis				transformation executing
	 */
	public FileSearchStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}
	
	/**
	 * This method is called by PDI during transformation startup. 
	 * 
	 * It should initialize required for step execution. 
	 * 
	 * The meta and data implementations passed in can safely be cast
	 * to the step's respective implementations. 
	 * 
	 * It is mandatory that super.init() is called to ensure correct behavior.
	 * 
	 * Typical tasks executed here are establishing the connection to a database,
	 * as wall as obtaining resources, like file handles.
	 * 
	 * @param smi 	step meta interface implementation, containing the step settings
	 * @param sdi	step data interface implementation, used to store runtime information
	 * 
	 * @return true if initialization completed successfully, false if there was an error preventing the step from working. 
	 *  
	 */
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		// Casting to step-specific implementation classes is safe
		FileSearchStepMeta meta = (FileSearchStepMeta) smi;
		FileSearchStepData data = (FileSearchStepData) sdi;

		return super.init(meta, data);
	}	

	/**
	 * Once the transformation starts executing, the processRow() method is called repeatedly
	 * by PDI for as long as it returns true. To indicate that a step has finished processing rows
	 * this method must call setOutputDone() and return false;
	 * 
	 * Steps which process incoming rows typically call getRow() to read a single row from the
	 * input stream, change or add row content, call putRow() to pass the changed row on 
	 * and return true. If getRow() returns null, no more rows are expected to come in, 
	 * and the processRow() implementation calls setOutputDone() and returns false to
	 * indicate that it is done too.
	 * 
	 * Steps which generate rows typically construct a new row Object[] using a call to
	 * RowDataUtil.allocateRowData(numberOfFields), add row content, and call putRow() to
	 * pass the new row on. Above process may happen in a loop to generate multiple rows,
	 * at the end of which processRow() would call setOutputDone() and return false;
	 * 
	 * @param smi the step meta interface containing the step settings
	 * @param sdi the step data interface that should be used to store
	 * 
	 * @return true to indicate that the function should be called again, false if the step is done
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		// safely cast the step settings (meta) and runtime info (data) to specific implementations 
		FileSearchStepMeta meta = (FileSearchStepMeta) smi;
		FileSearchStepData data = (FileSearchStepData) sdi;

		// get incoming row, getRow() potentially blocks waiting for more rows, returns null if no more rows expected
		Object[] r = getRow(); 
		
		// if no more rows are expected, indicate step is finished and processRow() should not be called again
		if (r == null){
			setOutputDone();
			return false;
		}

		// the "first" flag is inherited from the base step implementation
		// it is used to guard some processing tasks, like figuring out field indexes
		// in the row structure that only need to be done once
		if (first) {
			first = false;
			// clone the input row structure and place it in our data object
			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			// use meta.getFields() to change it, so it reflects the output row structure 
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this, null, null);
		}
		 
		App app = new Environment(this).getDefinedCoolApp(meta.fields.get("coolEnvironment").toString());
		
		if(app != null) {
			
			String schemaName = getFieldValueAsString(r, meta.fields.get("schemaName").toString());
			String actualSchema = getFieldValueAsString(r, meta.fields.get("actualSchema").toString());
			String table = getFieldValueAsString(r, meta.fields.get("table").toString());
			String pk = getFieldValueAsString(r, meta.fields.get("pk").toString());
			String category = getFieldValueAsString(r, meta.fields.get("category").toString());
			String fileName = getFieldValueAsString(r, meta.fields.get("fileName").toString());
			int limit = Integer.parseInt( meta.fields.get("limit").toString() );
			boolean fetchContent = meta.fields.get("fetchContent").toString().equals("yes");
			
			Object[] outputRow;
			
			JsonObject search;
			JsonObject file;
			JsonArray files;
			
			int page = 0, totalPages = 0;
			do{
				 search = app.searchFiles(schemaName, actualSchema, table, pk, category, fileName, fetchContent, page);
				 totalPages = search.get("pages_count").getAsInt();
				 files = search.get("files").getAsJsonArray();
				 
				 for(int i=0; i<files.size(); i++) {
					 file = files.get(i).getAsJsonObject();
					 
					 outputRow = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 10, (long) file.get("file_id").getAsInt());
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 9, file.get("path").getAsString());
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 8, file.get("source_table").getAsString());
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 7, (long) file.get("source_table_id").getAsInt());
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 6, file.get("category").isJsonNull() ? null : file.get("category").getAsString());
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 5, file.get("file_name").getAsString());
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 4, file.get("file_size").isJsonNull() ? null : (long) file.get("file_size").getAsInt());
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 3, file.get("uploaded_by_user").isJsonNull() ? null : (long) file.get("uploaded_by_user").getAsInt());
					 
					 if (file.has("content")) {
						 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 2,  file.get("content").isJsonNull() ? null : Base64.getDecoder().decode( file.get("content").getAsString() )) ;
					 } else outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 2, null);
					 
					 outputRow = RowDataUtil.addValueData(outputRow, data.outputRowMeta.size() - 1, file.get("properties").isJsonNull() ? null : file.get("properties").getAsString());
					 
					 putRow(data.outputRowMeta, outputRow);
				 }
				 
			} while(page++ < totalPages);
				
		} 
	
		// log progress if it is time to to so
		if (checkFeedback(getLinesRead())) {
			logBasic("Linenr " + getLinesRead()); // Some basic logging
		}

		// indicate that processRow() should be called again
		return true;
	}
	
	public Object getFieldValue(Object[] rowData, String fieldName) {
		int idx = getInputRowMeta().indexOfValue( fieldName );
		return idx == -1 ? null : rowData[ idx ];
	}
	
	public String getFieldValueAsString(Object[] rowData, String fieldName) {
		Object o = getFieldValue(rowData, fieldName);
		return o == null ? null : o.toString();
	}
	
	/**
	 * This method is called by PDI once the step is done processing. 
	 * 
	 * The dispose() method is the counterpart to init() and should release any resources
	 * acquired for step execution like file handles or database connections.
	 * 
	 * The meta and data implementations passed in can safely be cast
	 * to the step's respective implementations. 
	 * 
	 * It is mandatory that super.dispose() is called to ensure correct behavior.
	 * 
	 * @param smi 	step meta interface implementation, containing the step settings
	 * @param sdi	step data interface implementation, used to store runtime information
	 */
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {

		// Casting to step-specific implementation classes is safe
		FileSearchStepMeta meta = (FileSearchStepMeta) smi;
		FileSearchStepData data = (FileSearchStepData) sdi;
		
		super.dispose(meta, data);
	}

}
