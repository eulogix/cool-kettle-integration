package com.eulogix.cool.lib.pentaho;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import com.eulogix.cool.lib.KettleAppConfiguration;
import com.eulogix.kettle.lib.EasyStepDialog;
import com.eulogix.kettle.lib.EasyStepMeta;

public abstract class CoolStepDialog extends EasyStepDialog {
		
	private CoolStepMeta meta;
	
	protected Environment coolEnvironment;

	public CoolStepDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		meta = (CoolStepMeta) in;
	}
	
	protected CCombo addCoolEnvironmentSelector(String name, Control lastControl) {
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;
		return addCoolEnvironmentSelector(name, lastControl, shell, middle, margin);
	}
	
	protected CCombo addCoolEnvironmentSelector(String name, Control lastControl, Composite composite, int middle, int margin) {
		addLabel(messagesPrefix + ".Field." + name + ".Label", composite, middle, margin, lastControl);
		
		CCombo field = new CCombo( composite, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER );

	    ArrayList<KettleAppConfiguration> confs = this.getCoolEnvironment().getConfiguredCoolApps();
		for(int i=0; i<confs.size(); i++) {
			field.add( confs.get(i).getName() );
		}
	    
	    field.select( 0 ); // +1: starts at -1
	    props.setLook( field );
	    FormData fdField = new FormData();
	    fdField.left = new FormAttachment( middle, 0 );
	    fdField.top = new FormAttachment( lastControl, margin );
	    fdField.right = new FormAttachment( 100, 0 );
	    field.setLayoutData( fdField );
	    field.addSelectionListener( new SelectionAdapter() {
	      public void widgetSelected( SelectionEvent e ) {
	    	  System.out.println("cool env changed");
	      }
	    } );
	    
	    addControl(name, field, "CCOMBO");
	    return field;
	}
		
	protected Environment getCoolEnvironment() {
		if(coolEnvironment == null)
			coolEnvironment = new Environment(transMeta);
		return coolEnvironment;
	}
	
}
