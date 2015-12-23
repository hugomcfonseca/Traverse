package feup.resilience;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;



/**
 * Created by Filipe on 16/12/2015.
 */
public class QuestionaryFragment extends Fragment {
    Communicator comm;
    public int flag;
    private static final int NUM_COLS = 2 ;
    private static final int NUM_ROWS = 2;
    Button buttons[][] =new Button[NUM_ROWS][NUM_COLS];
    int questionResult[]=new int[NUM_COLS*NUM_ROWS];



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        SignOn activity = (SignOn)getActivity();
        flag = activity.getMyFlag();
        if(flag==0)
            view= inflater.inflate(R.layout.fragment_question_init,container,false);
        else if (flag==3)
            view= inflater.inflate(R.layout.fragment_question_finish,container,false);
        else
            view= inflater.inflate(R.layout.fragment_question,container,false);
        populateButtons(view, flag * 4);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        Button next =(Button)getActivity().findViewById(R.id.btn_next_questionary);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionResult[0]==0)
                    Toast.makeText(getActivity(),"Select one Image",Toast.LENGTH_SHORT).show();
                else
                     comm.respond(flag + 1, questionResult[0]);
            }
        });
    }

    private void populateButtons(View view, int i) {
        TableLayout table = (TableLayout)view.findViewById(R.id.QuestionTable);
        int flag=i;
        for (int row=0;row<NUM_ROWS;row++){
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);
            for (int col=0;col<NUM_COLS;col++){
                final int colfinal = col;
                final int rowfinal = row;
                final Button button = new Button(getActivity());

                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));
                tableRow.addView(button);
                buttons[row][col]=button;
                putImage(row,col,flag);
                flag++;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Focus(rowfinal, colfinal);
                    }
                });
            }
        }
    }

    private void Focus(int rowfinal, int colfinal) {
        for (int row=0;row<NUM_ROWS;row++){
            for (int col=0;col<NUM_COLS;col++){
                if(col==colfinal && row==rowfinal ) {
                    buttons[row][col].getBackground().setAlpha(255);
                    questionResult[0]=col+1+NUM_COLS*row;
                }
                else
                    buttons[row][col].getBackground().setAlpha(80);

            }
        }
    }

    private void putImage(int row, int col, int i) {
        Button button = buttons[row][col];
        int newWidth=button.getWidth();
        int newHeight=button.getHeight();
        Bitmap originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.separator);
        if (i==0){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.porto);
        }else if (i==1){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.psg);
        }else if (i==2){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ch);
        }else if (i==3){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.real);
        }else if (i==4){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.real);
        }else if (i==5){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.real);
        }else if (i==6){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.real);
        }else if (i==7){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.real);
        }else if (i==8){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.porto);
        }else if (i==9){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.porto);
        }else if (i==10){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.porto);
        }else if (i==11){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.porto);
        }else if (i==12){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.psg);
        }else if (i==13){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.psg);
        }else if (i==14){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.psg);
        }else if (i==15){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.psg);
        }
        Bitmap scaleBitmap =Bitmap.createScaledBitmap(originalBitmap,100,100,true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaleBitmap));

    }

}
