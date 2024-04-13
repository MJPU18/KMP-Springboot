package co.edu.unbosque.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.model.StringMatching;

@RestController
@RequestMapping("/stringmatchingkmp")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081", "*" })
public class StringMatchingController {

	private StringMatching sm;

	public StringMatchingController() {
		sm = new StringMatching();
	}

	@GetMapping(path = "/obtainResults")
	public ResponseEntity<String> obtainResults(@RequestParam("txt") String txt, @RequestParam("pat") String pat) {
		sm.setPat(pat);
		sm.setTxt(txt);
		sm.KMPSearch();
		String response = "";
		if (!sm.getIndexes().isEmpty()) {
			for (int i = 0; i < sm.getIndexes().size(); i++) {
				if (i != 0)response += ";";
				int arr[] = sm.getIndexes().get(i);
				response += (arr[0] + "," + arr[1]);

			}
			response+=("\n"+sm.getIndexes().size());
		}
		else {
			response="0";
		}
		return new ResponseEntity<String>(response,HttpStatus.OK);
	}
}
