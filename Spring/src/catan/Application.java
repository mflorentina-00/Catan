package catan;

import catan.game.Game;
import catan.game.map.IntersectionGraph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {
	public static Map<String, Game> games = new HashMap<>();

	public static void main(String[] args) {

		SpringApplication.run(Application.class);

		try {
			ConnectivitySimulation conn = new ConnectivitySimulation();
			conn.simulation();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
}
