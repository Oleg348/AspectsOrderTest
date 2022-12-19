package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;


@SpringBootApplication
public class Main implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

    @Autowired
    protected MyService service;

    @Override
    public void run(String... args) throws Exception {
        var key = System.in.read();
        while (key != -1) {
            var uid = Integer.toString(new Random().nextInt());
            service.save(uid);
            key = System.in.read();
        }
    }
}

