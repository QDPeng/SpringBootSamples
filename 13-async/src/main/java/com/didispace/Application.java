package com.didispace;

import com.didispace.async.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Future;

@SpringBootApplication
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		Task task=new Task();
		try {
			Future<String> future =task.doTaskOne();
			future.cancel(true);
			task.doTaskTwo();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
