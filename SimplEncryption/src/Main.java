/**
 * @author Maximilian Guzman - gan022 */

public class Main {

	public static void main(String[] args) {
		String[] messages = {
				"Hello world!",
				"UTSA, Birds up!",
				"Being hacker is fun",
				"True"};
		String[] keys = {
				"AXNY",
				"NPB",
				"But risky",
				"Today is a happy day"};
		String msg, msg2 = "", key, cipher = "";
		int j = 0;
		
		for(int k = 0; k < messages.length; k++) {
			msg = messages[k];
			key = keys[k];
			
			System.out.println("Original text: "+ msg);
			
			/* "encrypt" text */
			for(int i = 0; i < msg.length(); i++) {
				cipher += (char ) ((msg.charAt(i) + key.charAt(j)) % 255);
				j++;
				if(j == key.length()) {
					j = 0;
				}
			}
			j = 0;
			
			System.out.println("Ciphered text: " + cipher);
			System.out.print("Ascii text: ");
			for(char c: cipher.toCharArray()) {
				System.out.print((int)c+"-");
			}
			System.out.println();
		
			/* "decrypt" text */
			for(int i = 0; i < cipher.length(); i++) {
				msg2 += (char ) ((cipher.charAt(i) - key.charAt(j)) % 255);
				j++;
				if(j == key.length()) {
					j = 0;
				}
			}
			j = 0;
			
			System.out.println("Deciphered text: "+ msg2);
			System.out.println();
			msg = msg2 = key = cipher = "";
		}
	}

}
