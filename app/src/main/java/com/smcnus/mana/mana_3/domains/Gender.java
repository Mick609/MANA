package com.smcnus.mana.mana_3.domains;

public class Gender {
    String gender;
    public static final String male = "Male";
    public static final String female = "Female";

    public Gender(boolean isMale) {
        if (isMale) {
            gender = male;
        } else {
            gender = female;
        }
    }

    public boolean isMale() {
        if (gender.equalsIgnoreCase(male)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFemale() {
        if (gender.equalsIgnoreCase(female)) {
            return true;
        } else {
            return false;
        }
    }

    public void setGender(String newGender) {
        gender = newGender;
    }

    public String getGender() {
        return gender;
    }
}
