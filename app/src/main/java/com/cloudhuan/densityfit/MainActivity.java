package com.cloudhuan.densityfit;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_apply,btn_reset;
    EditText et_density;
    private String saveFileName = "DenistyValue";
    private String saveKey = "denisty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRoot();
        btn_apply = this.findViewById(R.id.btn_apply);btn_apply.setOnClickListener(this);
        btn_reset = this.findViewById(R.id.btn_reset);btn_reset.setOnClickListener(this);
        et_density = this.findViewById(R.id.et_density);
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_density.setText(getData());
        et_density.setSelection(et_density.getText().length());
    }

    private void requestRoot(){
        execShell("su");
    }

    private void apply(String i){
        execShellWithSu("wm density "+i);
    }

    private void reset(){
        execShellWithSu("wm density reset");
    }

    private void execShell(String string){
        try {
            Log.i("cz","exec:"+string);
            Runtime.getRuntime().exec(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execShellWithSu(String string){
        try {
            Log.i("cz","exec:"+string);
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes(string+"\n");
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean saveData(String value){
        SharedPreferences.Editor editor = this.getSharedPreferences(saveFileName,MODE_PRIVATE).edit();
        editor.putString("denisty",value);
        return editor.commit();
    }

    private String getData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(saveFileName,MODE_PRIVATE);
        return sharedPreferences.getString(saveKey,"480");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_apply:
                String d = et_density.getText().toString();
                apply(d);
                saveData(d);
                break;
            case R.id.btn_reset:
                reset();
                break;
        }
    }
}
