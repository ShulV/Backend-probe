package ru.shulpov.testingjunit5.entity;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 20.01.2026 15:34
 */
public class User {
    private final String email;
    private final int age;

    public User(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}

