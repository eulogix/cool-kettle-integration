package com.eulogix.cool.pentaho.steps.file_set_properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import com.eulogix.cool.lib.App;
import com.eulogix.cool.lib.pentaho.CoolStepMeta;
import com.eulogix.cool.lib.pentaho.Environment;

@Step(	
		id = "CoolFileSetPropertiesStep",
		image = "file-set-properties.png",
		i18nPackageName="com.eulogix.cool.pentaho.steps.file_set_properties",
		name="FileSetPropertiesStep.Name",
		description = "FileSetPropertiesStep.TooltipDesc",
		categoryDescription="FileSetPropertiesStep.Category"
)
public class FileSetPropertiesStepMeta extends CoolStepMeta implements StepMetaInterface {
	
	public static final String VAR_SOURCE_STREAM = "Stream";
	public static final String VAR_SOURCE_VAR = "TextVar";
	

	/**
	 *	The PKG member is used when looking up internationalized strings.
	 *	The properties file with localized keys is expected to reside in 
	 *	{the package of the class specified}/messages/messages_{locale}.properties   
	 */
	private static Class<?> PKG = FileSetPropertiesStepMeta.class; // for i18n purposes
	
	public ArrayList<HashMap<String,String>> properties = new ArrayList<HashMap<String,String>>();
	
	/**
	 * Constructor should call super() to make sure the base class has a chance to initialize properly.
	 */
	public FileSetPropertiesStepMeta() {
		super();
	}
	
	public void setUpFields() {
		fields = new LinkedHashMap<String, Object>();
		fields.put("coolEnvironment", "");
		fields.put("schemaName", "");
		fields.put("actualSchema", "");
		fields.put("fileId", "");
		fields.put("merge", "");
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
		return new FileSetPropertiesStepDialog(shell, meta, transMeta, name);
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
		return new FileSetPropertiesStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	/**
	 * Called by PDI to get a new instance of the step data class.
	 */
	public StepDataInterface getStepData() {
		return new FileSetPropertiesStepData();
	}	

	/**
	 * This method is called every time a new step is created and should allocate/set the step configuration
	 * to sensible defaults. The values set here will be used by Spoon when a new step is created.    
	 */
	public void setDefault() {
		/*fields.put("schemaName", "core");
		fields.put("actualSchema", "core");*/
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
		FileSetPropertiesStepMeta retval = (FileSetPropertiesStepMeta) super.clone();
		retval.setUpFields();
		retval.properties = deepCloneTableData(properties);
		return retval;
	}
	
	public String getXML() throws KettleValueException {
		return super.getXML() 
			   + getTableXML("properties", properties);
	}
	
	/**
	 * This method is called by PDI when a step needs to load its configuration from XML.
	 * 
	 * Please use org.pentaho.di.core.xml.XMLHandler to conveniently read from the
	 * XML node passed in.
	 * 
	 * @param stepnode	the XML node containing the configuration
	 * @param databases	the databases available in the transformation
	 * @param metaStore the metaStore to optionally read from
	 */
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
		super.loadXML(stepnode, databases, metaStore);
		properties = loadXMLTableData(stepnode, "properties", databases, metaStore);
	}
	
	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step) throws KettleException
	{
		super.saveRep(rep, metaStore, id_transformation, id_step);
		saveTableToRep(properties, "properties", rep, metaStore, id_transformation, id_step);
	}	
	
	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases) throws KettleException  {
		super.readRep(rep, metaStore, id_step, databases);
		properties = readTableFromRep("properties", rep, metaStore, id_step, databases);
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

		/*
		 * This implementation appends the outputField to the row-stream
		 */

		ValueMetaInterface pName = new ValueMeta("success", ValueMeta.TYPE_BOOLEAN);
		pName.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
		pName.setOrigin(name);		// the name of the step that adds this field  
		inputRowMeta.addValueMeta(pName);
		
	}
}
