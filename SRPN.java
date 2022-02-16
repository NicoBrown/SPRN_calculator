import java.util.*;
import java.util.regex.*;

/**
 * Program class for an SRPN calculator.
 */

public class SRPN {

  Stack<String> strlist = new Stack<String>(); // for all string characters to be processed
  Stack<Long> numlist = new Stack<Long>(); // for number characters only
  Boolean comment = false; // comment indicator

  String[] strarr = new String[0];

  /** 
  * Call for user input string to be validated and then processes.
  * @param t User string input
  */
  public void processCommand(String t) {
  
    numlist.ensureCapacity(23);
    validate(t); 
    process(); 
  }

  /** 
  * Processes strlist after they have been validated
  */
  public void process()
  {
    if(strlist.empty() == false)
    {
      int j = strlist.size();

      for(int i = 0; i <= j; i++)
      {
        String s = "";
        try 
        {
          s = strlist.firstElement();  
        } 
        catch (Exception e) 
        {
          break;
        }

        if (s.matches("=")) // if nothing been calculated print last digit entered
        {
          if (numlist.empty() == true) // if inputs = and nothings been calculated
          {
            System.out.println("Stack empty.");
            //strlist.remove(0);
          }
          if (numlist.empty() == false) // if inputs = and something has been calculated
          {
            System.out.println(numlist.peek());// not sure whether to print all or just the last one
            //strlist.remove(0);
          }
        }

        else if (s.matches("d")) // Logic is wrong here - NOTE: does D need to move store a value in resultlist?
        {
          if (numlist.empty() == true) // if inputs d and nothings been calculated
          {
            System.out.println(Integer.MIN_VALUE);
          }
          else if (numlist.empty() == false) // if inputs d and something has been calculated
          {
            for (long k : numlist)
            {
            System.out.println(k); // not sure whether to print all or just the one
            }
          }
        }

        else if (s.equals("+") || s.equals("-") || s.equals("/") || s.equals("*") || s.equals("^") || s.equals("%")) {
          int k = numlist.size();


          /*

          need to check special circumstances - something like 1 * =


          */
          if (k <= 1) 
          {
            System.out.println("Stack underflow."); // if numlist isnt long enough
          } 
          else if (numlist.size() >= 2 && s.equals("*")) 
          {
            multiply();
          } 
          else if (numlist.size() >= 2 && s.equals("/")) 
          {
            divide();
          } 
          else if (numlist.size() >= 2 && s.equals("-")) 
          {
            minus();
          } 
          else if (numlist.size() >= 2 && s.equals("%")) 
          {
            modulo();
          } 
          else if (numlist.size() >= 2 && s.equals("+")) 
          {
            add();
          }
          else if (numlist.size() >= 2 && s.equals("^")) 
          {
            pow();
          }
        }

        else if (s.matches("[-+]?\\d*")) // "[-+]?\\d*\\d+"
        {
          numlist.add(Long.parseLong(s));
        }
        
        if(strlist.empty() != true)
        {
        strlist.remove(0);
        }
      }
    }
  }
    
  /**
  * Calculates an addition of the last 2 numbers added to numlist, removes them and pushes the results to the stack.
  */
  private void add() 
  {
    Long b = (long) numlist.pop();
    Long a = (long) numlist.pop();

    Long result = a + b;

    numlist.add(overflowcheck(result));
    postCalc();
    
  }

  /**
  * Calculates a mutiplication of the last 2 numbers added to numlist, removes them and pushes the results to the stack.
  */
  public void multiply() {

    Long b = (long) numlist.pop();
    Long a = (long) numlist.pop();

    Long result = a * b;

    numlist.add(overflowcheck(result));

    postCalc();
  }

  /**
  * Calculates the minus of the last 2 numbers added to numlist, removes them and pushes the results to the stack.
  */
  public void minus() {

    Long b = (long) numlist.pop();
    Long a = (long) numlist.pop();

    Long result = a - b;

    numlist.add(overflowcheck(result));
    postCalc();

  }

