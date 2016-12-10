package no.pokerodds.pxpdev.pokerodds;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;


public class Card implements Parcelable {

    public static final Card FACE_DOWN = new Card(null, 0);

    private Suit suit;
    private int value;


    public Card(Suit suit, int value)
    {
        this.suit = suit;
        this.value = value;
    }

    protected Card(Parcel in) {
        suit = (Suit) in.readSerializable();
        value = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(suit);
        dest.writeInt(value);
    }

    @Nullable
    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @DrawableRes
    public int getDrawableResId(Context context) {
        if(suit == null || value == 0){
            return R.drawable.face_down;
        }
        TypedArray typedArray = context.getResources()
                .obtainTypedArray(suit.getDrawableArray());
        int resId = typedArray.getResourceId(value - 1, 0);
        typedArray.recycle();
        return resId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        if (value != card.value) return false;
        return suit == card.suit;
    }

    @Override
    public int hashCode() {
        int result = suit != null ? suit.hashCode() : 0;
        result = 31 * result + value;
        return result;
    }

    public enum Suit {
        HEARTS(R.array.hearts, R.string.hearts),
        DIAMONDS(R.array.diamonds, R.string.diamonds),
        CLUBS(R.array.clubs, R.string.clubs),
        SPADES(R.array.spades, R.string.spades),
        ;

        public static int MAX = 4;

        @ArrayRes
        private int drawableArray;

        @StringRes
        private int nameStringId;


        private Suit(@ArrayRes int drawableArray, @StringRes int nameStringId)
        {
            this.drawableArray = drawableArray;
            this.nameStringId = nameStringId;
        }

        @ArrayRes
        public int getDrawableArray() {
            return drawableArray;
        }

        @StringRes
        public int getNameStringId() {
            return nameStringId;
        }
    }
}
