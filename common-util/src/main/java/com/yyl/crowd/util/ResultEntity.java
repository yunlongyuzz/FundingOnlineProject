package com.yyl.crowd.util;



public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    private String result;

    private String message;

    private T data;

    public static <Type> ResultEntity<Type> successWithoutData(){

        return new ResultEntity<Type>(SUCCESS,null,null);

    }

    public static <Type> ResultEntity<Type> successWithData(Type data){
        return new ResultEntity<Type>(SUCCESS,null,data);
    }

    public static <Type> ResultEntity<Type> failed(String message){
        return new ResultEntity<Type>(FAILED,message,null);
    }


    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
