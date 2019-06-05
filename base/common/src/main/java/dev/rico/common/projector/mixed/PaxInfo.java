package dev.rico.common.projector.mixed;


import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaxInfo {
   private final int standardCount;
   private final int malesCount;
   private final int femalesCount;
   private final int childrenCount;
   private final int infantsCount;

   private PaxInfo(int standardCount, int malesCount, int femalesCount, int childrenCount, int infantsCount) {
      this.standardCount = standardCount;
      this.malesCount = malesCount;
      this.femalesCount = femalesCount;
      this.childrenCount = childrenCount;
      this.infantsCount = infantsCount;
   }

   /**
    * Erzeugt aus einem String nach dem Muster nM nF nC nI
    * ein PaxInfo-Objekt. Dabei gilt: M=Male, F=Female, C=Child, I=Infant
    * PaxInfo ist immutable.
    *
    * @throws IllegalArgumentException falls der String kein PaxInfo-Objekt beschreibt
    */
   public static PaxInfo from(String paxCodeString) {
      Objects.requireNonNull(paxCodeString);
      paxCodeString = paxCodeString.trim();
      if (paxCodeString.isEmpty()) {
         return new PaxInfo(0, 0, 0, 0, 0);
      }
      paxCodeString = paxCodeString.replaceAll(",", " ");
      paxCodeString = paxCodeString.replaceAll("-", " ");
      paxCodeString = paxCodeString.replaceAll("/", " ");
      paxCodeString = paxCodeString.toUpperCase();
      String[] parts = paxCodeString.split("\\s+");
      int standardCount = 0;
      int malesCount = 0;
      int femalesCount = 0;
      int childrenCount = 0;
      int infantsCount = 0;
      for (String part : parts) {
         Matcher matcher = Pattern.compile("^(\\d+)([MFCI])$").matcher(part);
         if (matcher.matches()) {
            int groupCount = matcher.groupCount();
            if (groupCount != 2) {
               throw new IllegalArgumentException("Unrecognizable part: " + part);
            }
            int personCount = Integer.valueOf(matcher.group(1));
            switch (matcher.group(2)) {
               case "M":
                  malesCount = personCount;
                  break;
               case "F":
                  femalesCount = personCount;
                  break;
               case "C":
                  childrenCount = personCount;
                  break;
               case "I":
                  infantsCount = personCount;
                  break;
               default:
                  throw new IllegalArgumentException("Unrecognizable part: " + part);
            }
         } else {
            matcher = Pattern.compile("^(\\d+)$").matcher(part);
            if (matcher.matches()) {
               standardCount = Integer.valueOf(matcher.group(1));
            } else {
               throw new IllegalArgumentException("Unrecognizable part: " + part);
            }
         }
      }
      return new PaxInfo(standardCount, malesCount, femalesCount, childrenCount, infantsCount);
   }

   public String toString() {
      String result = "";
      result += subString(standardCount, "");
      result += subString(malesCount, "M");
      result += subString(femalesCount, "F");
      result += subString(childrenCount, "C");
      result += subString(infantsCount, "I");
      return result.trim();
   }

   private String subString(int count, String letter) {
      if (count > 0) {
         return " " + count + letter;
      }
      return "";
   }

   public int combined() {
      return childrenCount + femalesCount + infantsCount + malesCount + standardCount;
   }
}