  /**
  * Divides the last 2 numbers added to numlist, removes them and pushes the results to the stack.
  * Checks for DIV/0 and fractions.  
  */
  public void divide() {

    Long b = (long) numlist.pop();
    Long a = (long) numlist.pop();

    Long result = 0L;

    if(b == 0) //if second digit is a 0
    {
      System.out.println("Divide by 0.");
      result = 0l;
    }
    else if(a == 0) //if first digit is a 0
    {
      result = 0l;
    }

    else 
    {      
      if (a % b != 0) //the division results in a fraction.
      {
        float fa = (float) a;
        float fb = (float) b;
        result = (long) Math.round(fa/fb);
      }

      else //the division results in an integer.
      {
        result = a / b; 
      }
    }
    numlist.add(overflowcheck(result));
    postCalc();
  
  }
  
  /**
  * Calculates the modulo of the last 2 numbers added to numlist, removes them and pushes the results to the stack.
  */
  private void modulo() 
  {
    Long b = (long) numlist.pop();
    Long a = (long) numlist.pop();

    Long result = a % b;

    numlist.add(overflowcheck(result));

    postCalc();
  }

  /**
  * Calculates the power of the last 2 numbers added to numlist, removes them and pushes the results to the stack.
  */
  private void pow() 
  {
    Double b = (double) numlist.pop();
    Double a = (double) numlist.pop();

    Long result = (long) Math.pow(a, b);

    numlistStackcheck(overflowcheck(result));

    postCalc();

  }

  /**
  * Method runs after any calculation to remove the first member of strlist.
  * Process is called to return to iterating through strlist. 
  */
  public void postCalc()
  {
    if(strlist.empty() != true)
    {
    strlist.remove(0);
    }
    process();
  }

  /**
   * Method check for overloads of integers and returns MAX or MIN int value +-/1. 
   * @param  l the number to check for typical int overload
   */
  public Long overflowcheck(Long l)
  {
    
    if (l > Integer.MAX_VALUE) 
    {
      return (long) Integer.MAX_VALUE;
    } 
    else if (l < Integer.MIN_VALUE) 
    {
      return (long) Integer.MIN_VALUE;
    } 
    else 
    {
      return l;
    }

  }

    /**
   * Method check the size if the stack against the capacity. 
   * @param  l the number to check for typical int overload
   */
  public void numlistStackcheck(long l)
  {    
    if (numlist.size() == numlist.capacity()) //if stack is at full capacity
    {
      System.out.println("Stack overflow.");
    } 
    else if (numlist.size() < numlist.capacity()) 
    {
      numlist.push(overflowcheck(l));
    }
  }
  
  /**
   * Method reads the imput string to check for the comment signifier "#". 
   * @param  s the input string to check for comments
   */

  public String commentCheck(String s)
  {   
    StringBuffer stb = new StringBuffer(s);

    if(s.matches("#"))
    {
      stb = stb.replace(0, stb.length(), "");
      comment = !comment;
    }

    else if(s.indexOf("#") != -1 && s.indexOf("#", s.indexOf("#")+1) != -1)
    {
      if(comment == true) // if comment is already true and s contains 2 #'s remain true
      {
        stb = stb.replace(0,s.indexOf("#"), "");
        stb = stb.replace(s.indexOf("#"),stb.length()-1, "");
      }
      else if (comment == false) // if comment is already false and s contains 2 #'s remain false
      {
        stb.replace(s.indexOf("#"),s.indexOf("#")+1,"");
      }
    }

    else if(s.indexOf("#") != -1 && s.indexOf("#", s.indexOf("#")+1) == -1)
    {
      if(comment == true)// if comment is already true and s contains 1 #'s turn false
      {
        if(stb.indexOf("#") == 0)
        {
          stb = stb.deleteCharAt(0);
        }
        else
        {
          stb = stb.replace(0,stb.indexOf("#"), "");
        }
        comment = !comment;
      }
      else if (comment == false)// if comment is already false and s contains 1 "#"
      {
        if(stb.indexOf("#") == 0)
        {
          stb = stb.replace(0, stb.length(), "");
          comment = true;
        }
        else
        {
          stb = stb.replace(stb.indexOf("#"),stb.length(), "");
          comment = false;
        }

      }
    }
    return stb.toString();    // return modified string
  }

  /**
   * Method attempts to validate the string. If the input string is a single valid char its added to strlist. 
  *  If string cannot be validated, push string to strParse Method 
   * <p>
   * @param  s the user input string
   *   
   */

