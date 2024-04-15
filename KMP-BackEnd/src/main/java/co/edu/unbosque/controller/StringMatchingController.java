package co.edu.unbosque.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.model.StringMatching;

@RestController
@RequestMapping("/stringmatchingkmp")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081", "*" })
public class StringMatchingController {

	public StringMatchingController() {
	}

	@PostMapping(path = "/obtainResults")
	public ResponseEntity<String> obtainResults(@RequestBody StringMatching sm) {
		String pattern="";
		String txt="";
		boolean encode=true;
		try {
			pattern=URLDecoder.decode(sm.getPat(), "UTF-8");
			txt=URLDecoder.decode(sm.getTxt(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			encode=false;
		}
		if(encode) {
			sm.setPat(pattern);
			sm.setTxt(txt);
		}
		ArrayList<int[]> indexes=sm.KMPSearch();
		String response = "";
		if (!indexes.isEmpty()) {
			for (int i = 0; i < indexes.size(); i++) {
				if (i != 0)response += ";";
				int arr[] = indexes.get(i);
				response += (arr[0] + "," + arr[1]);

			}
			response+=("\n"+indexes.size());
		}
		else {
			response="0";
		}
		return new ResponseEntity<String>(response,HttpStatus.OK);
	}
}