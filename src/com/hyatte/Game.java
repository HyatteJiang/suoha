package com.hyatte;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
    static int cash = 1000;
    static int bet = 0;
    static int winner = 2;

    public static void main(String[] args) {
        dubo();
    }

    private static void dubo() {
        // 生成13*4=52张牌
        ArrayList<Card> deck = new ArrayList<>(); // 设置牌堆
        CardNumber[] cts = CardNumber.values();
        Flower[] fs = Flower.values();

        //把牌加入牌堆
        for (Flower flower : fs) {
            for (CardNumber cardtype : cts) {
                deck.add(new Card(cardtype, flower));
            }
        }
        Collections.shuffle(deck);// 洗牌

        //建立玩家和电脑的牌
        Card[] player = new Card[5];
        Card[] computer = new Card[5];
        System.out.print("你的底牌是：");
        player[0] = deck.get(0);
        System.out.println(player[0]);
        deck.remove(0);
        System.out.println("电脑获得底牌");
        computer[0] = deck.get(0);
        deck.remove(0);
        bet += 100;
        cash -= 50;

        // 按流程发四张牌
        for (int i = 1; i < 5; i++) {
            player[i] = deck.get(0);
            System.out.println("你的第" + i + "张牌是" + player[i]);
            deck.remove(0);
            //System.out.println("?????????" + i + "????");
            computer[i] = deck.get(0);
            System.out.println("电脑的第" + i + "张牌是" + computer[i]);
            deck.remove(0);
            System.out.println("你的牌是：");
            for (Card card : player) {
                if (card != null) {
                    System.out.println(card);
                }
            }
            System.out.println("电脑的牌是");
            for (int j = 1; j < 5; j++) {
                if (computer[j] != null) {
                    System.out.println(computer[j]);
                }
            }
            Scanner input = new Scanner(System.in);
            System.out.println("是否放弃 Y|N?" + cash);
            String choice = input.next();
            if (choice.equalsIgnoreCase("Y")) {
                System.out.println("下注了" + cash);
                newGame();
            } else {
                System.out.print("输入金额:");
                int touzhu = input.nextInt();
                bet += 2 * touzhu;
                cash -= touzhu;
                continue;
            }
        }
        result(player, computer);
        newGame();
    }

    private static void newGame() {
        System.out.println("继续？Y|N?");
        Scanner input = new Scanner(System.in);
        String choice = input.next();
        if (choice.equalsIgnoreCase("y")) {
            bet = 0;
            dubo();
        } else {
            return;
        }
    }

    /**
     * 计算结果并记录日志
     */
    private static void result(Card[] player, Card[] computer) {// ??????
        System.out.println("***************");
        Arrays.sort(player);
        Arrays.sort(computer);
        // 利用IO流记录玩家的日志
        try {
            FileWriter fr = new FileWriter("record of player.txt", true);
            System.out.println("你的牌是：");
            for (Card card : player) {
                if (card != null) {
                    System.out.println(card);
                    fr.write(card.toString() + "\n");
                }
            }
            System.out.print("你的牌型是" + getCardType(player));
            // 记录电脑的日志
            System.out.println("电脑的牌是：");
            FileWriter fw2 = new FileWriter("record of computer.txt", true);
            for (Card card : computer) {
                if (card != null) {
                    System.out.println(card);
                    fw2.write(card.toString() + "\n");
                }
            }
            fw2.write("游戏时间：" + new Date().toLocaleString() + "金额：" + (bet / 2) + "\n");
            fw2.write("**************\n");

            System.out.print("电脑的牌型是：" + getCardType(computer));
            if (getCardType(player).compareTo(getCardType(computer)) > 0) {
                winner = 1;
            } else if (getCardType(player).compareTo(getCardType(computer)) < 0) {
                winner = 2;
            } else {
                winner = compare(player, computer) > 0 ? 1 : 2;
            }
            if (winner == 1) {
                cash = cash + bet;
                System.out.println("你赢了" + cash);
                fr.write("游戏时间：" + new Date().toLocaleString() + "获得了" + (bet / 2) + ",恭喜\n");
                fr.write("*************\n");

            } else if (winner == 2) {
                System.out.println("你输了" + cash);
                fr.write("游戏时间" + new Date().toLocaleString() + "失去了" + (bet / 2) + ",真蠢\n");
                fr.write("*************\n");
            }
            fr.close();
            fw2.close();
        } catch (IOException e) {
            System.out.println("error in result");
        }
    }

    //是否同花
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

    //是否顺子
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

    //通过5张牌来计算牌型
    private static CardType getCardType(Card[] c) {
        HashMap<CardNumber, Integer> hm = computeNumber(c);
        ArrayList<Integer> al = new ArrayList<>();
        al.addAll(hm.values());
        //如果只有两种点数，则为炸弹或葫芦
        if (al.size() == 2) if (al.get(0) == 1 || al.get(0) == 4) {
            return CardType.BOMB;
        } else {
            return CardType.THREE_ZONE;
        }
            //如果有三种点数，则为三条或两对
        else if (al.size() == 3) if (al.get(0) == 2 || al.get(1) == 2) {
            return CardType.TWO_PAIRS;
        } else {
            return CardType.THREE_WITH_A;
        }
            //如果有四种点数，则为一对
        else if (al.size() == 4) return CardType.A_PAIRS;
            //判断是否同花顺
        else if (tonghua(c) && shunzi(c)) return CardType.WITH_FLOWERS_DASHUN;
            //同花
        else if (tonghua(c)) return CardType.WITH_FLOWERS;
            //顺子
        else if (shunzi(c)) return CardType.SHUNZAI;
            //无
        else return CardType.LEAFLETS;


    }

    //计算牌面点数
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

    private static void change(Card[] cards, int i, int j) {
        if (i >= cards.length || j >= cards.length) return;
        Card tmp = cards[i];
        cards[i] = cards[j];
        cards[j] = tmp;
    }




    private static void sort(Card[] cards) {
        Arrays.sort(cards);

        int n = 0;//记录有几个不重复的
        HashMap<CardNumber, Integer> hm = computeNumber(cards);
        for (CardNumber cn : hm.keySet()) {
            if (hm.get(cn) == 1) {
                n++;
            }
        }
        //n==5 无牌型,n==0 不可能
        //四条 n==1
        if (n == 1 && cards[0] == cards[1]){
            Card[] tmp =new Card[4];
            for(int i=0;i<4;i++){
                tmp[i+1]=cards[i];
            }
            tmp[0]=cards[4];
            cards=tmp;
        }
        else if (n == 2){
            Card[] tmp=new Card[2];
           for (int i=0;i<5;i++){
               if(hm.get(cards[i].getCn())==1){
                            
               }
           }
        }


    }

    //同牌型时的比较，如果事先进行排序，则更简单
    private static int compare(Card[] player, Card[] computer) {
        int result = 0;
        CardNumber tmp = CardNumber.ACE;
        CardNumber tmpc = CardNumber.ACE;
        ArrayList<Card> alp = new ArrayList<>();
        ArrayList<Card> alc = new ArrayList<>();

        if (getCardType(computer) == CardType.LEAFLETS) {
            for (int i = player.length - 1; i >= 0; i--) {
                result = result == 0 ? (player[i].getCn()).compareTo(computer[i].getCn()) : result;
            }
        } else if (getCardType(computer) == CardType.A_PAIRS) {
            HashMap<CardNumber, Integer> hm = computeNumber(player);
            for (CardNumber c : hm.keySet()) {
                if (hm.get(c) == 2) {
                    tmp = c;
                }
            }
            HashMap<CardNumber, Integer> hmc = computeNumber(computer);
            for (CardNumber c : hmc.keySet()) {
                if (hmc.get(c) == 2) {
                    tmpc = c;
                }
            }
            for (Card card : player) {
                if (card.getCn().compareTo(tmp) != 0) {
                    alp.add(card);
                }
            }
            for (Card card : computer) {
                if (card.getCn().compareTo(tmpc) != 0) {
                    alc.add(card);
                }
            }
            Collections.sort(alp);
            Collections.sort(alc);
            Card[] pt = (Card[]) alp.toArray(new Card[0]);
            Card[] ct = (Card[]) alc.toArray(new Card[0]);

            result = tmp.compareTo(tmpc);
            for (int i = ct.length - 1; i >= 0; i--) {
                result = result == 0 ? pt[i].compareTo(ct[i]) : result;
            }
        } else if (getCardType(computer) == CardType.TWO_PAIRS) {
            Card pt1 = new Card();
            Card ct1 = new Card();
            HashMap<CardNumber, Integer> hm = computeNumber(player);
            for (CardNumber c : hm.keySet()) {
                if (hm.get(c) == 1) {
                    tmp = c;
                }
            }
            HashMap<CardNumber, Integer> hmc = computeNumber(computer);
            for (CardNumber c : hmc.keySet()) {
                if (hmc.get(c) == 1) {
                    tmpc = c;
                }
            }
            for (Card card : player) {
                if (card.getCn().compareTo(tmp) != 0) {
                    alp.add(card);
                } else {
                    pt1 = card;
                }
            }
            for (Card card : computer) {
                if (card.getCn().compareTo(tmpc) != 0) {
                    alc.add(card);
                } else {
                    ct1 = card;
                }
            }
            Collections.sort(alp);
            Collections.sort(alc);
            Card[] pt = (Card[]) alp.toArray(new Card[0]);
            Card[] ct = (Card[]) alc.toArray(new Card[0]);
            result = pt[3].getCn().compareTo(ct[3].getCn());
            result = result == 0 ? pt[0].getCn().compareTo(ct[0].getCn()) : result;
            result = result == 0 ? pt1.compareTo(ct1) : result;
        } else if (getCardType(computer) == CardType.THREE_WITH_A) {
            compare_three(player, computer);
        } else if (getCardType(computer) == CardType.SHUNZAI) {
            result = player[4].compareTo(computer[4]);
        } else if (getCardType(computer) == CardType.WITH_FLOWERS) {
            for (int i = computer.length - 1; i >= 0; i--) {
                result = result == 0 ? player[i].compareTo(computer[i]) : result;
            }
        } else if (getCardType(computer) == CardType.THREE_ZONE) {
            compare_three(player, computer);
        } else if (getCardType(computer) == CardType.BOMB) {
            HashMap<CardNumber, Integer> hm = computeNumber(player);
            for (CardNumber c : hm.keySet()) {
                if (hm.get(c) == 4) {
                    tmp = c;
                }
            }
            HashMap<CardNumber, Integer> hmc = computeNumber(computer);
            for (CardNumber c : hmc.keySet()) {
                if (hmc.get(c) == 4) {
                    tmpc = c;
                }
            }
            result = tmp.compareTo(tmpc);
        } else if (getCardType(computer) == CardType.WITH_FLOWERS_DASHUN) {
            result = player[4].compareTo(computer[4]);
        }
        return result;
    }

    private static int compare_three(Card[] player, Card[] computer) {
        CardNumber tmp = CardNumber.ACE;
        CardNumber tmpc = CardNumber.ACE;
        HashMap<CardNumber, Integer> hm = computeNumber(player);
        for (CardNumber c : hm.keySet()) {
            if (hm.get(c) == 3) {
                tmp = c;
            }
        }
        HashMap<CardNumber, Integer> hmc = computeNumber(computer);
        for (CardNumber c : hmc.keySet()) {
            if (hmc.get(c) == 3) {
                tmpc = c;
            }
        }
        return tmp.compareTo(tmpc);
    }
}
