package com.eulogix.cool.pentaho.steps.file_set_properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.FieldDisabledListener;
import com.eulogix.cool.lib.pentaho.CoolStepDialog;
import com.eulogix.cool.lib.pentaho.ui.Grid;

public class FileSetPropertiesStepDialog extends CoolStepDialog implements StepDialogInterface {
    
  //this is the object the stores the step's settings
	// the dialog reads the settings from it when opening
	// the dialog writes the settings to it when confirmed 
  private FileSetPropertiesStepMeta meta;

  private Grid propertiesGrid;
  
  private Map<String, Integer> inputFields;

  private Button wGetFromStream;
  
  private String[] fieldNames;

  private CTabFolder wTabFolder;

  private CTabItem wGeneralTab, wAdditionalTab;
  private FormData fdTabFolder;

  private Composite wGeneralComp, wAdditionalComp;
  private FormData fdGeneralComp, fdAdditionalComp;

  

  public FileSetPropertiesStepDialog( Shell parent, Object in, TransMeta transMeta, String sname ) {
    super( parent, (BaseStepMeta) in, transMeta, sname );
    meta = (FileSetPropertiesStepMeta) in;
    PKG = FileSetPropertiesStepMeta.class;
    messagesPrefix = "FileSetPropertiesStep";		
    
    inputFields = new HashMap<String, Integer>();
  }

