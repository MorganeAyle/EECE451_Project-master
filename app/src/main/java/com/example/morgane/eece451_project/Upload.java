package com.example.morgane.eece451_project;
/**
 * Created by apple on 12/8/17.
 */

public class Upload {

    public String sender;
    public String url;
    public String receiver;

    public String getsender(){
        return sender;
    }
    public String geturl(){
        return url;
    }

    public String getreceiver(){
        return receiver;
    }
    public Upload(String sender,String url,String receiver)
    {
        this.receiver=receiver;
        this.sender=sender;
        this.url=url;
    }
    public Upload()
    {}

}
