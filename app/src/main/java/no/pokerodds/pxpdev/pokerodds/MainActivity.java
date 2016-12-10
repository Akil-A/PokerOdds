package no.pokerodds.pxpdev.pokerodds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String STATE_SELECTED = "stateSelected";

    private static final int REQUEST_CARD = 1000;

    @IdRes
    private static final int[] CARD_VIEWS = new int[]{
            R.id.choice1, R.id.choice2, R.id.choice3, R.id.choice4
    };

    private ArrayList<Card> selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            selected = new ArrayList<>(Card.Suit.MAX);
            for (int i = 0; i < Card.Suit.MAX; i++) {
                selected.add(Card.FACE_DOWN);
            }
        } else {
            selected = savedInstanceState.getParcelableArrayList(STATE_SELECTED);
        }

        setContentView(R.layout.activity_main);

        for (int i = 0; i < CARD_VIEWS.length; i++) {
            setupCardView(CARD_VIEWS[i], selected.get(i));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_SELECTED, selected);
    }

    void setupCardView(@IdRes int id, Card card) {
        ImageView imageView = (ImageView) findViewById(id);
        imageView.setOnClickListener(cardClickListener);
        imageView.setImageResource(card.getDrawableResId(this));
    }

    private View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int index = -1;
            for (int i = 0; i < CARD_VIEWS.length; i++) {
                if (CARD_VIEWS[i] == view.getId()) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                Intent intent = SuitActivity.createIntent(MainActivity.this, selected, index);
                startActivityForResult(intent, REQUEST_CARD);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CARD) {
            if (resultCode == RESULT_OK) {
                int index = SuitActivity.extractIndex(data);
                Card card = SuitActivity.extractCard(data);
                if (index != -1) {
                    selected.set(index, card);
                    setupCardView(CARD_VIEWS[index], card);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
