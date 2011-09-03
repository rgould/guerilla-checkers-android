package com.CardboardGames;

import android.app.Activity;
import android.os.Bundle;

public class GuerillaCheckersActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BoardView bv = new BoardView(this);
        setContentView(bv);
    }
}