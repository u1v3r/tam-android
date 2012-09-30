package com.example.testmind;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void buttonClicked(View view){
    	int age = getAge();
    	
    	if(age < 0){
    		informAboutInvalidAge();
    	}else{
    		startAgeActivity(age);
    	}
    	
    }

	protected int getAge() {
		
		EditText ageInput = (EditText) findViewById(R.id.edittext);
		String ageString = ageInput.getText().toString();
		int age = -1;
		
		try{
			age = Integer.parseInt(ageString);
		} catch(NumberFormatException e){
			
		}
		
		return age;
	}
	
	protected void informAboutInvalidAge(){
	    Toast.makeText(this, R.string.invalid_age, Toast.LENGTH_SHORT).show();
	    EditText ageInput = ((EditText)findViewById(R.id.edittext));
	    ageInput.setText("");
	    ageInput.requestFocus();
	}
	
	protected void startAgeActivity(int age){
	    Intent intent = new Intent(this, AgeDisplayerActivity.class);
	    intent.putExtra(AgeDisplayerActivity.AGE, age);
	    startActivity(intent);
	}
}
