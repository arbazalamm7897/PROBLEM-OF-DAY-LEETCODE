public class deleteCharacerToMakeFancyString {
  public String makeFancyString(String s) {
        StringBuilder result = new StringBuilder();
        int count = 1; // To count consecutive characters

        for (int i = 0; i < s.length(); i++) {
            // If it's not the first character and current is same as previous
            if (i > 0 && s.charAt(i) == s.charAt(i - 1)) {
                count++;
            } else {
                count = 1;
            }

            // Only add the character if count is less than 3
            if (count < 3) {
                result.append(s.charAt(i));
            }
        }

        return result.toString();
    }
}
