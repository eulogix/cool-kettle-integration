package com.eulogix.cool.pentaho.steps.file_search;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;

import com.eulogix.cool.lib.pentaho.CoolStepMeta;

@Step(	
		id = "CoolFileSearchStep",
		image = "file-search.png",
		i18nPackageName="com.eulogix.cool.pentaho.steps.file_search",
		name="FileSearchStep.Name",
		description = "FileSearchStep.TooltipDesc",
		categoryDescription="FileSearchStep.Category"
)
public class FileSearchStepMeta extends CoolStepMeta implements StepMetaInterface {

	/**
	 *	The PKG member is used when looking up internationalized strings.
	 *	The properties file with localized keys is expected to reside in 
	 *	{the package of the class specified}/messages/messages_{locale}.properties   
	 */
	private static Class<?> PKG = FileSearchStepMeta.class; // for i18n purposes
	
	/**
	 * Constructor should call super() to make sure the base class has a chance to initialize properly.
	 */
	public FileSearchStepMeta() {
		super();
	}
	
	public void setUpFields() {
		fields = new LinkedHashMap<String, Object>();
		fields.put("coolEnvironment", "");
		fields.put("schemaName", "");
		fields.put("actualSchema", "");
		fields.put("table", "");
		fields.put("pk", "");
		fields.put("category", "");
		fields.put("fileName", "");
		fields.put("limit", "");
		fields.put("fetchContent", "");
	}
	
	/**
	 * Called by Spoon to get a new instance of the SWT dialog for the step.
	 * A standard implementation passing the arguments to the constructor of the step dialog is recommended.
	 * 
	 * @param shell		an SWT Shell
	 * @param meta 		description of the step 
	 * @param transMeta	description of the the transformation 
	 * @param name		the name of the step
	 * @return 			new instance of a dialog for this step 
	 */
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new FileSearchStepDialog(shell, meta, transMeta, name);
	}

	/**
	 * Called by PDI to get a new instance of the step implementation. 
	 * A standard implementation passing the arguments to the constructor of the step class is recommended.
	 * 
	 * @param stepMeta				description of the step
	 * @param stepDataInterface		instance of a step data class
	 * @param cnr					copy number
	 * @param transMeta				description of the transformation
	 * @param disp					runtime implementation of the transformation
	 * @return						the new instance of a step implementation 
	 */
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
		return new FileSearchStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	/**
	 * Called by PDI to get a new instance of the step data class.
	 */
	public StepDataInterface getStepData() {
		return new FileSearchStepData();
	}	

	/**
	 * This method is called every time a new step is created and should allocate/set the step configuration
	 * to sensible defaults. The values set here will be used by Spoon when a new step is created.    
	 */
	public void setDefault() {
		fields.put("schemaName", "core");
		fields.put("actualSchema", "core");
		fields.put("limit", "0");
		fields.put("fetchContent", "no");
	}

	/**
	 * This method is used when a step is duplicated in Spoon. It needs to return a deep copy of this
	 * step meta object. Be sure to create proper deep copies if the step configuration is stored in
	 * modifiable objects.
	 * 
	 * See org.pentaho.di.trans.steps.rowgenerator.RowGeneratorMeta.clone() for an example on creating
	 * a deep copy.
	 * 
	 * @return a deep copy of this
	 */
	public Object clone() {
		FileSearchStepMeta retval = (FileSearchStepMeta) super.clone();
		retval.setUpFields();
		return retval;
	}
	
	/**
	 * This method is called to determine the changes the step is making to the row-stream.
	 * To that end a RowMetaInterface object is passed in, containing the row-stream structure as it is when entering
	 * the step. This method must apply any changes the step makes to the row stream. Usually a step adds fields to the
	 * row-stream.
	 * 
	 * @param inputRowMeta		the row structure coming in to the step
	 * @param name 				the name of the step making the changes
	 * @param info				row structures of any info steps coming in
	 * @param nextStep			the description of a step this step is passing rows to
	 * @param space				the variable space for resolving variables
	 * @param repository		the repository instance optionally read from
	 * @param metaStore			the metaStore to optionally read from
	 */
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException{
		
		ArrayList<ValueMetaInterface> metas = new ArrayList<ValueMetaInterface>(); 

		// a value meta object contains the meta data for a field
		metas.add( new ValueMeta("file_id", ValueMeta.TYPE_INTEGER) );
		metas.add( new ValueMeta("path", ValueMeta.TYPE_STRING) );
		metas.add( new ValueMeta("source_table", ValueMeta.TYPE_STRING) );
		metas.add( new ValueMeta("source_table_id", ValueMeta.TYPE_INTEGER) );
		metas.add( new ValueMeta("category", ValueMeta.TYPE_STRING) );
		metas.add( new ValueMeta("file_name", ValueMeta.TYPE_STRING) );
		metas.add( new ValueMeta("file_size", ValueMeta.TYPE_INTEGER) );
		metas.add( new ValueMeta("uploaded_by_user", ValueMeta.TYPE_INTEGER) );
		metas.add( new ValueMeta("content", ValueMeta.TYPE_BINARY) );
		metas.add( new ValueMeta("json_properties", ValueMeta.TYPE_STRING) );

		for(int i=0;i<metas.size();i++) {
			metas.get(i).setTrimType(ValueMeta.TRIM_TYPE_BOTH);
			metas.get(i).setOrigin(name);
			inputRowMeta.addValueMeta(metas.get(i));
		}

	}

}
