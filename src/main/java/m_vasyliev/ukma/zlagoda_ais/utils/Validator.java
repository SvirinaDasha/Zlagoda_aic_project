package m_vasyliev.ukma.zlagoda_ais.utils;

import m_vasyliev.ukma.zlagoda_ais.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.Period;

public class Validator {


    public static boolean isEmployeeOlderThan18(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthDate, today);
        return age.getYears() >= 18;
    }


    public static boolean isEmployeeOlderThan18(String birthDateString) {
        LocalDate birthDate = LocalDate.parse(birthDateString);
        return isEmployeeOlderThan18(birthDate);
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() <= 13;
    }

    public static boolean isNonNegative(double value) {
        return value >= 0;
    }

    public static boolean isNonNegative(int value) {
        return value >= 0;
    }

    public static boolean isParsableAsInt(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isParsableAsDouble(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isManager(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute("user");
        return user != null && "Manager".equals(user.getRole());
    }
}
