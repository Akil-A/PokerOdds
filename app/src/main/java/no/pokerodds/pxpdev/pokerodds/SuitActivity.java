package no.pokerodds.pxpdev.pokerodds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SuitActivity extends AppCompatActivity implements CardChooserFragment.Callback {

    private static final String EXTRA_INDEX = "extraIndex";
    private static final String EXTRA_CARD = "extraCard";
    private static final String EXTRA_SELECTED = "extraSelected";

    private ArrayList<Card> selected;


    public static Intent createIntent(Context context, ArrayList<Card> selected, int index)
    {
        return new Intent(context, SuitActivity.class)
                .putExtra(EXTRA_SELECTED, selected)
                .putExtra(EXTRA_INDEX, index);
    }

    public static int extractIndex(Intent data)
    {
        return data.getIntExtra(EXTRA_INDEX, -1);
    }

    public static Card extractCard(Intent data)
    {
        return data.getParcelableExtra(EXTRA_CARD);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suit);

        selected = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
              return CardChooserFragment.newInstance(Card.Suit.values()[position]);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(Card.Suit.values()[position].getNameStringId());
            }

            @Override
            public int getCount() {
                return Card.Suit.MAX;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setTitle(pager.getAdapter().getPageTitle(position));
            }
        });

        Card card = selected.get(extractIndex(getIntent()));
        if(card.getSuit() != null){
            pager.setCurrentItem(card.getSuit().ordinal());
        }
        setTitle(pager.getAdapter().getPageTitle(pager.getCurrentItem()));
    }

    @Override
    public void onCardClicked(Card card) {
        setResult(RESULT_OK, getIntent().putExtra(EXTRA_CARD, card));
        finish();
    }

    @Override
    public List<Card> getSelected() {
        return selected;
    }
}
