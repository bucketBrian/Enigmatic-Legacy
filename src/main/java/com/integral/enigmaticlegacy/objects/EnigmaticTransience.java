package com.integral.enigmaticlegacy.objects;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;

public class EnigmaticTransience {
	private boolean isCursed, isPermanentlyDead;

	private EnigmaticTransience() {
		// NO-OP
	}

	public boolean isCursed() {
		return this.isCursed;
	}

	public void setCursed(boolean isCursed) {
		this.isCursed = isCursed;
	}

	public boolean isPermanentlyDead() {
		return OmniconfigHandler.maxSoulCrystalLoss.getValue() >= 10 && this.isPermanentlyDead;
	}

	public void setPermanentlyDead(boolean isPermanentlyDead) {
		this.isPermanentlyDead = isPermanentlyDead;
	}

	public void write(File directory) {
		if (!directory.exists() || !directory.isDirectory())
			throw new IllegalArgumentException("Directory " + directory + " does not exist or is not a folder!");

		try (FileWriter writer = new FileWriter(new File(directory, "enigmatic_transience.json"))) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(this, writer);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static EnigmaticTransience read(File directory) {
		if (!directory.exists() || !directory.isDirectory())
			throw new IllegalArgumentException("Directory " + directory + " does not exist or is not a folder!");

		File file = new File(directory, "enigmatic_transience.json");

		if (!file.exists() || !file.isFile())
			return new EnigmaticTransience();

		try (FileReader reader = new FileReader(file)) {
			EnigmaticTransience transience = new Gson().fromJson(reader, EnigmaticTransience.class);
			return transience;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
