package com.ungureanu.inshape;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.List;

public class BMIFragment extends Fragment {

    private final String TAG = BMIFragment.class.getSimpleName();
    private View view;
    private Context context;

    private TextView result;
    private EditText heightEditText;
    private EditText weightEditText;
    private Button buttonResult;
    private MaterialButton buttonMetric;
    private MaterialButton buttonImperial;
    private MaterialButtonToggleGroup materialButtonToggleGroup;

    private final int ENABLED = 1;
    private final int DISABLED = 0;
    private int setOnce = this.DISABLED;

    private final double DEFAULT = -1;
    private double heigh = this.DEFAULT;
    private double weigh = this.DEFAULT;
    private double resultBMI = this.DEFAULT;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bmi, container, false);
    }


    private void setContext() {
        //Log.d(TAG, "setContext: called");
        this.context = getContext();
    }

    private void setView() {
        //Log.d(TAG, "setView: called");
        this.view = getView();
        if (view != null) {
            this.setOnce = ENABLED;
        }
    }

    private void setResultGUI(View view) {
        //Log.d(TAG, "setResult: called");
        if (this.setOnce == ENABLED) {
            this.result = (TextView) view.findViewById(R.id.idResult);
        }
    }

    private void setHeightEditTextGUI(View view) {
        //Log.d(TAG, "setResult: called");
        if (this.setOnce == ENABLED) {
            this.heightEditText = (EditText) view.findViewById(R.id.idEditHeight);
        }
    }

    private void setWeightEditTextGUI(View view) {
        //Log.d(TAG, "setResult: called");
        if (this.setOnce == ENABLED) {
            this.weightEditText = (EditText) view.findViewById(R.id.idEditWeight);
        }
    }

    private void setButtonResultGUI(View view) {
        //Log.d(TAG, "setResult: called");
        if (this.setOnce == ENABLED) {
            this.buttonResult = (Button) view.findViewById(R.id.button);
        }
    }

    private void setMaterialButtonToggleGroupGUI(View view) {
        //Log.d(TAG, "setResult: called");
        if (this.setOnce == ENABLED) {
            this.materialButtonToggleGroup = (MaterialButtonToggleGroup) view.findViewById(R.id.idMaterialButtonToggleGroup);
            this.materialButtonToggleGroup.check(R.id.idMetricButton);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setButtonResultListener() {
        //Log.d(TAG, "setButtonResultListener: called");
        this.buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heightEditText.getText().toString().trim().length() > 0
                        && weightEditText.getText().toString().trim().length() > 0) {
                weigh = Double.parseDouble(weightEditText.getText().toString());
                heigh = Double.parseDouble(heightEditText.getText().toString());
                if (heigh != DEFAULT && weigh != DEFAULT) {

                        int caseID = materialButtonToggleGroup.getCheckedButtonId();
                        switch (caseID) {
                            case R.id.idMetricButton:
                                resultBMI = weigh / (heigh * heigh);
                                break;
                            case R.id.idImperialButton:
                                resultBMI = weigh / (heigh * heigh) * 703;
                        }
                        result.setText(String.valueOf(resultBMI));
                    }
                } else {
                    Toast.makeText(context, "Complete both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setMaterialButtonToggleGroupListener() {
        //Log.d(TAG, "setMaterialButtonToggleGroupListener: called");
        this.materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.idMetricButton) {
                        heightEditText.setHint("Please insert your height in meters");
                        weightEditText.setHint("Please insert your height in kilos");
                        //Toast.makeText(context, "METRIC", Toast.LENGTH_SHORT).show();
                    }
                    if (checkedId == R.id.idImperialButton) {
                        heightEditText.setHint("Please insert your height in feet");
                        weightEditText.setHint("Please insert your height in lbs");
                        //Toast.makeText(context, "IMPERIAL", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        this.materialButtonToggleGroup.check(R.id.idImperialButton);
        this.materialButtonToggleGroup.invalidate();
    }


    @Override
    public void onStart() {
        //Log.d(TAG, "onStart: called");
        super.onStart();

        setContext();
        setView();
        setResultGUI(this.view);
        setHeightEditTextGUI(this.view);
        setWeightEditTextGUI(this.view);
        setButtonResultGUI(this.view);
        setMaterialButtonToggleGroupGUI(this.view);
        setMaterialButtonToggleGroupListener();

        setButtonResultListener();
    }
}
