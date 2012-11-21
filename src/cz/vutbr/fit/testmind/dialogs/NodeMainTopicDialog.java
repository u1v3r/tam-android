package cz.vutbr.fit.testmind.dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.controls.TAMEAbstractControl;
import cz.vutbr.fit.testmind.editor.items.TAMENode;

public class NodeMainTopicDialog extends DialogFragment implements OnEditorActionListener{
	
	public interface OnMainTopicSetDialogListener{
		void onFinishNodeEditDialog(String title);
	}
		
	private EditText titleEditText;
	private Button cancelButton;
	private Button okButton;
	
	private OnMainTopicSetDialogListener control;
	
	private View.OnClickListener buttonsHandler = new View.OnClickListener() {
		
		public void onClick(View v) {			
			if(v == okButton){
				
				if(titleEditText.getText().length() == 0) return;				
				control.onFinishNodeEditDialog(titleEditText.getText().toString());	
				dismiss();
				
			}else if(v == cancelButton){
				control.onFinishNodeEditDialog(
						v.getResources().getString(R.string.node_main_topic_dialog_default_topic));
				dismiss();
			}
		}
	};
			
	public NodeMainTopicDialog(TAMEAbstractControl control) {
		this.control = (OnMainTopicSetDialogListener) control;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
			View view = inflater.inflate(R.layout.fragment_add_node, container);
			getDialog().setTitle(R.string.node_main_topic_dialog);
			
			//activity = (AddNodeDialogListener) getActivity();
			titleEditText = (EditText)view.findViewById(R.id.edit_text_node_title);
			cancelButton = (Button)view.findViewById(R.id.add_dialog_button_cancel);
			okButton = (Button)view.findViewById(R.id.add_dialog_button_ok);
			
			
			cancelButton.setOnClickListener(buttonsHandler);
			okButton.setOnClickListener(buttonsHandler);
			
			// zobrazi soft klavesnicu a po kliknuti na done ulozi
			titleEditText.requestFocus();
	        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	        titleEditText.setOnEditorActionListener(this);
			
			return view;
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
            // vrati vyplneny text do activity            
            control.onFinishNodeEditDialog(titleEditText.getText().toString());
            dismiss();
            return true;
        }
        return false;
	}
}
