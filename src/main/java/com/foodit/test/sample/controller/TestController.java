package com.foodit.test.sample.controller;

import com.threewks.thundr.view.json.JsonView;

/**
 * @author James Faulkner
 */
public class TestController {

    public JsonView testJsonView() {
        Person me = new Person("James", "Faulkner");
        return new JsonView(me);
    }

    public static final class Person {

        private String name;
        private String surname;

        public Person(final String name, final String surname) {
            this.name = name;
            this.surname = surname;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(final String surname) {
            this.surname = surname;
        }
    }
}
