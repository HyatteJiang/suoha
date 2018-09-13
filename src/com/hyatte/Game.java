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
        // ����13*4=52����
        ArrayList<Card> deck = new ArrayList<>(); // �����ƶ�
        CardNumber[] cts = CardNumber.values();
        Flower[] fs = Flower.values();

        //���Ƽ����ƶ�
        for (Flower flower : fs) {
            for (CardNumber cardtype : cts) {
                deck.add(new Card(cardtype, flower));
            }
        }
        Collections.shuffle(deck);// ϴ��

        //������Һ͵��Ե���
        Card[] player = new Card[5];
        Card[] computer = new Card[5];
        System.out.print("��ĵ����ǣ�");
        player[0] = deck.get(0);
        System.out.println(player[0]);
        deck.remove(0);
        System.out.println("���Ի�õ���");
        computer[0] = deck.get(0);
        deck.remove(0);
        bet += 100;
        cash -= 50;

        // �����̷�������
        for (int i = 1; i < 5; i++) {
            player[i] = deck.get(0);
            System.out.println("��ĵ�" + i + "������" + player[i]);
            deck.remove(0);
            //System.out.println("?????????" + i + "????");
            computer[i] = deck.get(0);
            System.out.println("���Եĵ�" + i + "������" + computer[i]);
            deck.remove(0);
            System.out.println("������ǣ�");
            for (Card card : player) {
                if (card != null) {
                    System.out.println(card);
                }
            }
            System.out.println("���Ե�����");
            for (int j = 1; j < 5; j++) {
                if (computer[j] != null) {
                    System.out.println(computer[j]);
                }
            }
            Scanner input = new Scanner(System.in);
            System.out.println("�Ƿ���� Y|N?" + cash);
            String choice = input.next();
            if (choice.equalsIgnoreCase("Y")) {
                System.out.println("��ע��" + cash);
                newGame();
            } else {
                System.out.print("������:");
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
        System.out.println("������Y|N?");
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
     * ����������¼��־
     */
    private static void result(Card[] player, Card[] computer) {// ??????
        System.out.println("***************");
        Arrays.sort(player);
        Arrays.sort(computer);
        // ����IO����¼��ҵ���־
        try {
            FileWriter fr = new FileWriter("record of player.txt", true);
            System.out.println("������ǣ�");
            for (Card card : player) {
                if (card != null) {
                    System.out.println(card);
                    fr.write(card.toString() + "\n");
                }
            }
            System.out.print("���������" + getCardType(player));
            // ��¼���Ե���־
            System.out.println("���Ե����ǣ�");
            FileWriter fw2 = new FileWriter("record of computer.txt", true);
            for (Card card : computer) {
                if (card != null) {
                    System.out.println(card);
                    fw2.write(card.toString() + "\n");
                }
            }
            fw2.write("��Ϸʱ�䣺" + new Date().toLocaleString() + "��" + (bet / 2) + "\n");
            fw2.write("**************\n");

            System.out.print("���Ե������ǣ�" + getCardType(computer));
            if (getCardType(player).compareTo(getCardType(computer)) > 0) {
                winner = 1;
            } else if (getCardType(player).compareTo(getCardType(computer)) < 0) {
                winner = 2;
            } else {
                winner = compare(player, computer) > 0 ? 1 : 2;
            }
            if (winner == 1) {
                cash = cash + bet;
                System.out.println("��Ӯ��" + cash);
                fr.write("��Ϸʱ�䣺" + new Date().toLocaleString() + "�����" + (bet / 2) + ",��ϲ\n");
                fr.write("*************\n");

            } else if (winner == 2) {
                System.out.println("������" + cash);
                fr.write("��Ϸʱ��" + new Date().toLocaleString() + "ʧȥ��" + (bet / 2) + ",���\n");
                fr.write("*************\n");
            }
            fr.close();
            fw2.close();
        } catch (IOException e) {
            System.out.println("error in result");
        }
    }

    //�Ƿ�ͬ��
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

    //�Ƿ�˳��
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

    //ͨ��5��������������
    private static CardType getCardType(Card[] c) {
        HashMap<CardNumber, Integer> hm = computeNumber(c);
        ArrayList<Integer> al = new ArrayList<>();
        al.addAll(hm.values());
        //���ֻ�����ֵ�������Ϊը�����«
        if (al.size() == 2) if (al.get(0) == 1 || al.get(0) == 4) {
            return CardType.BOMB;
        } else {
            return CardType.THREE_ZONE;
        }
            //��������ֵ�������Ϊ����������
        else if (al.size() == 3) if (al.get(0) == 2 || al.get(1) == 2) {
            return CardType.TWO_PAIRS;
        } else {
            return CardType.THREE_WITH_A;
        }
            //��������ֵ�������Ϊһ��
        else if (al.size() == 4) return CardType.A_PAIRS;
            //�ж��Ƿ�ͬ��˳
        else if (tonghua(c) && shunzi(c)) return CardType.WITH_FLOWERS_DASHUN;
            //ͬ��
        else if (tonghua(c)) return CardType.WITH_FLOWERS;
            //˳��
        else if (shunzi(c)) return CardType.SHUNZAI;
            //��
        else return CardType.LEAFLETS;


    }

    //�����������
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

        int n = 0;//��¼�м������ظ���
        HashMap<CardNumber, Integer> hm = computeNumber(cards);
        for (CardNumber cn : hm.keySet()) {
            if (hm.get(cn) == 1) {
                n++;
            }
        }
        //n==5 ������,n==0 ������
        //���� n==1
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

    //ͬ����ʱ�ıȽϣ�������Ƚ������������
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
