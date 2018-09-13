package com.hyatte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {

        Card[] player = {new Card(CardNumber.ACE, Flower.CLUB), new Card(CardNumber.ACE, Flower.DIAMOND),
                new Card(CardNumber.TEN, Flower.HEART), new Card(CardNumber.FOUR, Flower.SPADE),
                new Card(CardNumber.DEUCE, Flower.CLUB)};
        Card[] computer = {new Card(CardNumber.FIVE, Flower.CLUB), new Card(CardNumber.FIVE, Flower.DIAMOND),
                new Card(CardNumber.NINE, Flower.HEART), new Card(CardNumber.SEVEN, Flower.SPADE),
                new Card(CardNumber.EIGHT, Flower.CLUB)};

        System.out.println(CardNumber.ACE.compareTo(CardNumber.TEN));
        Arrays.sort(player);
        for(int i=0 ;i<5 ;i++){
            System.out.println(player[i]);
        }
        char c=0x0a;
        System.out.print("***********"+c+"**********");

    }

    private static boolean tonghua(Card[] c) {
        Flower f = c[0].getF();
        boolean result = true;
        for (int i = 1; i < c.length; i++) {
            if (f.compareTo(c[i].getF()) != 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    private static boolean shunzi(Card[] c) {
        boolean result = true;
        Arrays.sort(c);
        for (int i = 0; i < c.length - 1; i++) {
            if (c[i].getCn().compareTo(c[i + 1].getCn()) != -1) {
                result = false;
                break;
            }
        }
        return result;
    }

    private static CardType getCardType(Card[] c) {
        HashMap<CardNumber, Integer> hm = computeNumber(c);
        ArrayList<Integer> al = new ArrayList<>();
        al.addAll(hm.values());
        if (al.size() == 2) {
            if (al.get(0) == 1 || al.get(0) == 4) {
                return CardType.BOMB;
            } else {
                return CardType.THREE_ZONE;
            }
        } else if (al.size() == 3) {
            if (al.get(0) == 2 || al.get(1) == 2) {
                return CardType.TWO_PAIRS;
            } else {
                return CardType.THREE_WITH_A;
            }
        } else if (al.size() == 4) {
            return CardType.A_PAIRS;
        } else if (tonghua(c) && shunzi(c)) {
            return CardType.WITH_FLOWERS_DASHUN;
        } else if (tonghua(c)) {
            return CardType.WITH_FLOWERS;
        } else if (shunzi(c)) {
            return CardType.SHUNZAI;
        } else {
            return CardType.LEAFLETS;
        }

    }

    private static HashMap<CardNumber, Integer> computeNumber(Card[] c) {
        HashMap<CardNumber, Integer> hm = new HashMap<>();
        for (Card card : c) {
            if (hm.containsKey(card.getCn())) {
                hm.put(card.getCn(), hm.get(card.getCn()) + 1);
            } else {
                hm.put(card.getCn(), 1);
            }
        }
        return hm;
    }
}
