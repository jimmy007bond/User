package com.geekworkx.hellocab.Model;

/**
 * Created by parag on 16/03/18.
 */

public class Remarks   {

    private  String _comments;
    private  int  _limit;

    public Remarks () {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Remarks (String _comments,int _limit) {
        this._comments=_comments;
        this._limit=_limit;

    }


    public String get_comments(int position) {
        return _comments;
    }

    public void set_comments(String _comments) {
        this._comments = _comments;
    }

    public int get_limit(int position) {
        return _limit;
    }

    public void set_limit(int _limit) {
        this._limit = _limit;
    }
}
