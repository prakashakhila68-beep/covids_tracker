package CovidDataValidator;

public class Validator {
	 public static boolean validateBusinessLogic(int total,
             int active,
             int recovered,
             int deaths) {

int sum = active + recovered + deaths;

// Allow small mismatch tolerance (data rounding issue)
return Math.abs(sum - total) <= 1;
}
}

