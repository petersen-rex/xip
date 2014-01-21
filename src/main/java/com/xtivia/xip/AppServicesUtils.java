package com.xtivia.xip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;

public class AppServicesUtils {
	public static PrintWriter getPrintWriter(HttpServletResponse response) throws IOException{
		return new PrintWriter(response.getOutputStream());
	}
	
	public static PrintWriter getZipPrintWriter(HttpServletResponse response) throws IOException{
		response.setHeader("Content-Encoding", "gzip");
		return new PrintWriter(new GZIPOutputStream(response.getOutputStream()));
	}
	
	public static void streamOutputFile(String fullPath, HttpServletResponse response) throws IOException{
		ServletOutputStream os = response.getOutputStream();
		FileInputStream is = new FileInputStream(new File(fullPath));
		IOUtils.copy(is, os);
	}
	
	public static void logErrorHigh(Log log, String company, String user, String details, String message){
		log.error(String.format("Error Priority: High, Company: %s, User: %s, Details: %s, Message: %s", company, user, details, message));
	}
	
	public static void logErrorMedium(Log log, String company, String user, String details, String message){
		log.error(String.format("Error Priority: Medium, Company: %s, User: %s, Details: %s, Message: %s", company, user, details, message));
	}
	
}
