package com.solvd.laba.homework10.exercise02;

import java.lang.reflect.*;

public class Reflections {
    public static void main(String[] args) {
        // print class details
        try {
            printClassDetails("com.solvd.laba.homework02.exercise01.Address");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found. Details: " + e.toString());
        }

        // create object and call method from reflection
        try {
            System.out.println("\nCreating Person class using reflection");

            Class<?> cl = Class.forName("com.solvd.laba.homework02.exercise01.Person");
            Class[] constructorParameters = {String.class, String.class, String.class};
            Object newInstance = cl.getDeclaredConstructor(constructorParameters)
                    .newInstance("12312", "John", "Smith");

            System.out.println("Instance Created");

            System.out.println("Invoking method using reflection");

            Method method = cl.getMethod("getFullName");
            String result = (String) method.invoke(newInstance);

            System.out.println("Method invoked!");
            System.out.println("result: " + result);

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found. Details: " + e.toString());
        } catch (InstantiationException e) {
            System.out.println("Unable to instantiate the class. Details: " + e.toString());
        } catch (IllegalAccessException e) {
            System.out.println("Member or constructor not accessible. Details: " + e.toString());
        } catch (NoSuchMethodException e) {
            System.out.println("No such method. Details: " + e.toString());
        } catch (InvocationTargetException e) {
            System.out.println("Invocation Target Exception. Details: " + e.toString());
        }
    }

    public static void printClassDetails(String className) throws ClassNotFoundException {
        Class<?> cl = Class.forName(className);
        System.out.println("Class name:      " + cl.getName());
        System.out.println("Class modifiers: " + Modifier.toString(cl.getModifiers()));
        System.out.println("Class fields:");
        for (Field field : cl.getDeclaredFields()) {
            System.out.println("- " + fieldToString(field));
        }
        System.out.println("Class constructors:");
        for (Constructor<?> constructor : cl.getDeclaredConstructors()) {
            System.out.println("- " + constructorToString(constructor));
        }
        System.out.println("Class methods:");
        for (Method method : cl.getDeclaredMethods()) {
            System.out.println("- " + methodToString(method));
        }
    }

    public static String fieldToString(Field field) {
        return Modifier.toString(field.getModifiers())
                + " " + field.getType().getName()
                + " " + field.getName();
    }

    public static String constructorToString(Constructor<?> constructor) {
        String result = Modifier.toString(constructor.getModifiers()) + " (";

        boolean firstParameter = true;
        for (Class<?> cl : constructor.getParameterTypes()) {
            result += firstParameter ? "" : ", ";
            result += cl.getCanonicalName();
            firstParameter = false;
        }
        result += ")";
        return result;
    }

    public static String methodToString(Method method) {
        String result = Modifier.toString(method.getModifiers())
                + " " + method.getName()
                + "(";

        boolean firstParameter = true;
        for (Parameter parameter : method.getParameters()) {
            result += firstParameter ? "" : ", ";
            result += parameter.getType().getCanonicalName();
            result += " " + parameter.getName();
            firstParameter = false;
        }
        result += ")";
        return result;
    }

}
