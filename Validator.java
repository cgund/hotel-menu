package menu;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/*
Class for validating user input. Contains static methods.  Built for Unit 1 project,
but applicable for this one as well
*/
public class Validator 
{
    /*
    Returns true if specified array of TextField objects are not empty, otherwise
    returns false
    */
    public static boolean subFieldsComplete(TextField...textFields)
    {
        int index = 0;
        boolean fieldsComplete = true;
        while (fieldsComplete && index < textFields.length)
        {
            if (textFields[index].getText().isEmpty())
            {
                fieldsComplete = false;
            }
            index++;
        }
        return fieldsComplete;
    }
    
    /*
    Returns false if the Text property of any TextField contained in the GridPane
    is empty, otherwise returns true
    @param gp The GridPane to check for empty TextField Text properties
    @param tfSkip An array of TextFields that are not to be checked 
    */
    public static boolean fieldsComplete(GridPane gp, TextField...tfSkip)
    {
        boolean fieldsComplete = true;
        int index = 0;
        int length = gp.getChildren().size();
        boolean continueIteration = true;
        while (fieldsComplete && continueIteration && index < length)
        {
            Node n = gp.getChildren().get(index);
            if (n instanceof TextField)
            {
                TextField tf = (TextField)n;
                boolean inSkipList = false;
                int i = 0;
                if (tfSkip != null)
                {
                    while (!inSkipList && i < tfSkip.length)
                    {
                        if (tf == tfSkip[i])
                        {
                            inSkipList = true;
                        }
                        i++;
                    }                     
                }         
                if (!inSkipList)
                {
                    if (tf.getText().isEmpty())
                    {
                        fieldsComplete = false;
                    }                    
                }
            }
            index++;
        }
        return fieldsComplete;
    }
    
    /*
    Validate Social Security Number
    */
    public static void SSN(String SSN) throws IllegalArgumentException
    {
        String regex = "([1-9]\\d{8})|([1-9]\\d{2}-\\d{2}-\\d{4})";
        SSN = SSN.trim();
        if (!SSN.matches(regex))
        {
            throw new IllegalArgumentException("Invalid SS format\ne.g. 555-55-5555 or 444556666") ;          
        }
    }
    
    /*
    Validate phone number
    */
    public static void phone(String phone) throws IllegalArgumentException
    {
        String regex = "([2-9]\\d{9})|(([2-9]\\d{2}-)?\\d{3}-\\d{4})";
        phone = phone.trim();
        if(!phone.matches(regex))
        {
            throw new IllegalArgumentException("Invalid phone format\ne.g. 555-555-5555 or 4445556666");
        }
    }
    /*
    Validate age
    */    
    public static void age(String age) throws IllegalArgumentException
    {
        String regex = "\\d{1,3}";
        age = age.trim();
        if(!age.matches(regex))
        {
            throw new IllegalArgumentException("Invalid age format: 2-3 digit positive number required");
        }        
    }
    /*
    Validate year
    */    
    public static void year(String year) throws IllegalArgumentException
    {
        String regex = "(19\\d{2})|(20\\d{2})";
        year = year.trim();
        if(!year.matches(regex))
        {
            throw new IllegalArgumentException("Invalid year format\nMin = 1900\nMax = 2099");
        }         
    }
    /*
    Validate GPA
    */    
    public static void GPA(String GPA) throws IllegalArgumentException
    {
        String regex = "[0-4]?\\.(\\d{1,2})";
        GPA = GPA.trim();
        if(!GPA.matches(regex))
        {
            throw new IllegalArgumentException("Invalid GPA format\ne.g. 3.51 or 3.5\nMin = 0\nMax = 4.99");
        }          
    }
    /*
    Validate salary
    */
    public static void salary(String salary) throws IllegalArgumentException
    {
        String regex = "\\$?\\d*(\\.(\\d{1,2}))?";
        salary = salary.trim();
        if (!salary.matches(regex))
        {
            throw new IllegalArgumentException("Invalid salary format\ne.g. $30000 or 30000");
        }
    }
    /*
    Validate hourly rate
    */    
    public static void hourlyRate(String rate) throws IllegalArgumentException
    {
        String regex = "\\$?\\d*(\\.(\\d{1,2}))?";
        rate = rate.trim();
        if(!rate.matches(regex))
        {
            throw new IllegalArgumentException("Invalid rate format\ne.g. 15.99 or 15");
        }          
    }
    /*
    Validate hours worked
    */
    public static void hoursWorked(String hours)throws IllegalArgumentException
    {
        String regex = "\\d*(\\.(\\d{1,2}))?";
        hours = hours.trim();
        if(!hours.matches(regex))
        {
            throw new IllegalArgumentException("Invalid hours format: number required");
        }         
    }
    
    /*
    Validates input dates
    */
    public static boolean dateRange(LocalDate inputDate, int daysBefore, int daysAfter) throws IllegalArgumentException
    {
        LocalDate dateNow = LocalDate.now();
        
        if (inputDate.isBefore(dateNow.minusDays(daysBefore)))
        {
            throw new IllegalArgumentException("Invalid date: must be today or later");
        }
        else if (inputDate.isAfter(dateNow.plusDays(daysAfter)))
        {
            throw new IllegalArgumentException("Invalid date: date must be within 1 week of today");
        }
        return true;
    }
    
    /*
    Validates time input
    */
    public static boolean time(LocalDate inputDate, String inputTime)
    {
        LocalDate today = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime in = LocalTime.parse(inputTime, f);
        
        if (inputDate.equals(today) && in.isBefore(timeNow))
        {
            throw new IllegalArgumentException("Invalid time: time cannot be in the past");
        }
        return true;
    }
}
