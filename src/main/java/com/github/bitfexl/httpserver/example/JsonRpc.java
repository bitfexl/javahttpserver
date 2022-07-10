package com.github.bitfexl.httpserver.example;

import com.github.bitfexl.httpserver.jsonrpc.JsonRpcServer;
import com.github.bitfexl.httpserver.jsonrpc.annotations.JsonRpcMethod;

import java.util.Random;

public class JsonRpc {
    public static void main(String[] args) throws Exception {
        JsonRpcServer server = new JsonRpcServer().start(65500);

        server.setJsonRpcHandler("/asdf", new JsonRpc());
    }

    @JsonRpcMethod
    public int add(int a, int b) {
        return a+b;
    }

    @JsonRpcMethod
    public String multiply(double a, double b) {
        return String.valueOf(a*b);
    }

    @JsonRpcMethod
    public Person getPerson(String name) {
        Person person = new Person();
        person.name = name;
        person.age = new Random().nextInt(90);
        person.favouriteFoods = new String[] { "apple", "pizza", "meat", "cake" };
        return person;
    }
}
