package co.edu.unbosque.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import co.edu.unbosque.controller.HttpClientSynchronous;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class FileBean {

	private UploadedFile file;
	private Map<String, String> data;
	private StringBuffer text;
	private String pat;
	private int repetitions;
	private boolean caseSensitive;

	public FileBean() {
		data = new HashMap<>();
		text = new StringBuffer();
		pat = "";
		repetitions = 0;
		caseSensitive=true;
	}

	public void upload() {
		if (file != null) {
			addMessage(FacesMessage.SEVERITY_INFO, "Successful", file.getFileName() + " is uploaded.");
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		file = event.getFile();
		if (file.getFileName().endsWith(".txt")) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
				String line = "";
				text = new StringBuffer();
				while ((line = br.readLine()) != null) {
					text.append(line).append("\n");
				}
				loadData(file.getFileName().replace(".txt", ""), text.toString());
				addMessage(FacesMessage.SEVERITY_INFO, "Successful", file.getFileName() + " is uploaded.");
				repetitions = 0;
				pat = "";
			} catch (IOException e) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error reading content.");
			}
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Only .txt files are allowed");
		}
	}

	public void markWord() {
		if (text.isEmpty()) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please upload a .txt file");
			return;
		} else if (pat.isEmpty()) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please enter a word.");
			return;
		}
		try {
			String txt=text.toString(),pattern=pat;
			if(!caseSensitive) {
				txt=txt.toLowerCase();
				pattern=pat.toLowerCase();
			}
			String url = "http://localhost:8081/stringmatchingkmp/obtainResults";
			String json = "{\"txt\": \"" + URLEncoder.encode(txt, "UTF-8") + "\", \"pat\": \""
					+ URLEncoder.encode(pattern, "UTF-8") + "\"}";
			String info = HttpClientSynchronous.doPost(url, json);
			if (info.equals("0")) {
				addMessage(FacesMessage.SEVERITY_INFO, "Info", "No matches found.");
				repetitions = 0;
				loadData(file.getFileName().replace(".txt", ""), text.toString());
			} else {
				String data[] = info.split("\n");
				ArrayList<int[]> indexes = new ArrayList<>();
				for (String row : data[0].split(";")) {
					String column[] = row.split(",");
					indexes.add(new int[] { Integer.parseInt(column[0]), Integer.parseInt(column[1]) });
				}
				StringBuffer aux = new StringBuffer(text.toString());
				int leni = 51, lenf = 7;
				int carry = 0;
				int ant[] = null;
				for (int arr[] : indexes) {
					if(ant!=null && arr[0]<ant[1])arr[0]=ant[1];
					aux.insert(arr[0] + carry, "<span style='background-color: yellow !important;'>");
					carry += leni;
					aux.insert(arr[1] + carry, "</span>");
					carry += lenf;
					ant=arr;
				}
				repetitions = Integer.parseInt(data[1]);
				loadData(file.getFileName().replace(".txt", ""), aux.toString());
			}
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error when searching for matches.");
		}
	}

	public void loadData(String title, String info) {
		data.put("title", title);
		data.put("body", info.replace("\n", "<br>"));
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public StringBuffer getText() {
		return text;
	}

	public void setText(StringBuffer text) {
		this.text = text;
	}

	public String getPat() {
		return pat;
	}

	public void setPat(String pat) {
		this.pat = pat;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}