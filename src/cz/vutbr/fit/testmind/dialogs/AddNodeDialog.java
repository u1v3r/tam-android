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

public class AddNodeDialog extends DialogFragment implements OnEditorActionListener{
	
	public interface AddNodeDialogListener{
		void onFinishNodeAddDialog(String title);
	}
	
	private EditText titleEditText;
	private Button cancelButton;
	private Button okButton;
	private AddNodeDialogListener activity;
	
	private View.OnClickListener buttonsHandler = new View.OnClickListener() {
		
		public void onClick(View v) {			
			if(v == okButton){
				activity.onFinishNodeAddDialog(titleEditText.getText().toString());	
				dismiss();
			}else if(v == cancelButton){
				dismiss();
			}
		}
	};
		
	public AddNodeDialog() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
			View view = inflater.inflate(R.layout.fragment_add_node, container);
			getDialog().setTitle(R.string.node_add_dialog);
			
			activity = (AddNodeDialogListener) getActivity();
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
            activity.onFinishNodeAddDialog(titleEditText.getText().toString());
            dismiss();
            return true;
        }
        return false;
	}
}
