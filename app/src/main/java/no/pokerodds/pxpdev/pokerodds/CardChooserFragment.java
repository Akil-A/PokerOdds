package no.pokerodds.pxpdev.pokerodds;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class CardChooserFragment extends Fragment {

    private static final String ARG_SUIT = "argSuit";

    private Card.Suit argSuit;

    @Nullable
    private Callback callback;


    public static CardChooserFragment newInstance(Card.Suit suit)
    {
        CardChooserFragment frag = new CardChooserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SUIT, suit);
        frag.setArguments(args);
        return frag;
    }

    public CardChooserFragment() {
        // Requires blank constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Callback){
            callback = (Callback)context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        argSuit = (Card.Suit) getArguments().getSerializable(ARG_SUIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_chooser, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.card_grid);
        recyclerView.setAdapter(new CardAdapter(getActivity(), argSuit));
    }

    private class CardAdapter extends RecyclerView.Adapter<CardViewHolder> implements View.OnClickListener {

        private final LayoutInflater inflater;
        private final ArrayList<Card> cards;

        public CardAdapter(Context context, Card.Suit suit)
        {
            inflater = LayoutInflater.from(context);
            cards = new ArrayList<>(12);
            for(int i = 1; i <= 13; i++){
                cards.add(new Card(suit, i));
            }
        }

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CardViewHolder(inflater, parent, this);
        }

        @Override
        public void onBindViewHolder(CardViewHolder holder, int position) {
            Card card = cards.get(position);
            holder.setCard(card);
            boolean enabled = true;
            if(callback != null){
                if(callback.getSelected().contains(card)){
                    enabled = false;
                }
            }
            holder.setEnabled(enabled);
        }

        @Override
        public void onClick(View view) {
            Card card = (Card) view.getTag();
            if(callback != null){
                callback.onCardClicked(card);
            }
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView cardImage;

        public CardViewHolder(LayoutInflater inflater, ViewGroup parent, View.OnClickListener listener) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            cardImage = (ImageView) itemView.findViewById(R.id.card);
            cardImage.setOnClickListener(listener);
        }

        public void setCard(Card card)
        {
            cardImage.setImageResource(card.getDrawableResId(itemView.getContext()));
            itemView.setTag(card);
        }

        public void setEnabled(boolean enabled)
        {
            cardImage.setClickable(enabled);
            cardImage.setAlpha(enabled ? 1.0f : 0.5f);
        }
    }

    public interface Callback {
        void onCardClicked(Card card);
        List<Card> getSelected();
    }
}
