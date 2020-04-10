package com.game.Catan;

import com.game.Catan.API.BaseResponse;
import com.game.Catan.API.HttpClientPost;
import com.game.Catan.Game.GameClass;
import com.game.Catan.API.Requests.ManagerRequest;
import com.game.Catan.API.Requests.UserRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class CatanApplication {
	public static Map<String,GameClass> games = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(CatanApplication.class, args);
		try {
			ConnectivitySimulation con=new ConnectivitySimulation();
			con.simulation();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