  public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN );
    props.setLook( shell );
    setShellImage( shell, meta );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        meta.setChanged();
      }
    };

    changed = meta.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "FileSetPropertiesStep.Shell.Title" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Stepname line
    wlStepname = new Label( shell, SWT.RIGHT );
    wlStepname.setText( BaseMessages.getString( PKG, "FileSetPropertiesStep.StepName.Label" ) );
    props.setLook( wlStepname );
    fdlStepname = new FormData();
    fdlStepname.left = new FormAttachment( 0, 0 );
    fdlStepname.right = new FormAttachment( middle, -margin );
    fdlStepname.top = new FormAttachment( 0, margin );
    wlStepname.setLayoutData( fdlStepname );
    wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wStepname.setText( stepname );
    props.setLook( wStepname );
    wStepname.addModifyListener( lsMod );
    fdStepname = new FormData();
    fdStepname.left = new FormAttachment( middle, 0 );
    fdStepname.top = new FormAttachment( 0, margin );
    fdStepname.right = new FormAttachment( 100, 0 );
    wStepname.setLayoutData( fdStepname );

    wTabFolder = new CTabFolder( shell, SWT.BORDER );
    props.setLook( wTabFolder, PropsUI.WIDGET_STYLE_TAB );

    // ////////////////////////
    // START OF GENERAL TAB ///
    // ////////////////////////
    wGeneralTab = new CTabItem( wTabFolder, SWT.NONE );
    wGeneralTab.setText( BaseMessages.getString( PKG, "FileSetPropertiesStep.GeneralTab.Title" ) );

    wGeneralComp = new Composite( wTabFolder, SWT.NONE );
    props.setLook( wGeneralComp );

    FormLayout fileLayout = new FormLayout();
    fileLayout.marginWidth = 3;
    fileLayout.marginHeight = 3;
    wGeneralComp.setLayout( fileLayout );

    // ////////////////////////
    // START Settings GROUP

    Group gSettings = new Group( wGeneralComp, SWT.SHADOW_ETCHED_IN );
    gSettings.setText( BaseMessages.getString( PKG, "FileSetPropertiesStep.SettingsGroup.Label" ) );
    FormLayout SettingsLayout = new FormLayout();
    SettingsLayout.marginWidth = 3;
    SettingsLayout.marginHeight = 3;
    gSettings.setLayout( SettingsLayout );
    props.setLook( gSettings );

    
    Control lastControl = wGeneralComp;
	
	for (Map.Entry<String, Object> entry : meta.fields.entrySet()) {
	    switch(entry.getKey()) {
	    	case "coolEnvironment"  : lastControl = addCoolEnvironmentSelector(entry.getKey(), lastControl, gSettings, middle, margin * 2); break; 	
	    	case "merge"			: lastControl = addCheckboxField(entry.getKey(), lastControl, gSettings, middle, margin * 2); break;
	    	default: lastControl = addStreamFieldSelector(entry.getKey(), lastControl, gSettings, middle, margin * 2); break;
	    	//default: lastControl = addTextVarField(entry.getKey(), lastControl, gSettings, middle, margin * 2); break;
	    }
	}  

    FormData fdSettings = new FormData();
    fdSettings.left = new FormAttachment( 0, 0 );
    fdSettings.right = new FormAttachment( 100, 0 );
    fdSettings.top = new FormAttachment( wStepname, margin );
    gSettings.setLayoutData( fdSettings );

    // END Output Settings GROUP
    // ////////////////////////

    fdGeneralComp = new FormData();
    fdGeneralComp.left = new FormAttachment( 0, 0 );
    fdGeneralComp.top = new FormAttachment( wStepname, margin );
    fdGeneralComp.right = new FormAttachment( 100, 0 );
    fdGeneralComp.bottom = new FormAttachment( 100, 0 );
    wGeneralComp.setLayoutData( fdGeneralComp );

    wGeneralComp.layout();
    wGeneralTab.setControl( wGeneralComp );

    // ///////////////////////////////////////////////////////////
    // / END OF GENERAL TAB
    // ///////////////////////////////////////////////////////////

    // Additional tab...
    //
    wAdditionalTab = new CTabItem( wTabFolder, SWT.NONE );
    wAdditionalTab.setText( BaseMessages.getString( PKG, "FileSetPropertiesStep.PropertiesTab.Title" ) );

    FormLayout addLayout = new FormLayout();
    addLayout.marginWidth = Const.FORM_MARGIN;
    addLayout.marginHeight = Const.FORM_MARGIN;

    wAdditionalComp = new Composite( wTabFolder, SWT.NONE );
    wAdditionalComp.setLayout( addLayout );
    props.setLook( wAdditionalComp );

    Label wlFields = new Label( wAdditionalComp, SWT.NONE );
    wlFields.setText( BaseMessages.getString( PKG, "FileSetProperties.Headers.Label" ) );
    props.setLook( wlFields );
    FormData fdlFields = new FormData();
    fdlFields.left = new FormAttachment( 0, 0 );
    fdlFields.top = new FormAttachment( wStepname, margin );
    wlFields.setLayoutData( fdlFields );

    wGetFromStream = new Button( wAdditionalComp, SWT.PUSH );
    wGetFromStream.setText( BaseMessages.getString( PKG, "FileSetPropertiesStep.GetStreamFields.Button" ) );
    FormData fdGetFromStream = new FormData();
    fdGetFromStream.top = new FormAttachment( wlFields, margin );
    fdGetFromStream.right = new FormAttachment( 100, 0 );
    wGetFromStream.setLayoutData( fdGetFromStream );

    String[] streamVarSelections = new String[]{
    		meta.VAR_SOURCE_STREAM,		
    		meta.VAR_SOURCE_VAR,
    };
    
    propertiesGrid = new Grid();
    
    propertiesGrid.addColumn("propertyNameStreamSel", new ColumnInfo(BaseMessages.getString(PKG, "Cool.streamVarSel.Label"),
			ColumnInfo.COLUMN_TYPE_CCOMBO, streamVarSelections,
			false));
    propertiesGrid.addColumn("propertyNameStream", new ColumnInfo(BaseMessages.getString(PKG, "FileSetProperties.ColumnInfo.propertyNameStream"),
			ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] { "" },
			false));
    propertiesGrid.addColumn("propertyNameVar", new ColumnInfo(BaseMessages.getString(PKG, "FileSetProperties.ColumnInfo.propertyNameVar"),
			ColumnInfo.COLUMN_TYPE_TEXT, false));
    
    propertiesGrid.addColumn("propertyValueStreamSel", new ColumnInfo(BaseMessages.getString(PKG, "Cool.streamVarSel.Label"),
			ColumnInfo.COLUMN_TYPE_CCOMBO, streamVarSelections,
			false));
    propertiesGrid.addColumn("propertyValueStream", new ColumnInfo(BaseMessages.getString(PKG, "FileSetProperties.ColumnInfo.propertyValueStream"),
    		ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] { "" },
    		false));
    propertiesGrid.addColumn("propertyValueVar", new ColumnInfo(BaseMessages.getString(PKG, "FileSetProperties.ColumnInfo.propertyValueVar"),
    		ColumnInfo.COLUMN_TYPE_TEXT, false));
    
    propertiesGrid.getColumnInfo("propertyNameVar").setUsingVariables( true );
    propertiesGrid.getColumnInfo("propertyValueVar").setUsingVariables( true );

    String[] columnsToCheck = new String[]{"propertyNameStream","propertyNameVar","propertyValueStream","propertyValueVar"};
    
    for (final String columnName : columnsToCheck){
    	propertiesGrid.getColumnInfo(columnName).setDisabledListener(new FieldDisabledListener() {
	    	public boolean isFieldDisabled(int rowNr) {
	    		return isCellReadOnly(rowNr, columnName);
	    	}
	    });
    }
	
    propertiesGrid.initTableView(transMeta, wAdditionalComp, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, 0, lsMod, props );
    
    FormData fdFields = new FormData();
    fdFields.left = new FormAttachment( 0,0 );
    fdFields.top = new FormAttachment( wlFields, margin );
    fdFields.right = new FormAttachment( wGetFromStream, -margin );
    fdFields.bottom = new FormAttachment( 100, -margin );
    propertiesGrid.getTable().setLayoutData( fdFields );

    fdAdditionalComp = new FormData();
    fdAdditionalComp.left = new FormAttachment( 0, 0 );
    fdAdditionalComp.top = new FormAttachment( wStepname, margin );
    fdAdditionalComp.right = new FormAttachment( 100, -margin );
    fdAdditionalComp.bottom = new FormAttachment( 100, 0 );
    wAdditionalComp.setLayoutData( fdAdditionalComp );

    wAdditionalComp.layout();
    wAdditionalTab.setControl( wAdditionalComp );
    // ////// END of Additional Tab

    

    fdTabFolder = new FormData();
    fdTabFolder.left = new FormAttachment( 0, 0 );
    fdTabFolder.top = new FormAttachment( wStepname, margin );
    fdTabFolder.right = new FormAttachment( 100, 0 );
    fdTabFolder.bottom = new FormAttachment( 100, -50 );
    wTabFolder.setLayoutData( fdTabFolder );

    //
    // Search the fields in the background
    //

    final Runnable runnable = new Runnable() {
      public void run() {
        StepMeta stepMeta = transMeta.findStep( stepname );
        if ( stepMeta != null ) {
          try {
            RowMetaInterface row = transMeta.getPrevStepFields( stepMeta );

            // Remember these fields...
            for ( int i = 0; i < row.size(); i++ ) {
            	System.out.println(row.getValueMeta( i ).getName());
              inputFields.put( row.getValueMeta( i ).getName(), Integer.valueOf( i ) );
            }

            setComboBoxes();
          } catch ( KettleException e ) {
            log.logError( toString(), BaseMessages.getString( PKG, "System.Dialog.GetFieldsFailed.Message" ) );
          }
        }
      }
    };
    new Thread( runnable ).start();

    // THE BUTTONS
    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

    setButtonPositions( new Button[] { wOK, wCancel }, margin, wTabFolder );

    // Add listeners
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    
    wCancel.addListener(SWT.Selection, lsCancel);
	wOK.addListener(SWT.Selection, lsOK);
       
    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    lsResize = new Listener() {
      public void handleEvent( Event event ) {
        Point size = shell.getSize();
        propertiesGrid.getTable().setSize( size.x - 10, size.y - 50 );
        propertiesGrid.getTable().setSize( size.x - 10, size.y - 50 );
        propertiesGrid.getTable().redraw();
      }
    };
    shell.addListener( SWT.Resize, lsResize );

    // Set the shell size, based upon previous time...
    setSize();
    wTabFolder.setSelection( 0 );
    
    populateDialog();
    
    meta.setChanged( changed );

    shell.open();
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return stepname;
  }

  /**
   * helper that determines wether a cell is read only or not
   *
   * @param rowId
   * @param columnId
   * @return
   */
  private boolean isCellReadOnly(int rowId, String columnName) {
	  
	  String isStream = meta.VAR_SOURCE_STREAM;
	  String isTextVar = meta.VAR_SOURCE_VAR;
	  
	  String controlFieldName = "";
	  
	  TableItem item = propertiesGrid.getTable().table.getItem( rowId );
	  
	  switch(columnName) {
	    case "propertyNameStream" 	:  case "propertyNameVar" 	: controlFieldName = "propertyNameStreamSel"; break;
	    case "propertyValueStream" 	:  case "propertyValueVar" 	: controlFieldName = "propertyValueStreamSel"; break;
	  }
	  
	  String valueToCheck = item.getText( propertiesGrid.getColumnIndex(controlFieldName)+1 )+"";
	  
	  switch(columnName) {
	    case "propertyNameStream" 	:  case "propertyValueStream" 	: return valueToCheck.equals("") || valueToCheck.equals(isTextVar);
	    case "propertyNameVar" 		:  case "propertyValueVar" 		: return valueToCheck.equals("") || valueToCheck.equals(isStream);
	  }
	  
	  return false;
	  
  }

  protected void setComboBoxes() {
    // Something was changed in the row.
    final Map<String, Integer> fields = new HashMap<String, Integer>();

    // Add the currentMeta fields...
    fields.putAll( inputFields );

    Set<String> keySet = fields.keySet();
    List<String> entries = new ArrayList<String>( keySet );

    fieldNames = entries.toArray( new String[entries.size()] );

    Const.sortStrings( fieldNames );
    propertiesGrid.getColumnInfo("propertyNameStream").setComboValues( fieldNames );
    propertiesGrid.getColumnInfo("propertyValueStream").setComboValues( fieldNames );
  }

  public void populateDialog() {
	  super.populateDialog();
	  propertiesGrid.setData(meta.properties);
  }
  
  protected void populateMeta() {
	  super.populateMeta();
	  meta.properties = propertiesGrid.getData();
  }  
	
}
