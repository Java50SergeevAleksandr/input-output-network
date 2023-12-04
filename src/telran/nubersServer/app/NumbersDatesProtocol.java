package telran.nubersServer.app;

import java.io.Serializable;

import java.util.function.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import telran.net.*;

public class NumbersDatesProtocol implements ApplProtocol {
	private static final String MULTIPLY = "Multiply";
	private static final String DIVIDE = "Divide";
	private static final String SUBTRACT = "Subtract";
	private static final String ADD = "Add";
	private static final String DAYS = "Days";
	private static final String DAYS_BETWEEN = "Between";

	public static String getMultiplyProtocol() {
		return MULTIPLY;
	}

	public static String getDivideProtocol() {
		return DIVIDE;
	}

	public static String getSubtractProtocol() {
		return SUBTRACT;
	}

	public static String getAddProtocol() {
		return ADD;
	}

	public static String getDays() {
		return DAYS;
	}

	public static String getDaysBetween() {
		return DAYS_BETWEEN;
	}

	@Override
	public Response getResponse(Request request) {
		String requestType = request.requestType();
		Serializable requestData = request.requestData();
		Response response = new Response(ResponseCode.WRONG_TYPE, "Wrong Type: \"" + requestType + "\"");
		try

		{
			switch (requestType) {

			case ADD -> {
				response = createNumberResponse(requestData, (a, b) -> a + b);
			}
			case SUBTRACT -> {
				response = createNumberResponse(requestData, (a, b) -> a - b);
			}
			case DIVIDE -> {
				response = createNumberResponse(requestData, (a, b) -> a / b);
			}
			case MULTIPLY -> {
				response = createNumberResponse(requestData, (a, b) -> a * b);
			}
			case DAYS -> {
				response = daysResponse(requestData);
			}
			case DAYS_BETWEEN -> {
				response = daysBetweenResponse(requestData);
			}
			}
			;
		} catch (Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, "Wrong Data " + e.getMessage());
		}

		return response;
	}

	private Response daysBetweenResponse(Serializable requestData) {
		LocalDate[] dates = (LocalDate[]) requestData;
		return new Response(ResponseCode.OK, ChronoUnit.DAYS.between(dates[0], dates[1]));
	}

	private Response daysResponse(Serializable requestData) {
		DateDays dateDays = (DateDays) requestData;
		LocalDate date = dateDays.date();
		int days = dateDays.days();
		return new Response(ResponseCode.OK, date.plusDays(days));
	}

	private Response createNumberResponse(Serializable requestData, BinaryOperator<Double> function) {
		double[] numbers = (double[]) requestData;
		return new Response(ResponseCode.OK, function.apply(numbers[0], numbers[1]));

	}

}
