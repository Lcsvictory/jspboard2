package utils;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.JspWriter;

public class JSFunction {
	public static void alertLocation(String msg, String url, JspWriter out) {
		try {
			String script = ""
							+ "<script>"
							+ "		alert('" + msg + "');"
							+ " 	location.href='" + url + "';"
							+ "</script>";
			out.println(script);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void alertLocation(HttpServletResponse resp, String msg, String url) {
	    try {
	        resp.setContentType("text/html; charset=UTF-8"); // 콘텐츠 타입과 문자 인코딩 설정
	        PrintWriter writer = resp.getWriter();
	        String script = ""
	                        + "<script>"
	                        + "		alert('" + msg + "');"
	                        + " 	location.href='" + url + "';"
	                        + "</script>";
	        writer.println(script);
	        writer.flush(); // 버퍼를 비움
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void alertBack(String msg, JspWriter out) {
		try {
			String script = ""
							+ "<script>"
							+ "		alert('" + msg + "');"
							+ " 	history.back();"
							+ "</script>";
			out.println(script);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void alertBack(HttpServletResponse resp, String msg) {
		try {
			resp.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			String script = ""
						+ "<script>"
						+ "		alert('" + msg + "');"
						+ "		history.back();"
						+ "</script>";
			writer.print(script);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void alertMsg(HttpServletResponse resp, String msg) {
		try {
			resp.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			String script = ""
						+ "<script>"
						+ "		alert('" + msg + "');"
						+ "</script>";
			writer.print(script);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
