package com.example.rule.ui.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.example.rule.ChartActivity;
import com.example.rule.HistoryEvent;
import com.example.rule.HistoryRequest;
import com.example.rule.HistoryTask;
import com.example.rule.KoqHistory;
import com.example.rule.R;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportFragment extends Fragment implements RecyclerViewInterface {

    ArrayList<historyModel> historyArrayList= new ArrayList<>();
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    Button btnChart;
    String userid;
    Button btnCount;
    ProgressDialog progressDialog;
    TextView tvNopt, tvPoint, tvPointBUB, tvPointKP, tvPointSP, tvGred, tvEvent;
    private RecyclerView hRecyclerView;
    AnyChartView anyChart;
    PieChart pieChart;
//    BarChart barChart;
    public static final String TAG = "TAG";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_report, container, false);
        fStore =FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        tvNopt= root.findViewById(R.id.tv_nopt);
        hRecyclerView = root.findViewById(R.id.hRecyclerView);
        hRecyclerView.setHasFixedSize(true);
        hRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        hAdapter hAdapter= new hAdapter(root.getContext(), historyArrayList, this::onItemClick);
        hRecyclerView.setAdapter(hAdapter);
        tvPoint=root.findViewById(R.id.tv_point);
        tvPointBUB=root.findViewById(R.id.tv_PointBUB);
        tvPointKP=root.findViewById(R.id.tv_PointKP);
        tvPointSP=root.findViewById(R.id.tv_PointSP);
        tvGred=root.findViewById(R.id.tv_gred);
        btnChart=root.findViewById(R.id.btn_chart);
        tvEvent=root.findViewById(R.id.tv_PointKoQ);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp1=tvPointBUB.getText().toString();
                String temp2=tvPointKP.getText().toString();
                String temp3=tvPointSP.getText().toString();
                String temp4=tvEvent.getText().toString();
                String temp5=tvPoint.getText().toString();

                Intent intent=new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("BUB",temp1);
                intent.putExtra("KP",temp2);
                intent.putExtra("SP",temp3);
                intent.putExtra("EVENT",temp4);
                intent.putExtra("TOTAL",temp5);

                startActivity(intent);
            }
        });
        fetchHistory();

        return root;
    }

    private void fetchHistory(){
        userid= mAuth.getCurrentUser().getUid();
        fStore.collection("fsHistory").whereEqualTo("studentID",userid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error", error.getMessage() );
                        }
                        if(value.isEmpty()||value==null){
                            hRecyclerView.setVisibility(View.GONE);
                            tvNopt.setVisibility(View.VISIBLE);
                        } else {
                            hRecyclerView.setVisibility(View.VISIBLE);
                            tvNopt.setVisibility(View.GONE);
                            double total=0;
                            double BUB=0;
                            double KP=0;
                            double SP=0;
                            double koQ=0;
                            for(DocumentChange dc: value.getDocumentChanges()){
                                if (dc.getType()==DocumentChange.Type.ADDED){
                                    historyArrayList.add(dc.getDocument().toObject(historyModel.class));
                                    if(dc.getDocument().getString("totalPoint")!=null){
                                        if(dc.getDocument().getString("result").equals("Approved")){
                                            String comp= dc.getDocument().getString("component");
                                            if("Badan dan Unit Beruniform".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                    BUB+=itemPoint;
                                            }else if("Kelab dan Persatuan".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                    KP+=itemPoint;
                                            }else if("Sukan dan Permainan".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                    SP+=itemPoint;
                                            }
                                            if(dc.getDocument().getString("type").equals("event")){
                                                koQ+=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                            }
                                        }
                                    }
                                }
                                if (dc.getType()==DocumentChange.Type.MODIFIED){
                                    historyArrayList.add(dc.getDocument().toObject(historyModel.class));
                                    if(dc.getDocument().getString("totalPoint")!=null){
                                        if(dc.getDocument().getString("result").equals("Approved")){
                                            String comp= dc.getDocument().getString("component");
                                            if("Badan dan Unit Beruniform".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                BUB+=itemPoint;
                                            }else if("Kelab dan Persatuan".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                KP+=itemPoint;
                                            }else if("Sukan dan Permainan".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                SP+=itemPoint;
                                            }
                                            if(dc.getDocument().getString("type").equals("event")){
                                                koQ+=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                            }
                                        }
                                    }
                                }
                                if (dc.getType()==DocumentChange.Type.REMOVED){
                                    historyArrayList.add(dc.getDocument().toObject(historyModel.class));
                                    if(dc.getDocument().getString("totalPoint")!=null){
                                        if(dc.getDocument().getString("result").equals("Approved")){
                                            String comp= dc.getDocument().getString("component");
                                            if("Badan dan Unit Beruniform".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                BUB+=itemPoint;
                                            }else if("Kelab dan Persatuan".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                KP+=itemPoint;
                                            }else if("Sukan dan Permainan".equals(comp)){
                                                double itemPoint=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                                SP+=itemPoint;
                                            }
                                            if(dc.getDocument().getString("type").equals("event")){
                                                koQ+=Double.parseDouble(dc.getDocument().getString("totalPoint"));
                                            }
                                        }
                                    }
                                }
                                hRecyclerView.getAdapter().notifyDataSetChanged();
                                tvPointBUB.setText(String.valueOf(BUB));
                                tvPointKP.setText(String.valueOf(KP));
                                tvPointSP.setText(String.valueOf(SP));
                                tvEvent.setText(String.valueOf(koQ));
                                double []max=new double[3];
                                max[0]=BUB;
                                max[1]=KP;
                                max[2]=SP;
                                double max1=Double.MIN_VALUE;
                                double max2=Double.MIN_VALUE;
                                for(double maxnum: max){
                                    if(maxnum>max1){
                                        max2=max1;
                                        max1=maxnum;
                                    }else if(maxnum>max2){
                                        max2=maxnum;
                                    }
                                }
                                double finalPoint=(max1+max2)/2;
                                finalPoint+=koQ;
                                double gred=0;
                                gred=finalPoint/10;
                                double gredPercent=0;
                                gredPercent=(finalPoint/110)*100;
                                DecimalFormat df= new DecimalFormat("#.##");
                                tvPoint.setText(String.valueOf(df.format(gred)));
                                if(gredPercent>=80){
                                    tvGred.setText("A");
                                } else if(gredPercent<80&&gredPercent>=60){
                                    tvGred.setText("B");
                                }else if(gredPercent<60&&gredPercent>=40){
                                    tvGred.setText("C");
                                }else if(gredPercent<40&&gredPercent>=20){
                                    tvGred.setText("D");
                                }else if(gredPercent<20){
                                    tvGred.setText("E");
                                }
                                fStore.collection("users").document(userid).update("maxPoint",total).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Task has been uploaded successfully by "+userid);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+ e.toString());

                                    }
                                });
                            }


                        }

                    }
                });

    }


    @Override
    public void onItemClick(int position) {
        String temp= historyArrayList.get(position).getType();
        switch (temp){
            case "event":
                Intent intent= new Intent(getActivity(), HistoryEvent.class);

                intent.putExtra("TASK", historyArrayList.get(position).getTask());
                intent.putExtra("STUDENT",historyArrayList.get(position).getStudentID());
                intent.putExtra("DOTG",historyArrayList.get(position).getDateOfTaskGiven());
                intent.putExtra("POT",historyArrayList.get(position).getPlace());
                intent.putExtra("DOTU",historyArrayList.get(position).getDateOfTaskUploaded());
                intent.putExtra("DOCID",historyArrayList.get(position).getDocID());
                intent.putExtra("PICUID",historyArrayList.get(position).getPICuid());
                intent.putExtra("TASKDETAIL",historyArrayList.get(position).getTaskDetail());
                intent.putExtra("TYPE", historyArrayList.get(position).getType());
                intent.putExtra("RESULT", historyArrayList.get(position).getResult());
                intent.putExtra("EVENTID", historyArrayList.get(position).getEventID());
                intent.putExtra("TOTALPOINT", historyArrayList.get(position).getTotalPoint());
                startActivity(intent);
                break;

            case "task":
                Intent intent2= new Intent(getActivity(), HistoryTask.class);

                intent2.putExtra("TASK", historyArrayList.get(position).getTask());
                intent2.putExtra("STUDENT",historyArrayList.get(position).getStudentID());
                intent2.putExtra("DOTG",historyArrayList.get(position).getDateOfTaskGiven());
                intent2.putExtra("POT",historyArrayList.get(position).getPlace());
                intent2.putExtra("DOTU",historyArrayList.get(position).getDateOfTaskUploaded());
                intent2.putExtra("DOCID",historyArrayList.get(position).getDocID());
                intent2.putExtra("PICUID",historyArrayList.get(position).getPICuid());
                intent2.putExtra("TASKDETAIL",historyArrayList.get(position).getTaskDetail());
                intent2.putExtra("TYPE", historyArrayList.get(position).getType());
                intent2.putExtra("RESULT", historyArrayList.get(position).getResult());
                startActivity(intent2);
                break;

            case "request":
                Intent intent3= new Intent(getActivity(), HistoryRequest.class);

                intent3.putExtra("TASK", historyArrayList.get(position).getTask());
                intent3.putExtra("STUDENT",historyArrayList.get(position).getStudentID());
                intent3.putExtra("DOTG",historyArrayList.get(position).getDateOfTaskGiven());
                intent3.putExtra("POT",historyArrayList.get(position).getPlace());
//                intent3.putExtra("DOTU",paArrayList.get(position).getDateOfTaskUploaded());
                intent3.putExtra("DOCID",historyArrayList.get(position).getDocID());
                intent3.putExtra("PICUID",historyArrayList.get(position).getPICuid());
                intent3.putExtra("TASKDETAIL",historyArrayList.get(position).getTaskDetail());
                intent3.putExtra("TYPE", historyArrayList.get(position).getType());
                intent3.putExtra("RESULT", historyArrayList.get(position).getResult());
                startActivity(intent3);
                break;

            case "KoQ":
                Intent intent4= new Intent(getActivity(), KoqHistory.class);

                intent4.putExtra("TASK", historyArrayList.get(position).getTask());
                intent4.putExtra("JAWATAN", historyArrayList.get(position).getJawatan());
                intent4.putExtra("KEHADIRAN", historyArrayList.get(position).getKehadiran());
                intent4.putExtra("KHIDMATSUMBANGAN", historyArrayList.get(position).getKhidmatSumbangan());
                intent4.putExtra("KOMITMEN", historyArrayList.get(position).getKomitmen());
                intent4.putExtra("PICUID", historyArrayList.get(position).getPICuid());
                intent4.putExtra("PENCAPAIAN", historyArrayList.get(position).getPencapaian());
                intent4.putExtra("PENGLIBATAN", historyArrayList.get(position).getPenglibatan());
                intent4.putExtra("DOCID", historyArrayList.get(position).getDocID());
                intent4.putExtra("RESULT", historyArrayList.get(position).getResult());
                intent4.putExtra("STUDENTID", historyArrayList.get(position).getStudentID());
                intent4.putExtra("TYPE", historyArrayList.get(position).getType());
                intent4.putExtra("TOTALPOINT", historyArrayList.get(position).getTotalPoint());
                intent4.putExtra("COMPONENT", historyArrayList.get(position).getComponent());
                startActivity(intent4);

                break;
        }

    }
}