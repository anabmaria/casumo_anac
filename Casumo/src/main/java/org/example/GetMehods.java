package org.example;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class GetMehods {
    String url ;
    Response response;

    public GetMehods(String url){
        this.url = url;
    }

    public void performGetRequest(String value)
    {
        response = RestAssured.get(url+value);
        if(response.getStatusCode() !=  200)
            throw new RuntimeException("Error encountered"+ response.getStatusCode());

    }

    public int getStatusCode(){
        return response.getStatusCode();
    }
    public String getBody(){ return  response.getBody().asString();}

    private Headers getHeaders(){ return  response.getHeaders();}

    public String getHeaderValue(String header){
        Headers responseHeader = response.getHeaders();
        return responseHeader.getValue(header);
    }

    public long getResponseTIme(){
        return response.getTime();
    }
}
