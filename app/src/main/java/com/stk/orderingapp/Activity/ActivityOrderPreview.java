package com.stk.orderingapp.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stk.orderingapp.Model.ModelProductList;
import com.stk.orderingapp.R;

import java.util.List;

public class ActivityOrderPreview extends AppCompatActivity {
    RecyclerView recyclerViewPreviewList = null;

    TextView tvHeaderText = null, tvSubHeaderText = null;
    Context context = null;
    private LinearLayoutManager linearLayoutManager = null;
    Button btnOrderPrev = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initComp();
        initCompListener();

    }

    private void initComp() {
        tvHeaderText = (TextView) findViewById(R.id.tvHeaderText);
        tvSubHeaderText = (TextView) findViewById(R.id.tvSubHeaderText);


        btnOrderPrev = (Button) findViewById(R.id.btnOrderPrev);
        btnOrderPrev.setVisibility(View.VISIBLE);
        btnOrderPrev.setText(R.string.submitOrder);

    }

    private void initCompListener() {

    }
}