  public void validate(String s) {

    if(s.matches("#"))
    {
      comment = !comment;
    }
    else
    {
      if (s.matches("[-+]?\\d*\\d+") || s.matches("[\\+\\=\\-\\d\\/\\%\\^\\*]"))
      { 
        strlist.push(s);
      }
      else if(s.matches("r"))
      {
        Random rand = new Random();
        Long longnum = (long) rand.nextInt(Integer.MAX_VALUE); 
        numlistStackcheck(longnum);
      }
      else if(s.matches("[A-Z][a-z]^d^r"))
      {
        System.out.println("Unrecognised operator or operand \"" + s + "\".");
      }
      else 
      {
        strParse(s);
      }
    }
    
  }
 /**
     * Takes an input string and attempts to split into substrings by " ". 
     * if the resulting strings are valid they are pushed onto "strlist", 
     * otherwise they are broken down into chars and then further validated. 
     * <p>
     * @param  s the input string to be parsed from validate
     *   
     */
  
  public void strParse(String s)
  { 
    try
    { 
        strarr = s.split(" "); // try to split string down into parts

        for(String t : strarr) // iterate through string to validate them
        {
          t = commentCheck(t);
          if(comment == false)
          {  
            if(t.matches("[-+]?\\d*\\d+"))// if substring is all digits and comments arent active
            {
              strlist.push(t); //push string to stringlist
            }

            else if(t.matches("[\\+\\=\\-\\d\\/\\%\\^\\*]")) // could be combined with above statement with OR statement but created issues
            {
              strlist.push(t); //push string to stringlist
            }

            else if(t.matches("r"))
            {
              Random rand = new Random();
              Long longnum = (long) rand.nextInt(Integer.MAX_VALUE); 
              numlistStackcheck(longnum);
            }

            else // if string cant be validated check characters are valid inputs
            {
              char[] carr = t.toCharArray(); // convert string to character array
            
              for(int j = 0; j<=carr.length-1; j++) // iterate through chars
              {
                
                Character c = carr[j];
                commentCheck(Character.toString(c));

                if(comment == false)
                {
                  if(Character.isDigit(c))
                    {
                      if(j == 0) 
                      {
                        strlist.push(Character.toString(c)); // Psuh first digit to strlist
                      }
                      else if(j > 0 && Character.isDigit(carr[j-1]) == true) // if previous char was a digit append this char to it
                      {
                        String y = strlist.get(strlist.size()-1);
                        strlist.setElementAt(y.concat(c.toString()), strlist.size()-1);
                      }
                      else if (j > 0 && Character.isLetter(carr[j-1]) == false) // if previous char was a letter add this char to strlist
                      {
                        strlist.push(Character.toString(c));
                      }
                    }

                  else if( j == 0 && c == '*'|| c == '/' || c == 45 || c == '+' || c == '^'|| c == '%'|| c == 'd')
                  {
                    strlist.push(Character.toString(c)); // if this is the first iteration and char is an allowable character add to strlist
                  }
                  else if( j > 0 && (Character.isDigit(carr[j-1]) && c == '*'|| c == '/' || c == 45 || c == '+' || c == '^'|| c == '%'|| c == 'd'))
                  {
                    strlist.push(Character.toString(c)); // if previous char was a digit and char is an allowable character add to strlist
                  }
                  else if (c == '=' && (carr[j-1] == '*'|| carr[j-1] == '/' || carr[j-1] == 45 || carr[j-1] == '+' || carr[j-1] == '^'|| carr[j-1] == '%'|| carr[j-1] == 'd'  ))
                  {
                    strlist.pop(); // if char is "=" and the previous char was an allowable character, replace previous char with this char
                    strlist.push(Character.toString(c));
                  }
                  else if(c == 'r') // Unsure if the "r" number is a randomly generated number or some sort of memory position
                  {
                    Random rand = new Random();
                    Long longnum = (long) rand.nextInt(Integer.MAX_VALUE); 
                    numlistStackcheck(longnum);
                  }
                  else
                  {
                    System.out.println("Unrecognised operator or operand \"" + c + "\".");
                  }
                }
              }
            
            }
          }
        }
      }

      catch (PatternSyntaxException e) 
      {
        
      }
    }
}
  

