package test;


public class Solution {

	public static void main(String[] args) {
		String answer = solution("hello",5);
		System.out.println(answer);
	}
	
	

	public static String solution(String S, int K) {
        // write your code in Java SE 8
        String output = "";
        for(int i = 0; i < S.length(); i++){
            if ( i % K == 0){
                output = output+="-";
            } else {
                if (S.charAt(i) != '-'){
                   //Char CLetter = S.charAt(i);
                   //.toUpper();
                }
            }
        }
        //Arrays.toString(a);
        
        ///S.repl
        return output;
    }
	
}
