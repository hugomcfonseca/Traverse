package feup.resilience;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * Created by Filipe on 16/12/2015.
 */
public class QuestionaryFragment extends Fragment {


    private static final int NUM_COLS = 2 ;
    private static final int NUM_ROWS = 2;
    Button buttons[][] =new Button[NUM_ROWS][NUM_COLS];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view= inflater.inflate(R.layout.fragmentquestionary,container,false);
        View view= inflater.inflate(R.layout.teste,container,false);
        populateButtons(view);


        return view;
    }

    private void populateButtons(View view) {
        TableLayout table = (TableLayout)view.findViewById(R.id.QuestionTable);
        int flag=1;
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
                if(col==colfinal && row==rowfinal )
                    buttons[row][col].getBackground().setAlpha(255);
                else
                    buttons[row][col].getBackground().setAlpha(80);

            }
        }
    }

    private void putImage(int row, int col, int i) {
        Button button = buttons[row][col];
        int newWidth=button.getWidth();
        System.out.println(newWidth);
        int newHeight=button.getHeight();
        System.out.println(newHeight);
        Bitmap originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.separator);
        if (i==1){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.porto);
        }else if(i==2){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.psg);
        }else if(i==3){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ch);

        }else if (i==4){
            originalBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.real);
        }
        Bitmap scaleBitmap =Bitmap.createScaledBitmap(originalBitmap,100,100,true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource,scaleBitmap));

    }


    private void gridButtonClicked() {
        Toast.makeText(getActivity(),"foi",Toast.LENGTH_SHORT).show();

    }
}
