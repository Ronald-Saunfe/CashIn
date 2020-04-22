package com.example.cashin;

public class ResponseError {
    private static String errMessage;

    //handle error code
    public static String GetResponse(String err){
        if (err.equals("Unable to resolve host \"us-central1-cashin-270220.cloudfunctions.net\": No address associated with hostname")){
            errMessage = "Check internet connection";
        }
        else if(err.equals("SSL handshake timed out")){
            errMessage = "Slow internet connection";
        }
        else if (err.length()>=17) {
            if (err.substring(0,17).contentEquals("Failed to connect")){
                errMessage = "Connection failed";
            }
        }
        else if (err.length()>=90) {
            if (err.substring(0,90).contentEquals("failed to connect to us-central1-cashin-270220.cloudfunctions.net/216.239.36.54 (port 443)")){
                errMessage = "client timeout";
            }
        }
        else if(err.length()>=11) {
            if (err.substring(0, 11).contentEquals("Read error:")) {
                errMessage = "Try Again";
            }
        }
        else{
            errMessage = err;
        }
        return errMessage;
    }
}
