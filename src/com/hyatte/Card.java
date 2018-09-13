package com.hyatte;

import java.io.Serializable;

public class Card implements Comparable<Card> {
    private CardNumber cn;
    private Flower f;

    public CardNumber getCn() {
        return cn;
    }

    public void setCn(CardNumber ct) {
        this.cn = ct;
    }

    public Flower getF() {
        return f;
    }

    public void setF(Flower f) {
        this.f = f;
    }

    public Card() {
    }

    public Card(CardNumber ct, Flower f) {
        super();
        this.cn = ct;
        this.f = f;
    }

    @Override
    public String toString() {
        return "Card [CardNumber=" + cn + ", Flower=" + f + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cn == null) ? 0 : cn.hashCode());
        result = prime * result + ((f == null) ? 0 : f.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        if (cn != other.cn)
            return false;
        if (f != other.f)
            return false;
        return true;
    }

    @Override
    public int compareTo(Card o) {
        int result = this.getCn().compareTo(o.getCn());
        if (result == 0) {
            result = this.getF().compareTo(o.getF());
        }
        return result;
    }
}
