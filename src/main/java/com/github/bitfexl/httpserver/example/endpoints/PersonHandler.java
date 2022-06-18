package com.github.bitfexl.httpserver.example.endpoints;

import com.github.bitfexl.httpserver.Method;
import com.github.bitfexl.httpserver.advanced.annotations.Handler;
import com.github.bitfexl.httpserver.advanced.annotations.Param;
import com.github.bitfexl.httpserver.advanced.annotations.Path;
import com.github.bitfexl.httpserver.advanced.response.JsonResponse;
import com.github.bitfexl.httpserver.example.Person;

import java.util.ArrayList;

@Path(path = "/person/")
public class PersonHandler {
    private ArrayList<Person> persons;

    public PersonHandler() {
        this.persons = new ArrayList<>();
    }

    @Handler(method = Method.GET)
    public Object getPersons() {
        return persons;
    }

    @Handler(method = Method.POST)
    public Object addPerson(@Param(Param.Type.BODY) Person person) {
        persons.add(person);
        return JsonResponse.of("{\"status\":\"ok\"}");
    }
}
