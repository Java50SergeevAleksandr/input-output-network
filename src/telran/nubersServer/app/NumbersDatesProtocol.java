package telran.nubersServer.app;

import java.util.*;
import java.util.function.BinaryOperator;

import telran.net.*;

public class NumbersDatesProtocol implements ApplProtocol {
	private static final String MULTIPLY = "Multiply";
	private static final String DIVIDE = "Divide";
	private static final String SUBTRACT = "Subtract";
	private static final String ADD = "Add";

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

	@Override
	public Response getResponse(Request request) {
		Response response = new Response(ResponseCode.WRONG_TYPE, "Wrong Type: \"" + request.requestType() + "\"");

		switch (request.requestType()) {

		case ADD -> {
			response = createNumberResponse(request, (a, b) -> a + b);
		}
		case SUBTRACT -> {
			response = createNumberResponse(request, (a, b) -> a - b);
		}
		case DIVIDE -> {
			response = createNumberResponse(request, (a, b) -> a / b);
		}
		case MULTIPLY -> {
			response = createNumberResponse(request, (a, b) -> a * b);
		}
		}
		;
		return response;
	}

	@SuppressWarnings("unchecked")
	private Response createNumberResponse(Request request, BinaryOperator<Double> function) {
		Response response;
		try {
			ArrayList<Double> list = (ArrayList<Double>) request.requestData();
			response = new Response(ResponseCode.OK, function.apply(list.get(0), list.get(1)));
		} catch (Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, "Wrong Data " + e.toString());
		}
		return response;
	}

}
