package co.edu.unbosque.model;

import java.util.ArrayList;

public class StringMatching {

	private String txt;
	private String pat;
	private ArrayList<int[]> indexes;

	public StringMatching() {
		indexes=new ArrayList<>();
	}

	public StringMatching(String txt, String pat, ArrayList<int[]> indexes) {
		this.txt = txt;
		this.pat = pat;
		this.indexes = indexes;
	}

	public void KMPSearch() {
		indexes.clear();
		int M = pat.length();
		int N = txt.length();
		int lps[] = new int[M];
		int j = 0;
		computeLPSArray(pat, M, lps);
		int i = 0;
		while (i < N) {
			if (pat.charAt(j) == txt.charAt(i)) {
				j++;
				i++;
			}
			if (j == M) {
				indexes.add(new int[] { i-j,i });
				j = lps[j - 1];
			} else if (i < N && pat.charAt(j) != txt.charAt(i)) {
				if (j != 0) {
					j = lps[j - 1];
				} else {
					i = i + 1;
				}
			}
		}
	}

	public void computeLPSArray(String pat, int M, int lps[]) {
		int len = 0;
		int i = 1;
		lps[0] = 0;
		while (i < M) {
			if (pat.charAt(i) == pat.charAt(len)) {
				len++;
				lps[i] = len;
				i++;
			} else {

				if (len != 0) {
					len = lps[len - 1];
				} else {
					lps[i] = len;
					i++;
				}
			}
		}
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getPat() {
		return pat;
	}

	public void setPat(String pat) {
		this.pat = pat;
	}

	public ArrayList<int[]> getIndexes() {
		return indexes;
	}

	public void setIndexes(ArrayList<int[]> indexes) {
		this.indexes = indexes;
	}

}
